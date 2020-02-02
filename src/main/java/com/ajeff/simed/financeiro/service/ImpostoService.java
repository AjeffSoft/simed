package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.TabelaIRPF;
import com.ajeff.simed.financeiro.model.TabelaIRPJ;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
import com.ajeff.simed.financeiro.service.calculos.CalculoINSS;
import com.ajeff.simed.financeiro.service.calculos.CalculoImpostoRenda;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.util.CalculosComDatas;

@Service
public class ImpostoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ImpostoService.class);	
	
	@Autowired
	private TabelasIrpfRepository tabelaIRPFRepository;
	@Autowired
	private TabelasIrpjRepository tabelaIRPJRepository;
	

	
	
	
	//TODO: Disponibilizar valor desconto dependente em banco de dados
	private static final BigDecimal DEP_VALOR = new BigDecimal(189.59);
	private static final BigDecimal TETO_INSS = new BigDecimal(6101.05);
	
	//TODO: Criar tabela para calculos INSS (Pessoa Fisica e futuros funcionários)
	private static final BigDecimal ALIQUOTA_INSS = new BigDecimal(11);
	
	
	
	
	
	public List<Imposto> retencaoImpostos(ContaPagar contaPagar, ContaPagar cp) {

		try {
			String tipoImposto = new String();
			List<Imposto> impostos = new ArrayList<>();
			
			if (contaPagar.getReterINSS()) {
				tipoImposto = "INSS";
				impostos.add(imposto(contaPagar, tipoImposto, cp));
			}
			
			if (contaPagar.getReterIR()) {
				tipoImposto = "IRRF";
				impostos.add(imposto(contaPagar, tipoImposto, cp));
			}
			
			
			if (contaPagar.getReterCOFINS() && contaPagar.getFornecedor().getTipo().equals("J")) {
				tipoImposto = "PCCS";
				impostos.add(imposto(contaPagar, tipoImposto, cp));
			}
			
			if (contaPagar.getIssPorcentagem() != null) {
				tipoImposto = "ISS";
				impostos.add(imposto(contaPagar, tipoImposto, cp));
			}
			return impostos;
		} catch (Exception e) {
			throw new RegistroNaoCadastradoException("Ocorreu um erro ao tentar gerar o(s) imposto(s)");
		}

	}


	private Imposto imposto(ContaPagar contaPagar, String tipoImposto, ContaPagar cp) {
		Imposto imp = new Imposto();
		imp.setApuracao(contaPagar.getDataEmissao().minusMonths(0).with(TemporalAdjusters.lastDayOfMonth()));
		imp.setJuros(BigDecimal.ZERO);
		imp.setMulta(BigDecimal.ZERO);
		imp.setValorNF(contaPagar.getValor());
		imp.setNumeroNF(contaPagar.getNotaFiscal());
		imp.setStatus("ABERTO");
		imp.setEmissaoNF(contaPagar.getDataEmissao());
		imp.setNome(tipoImposto);
		imp.setHistorico(imp.getNome() + " - " + contaPagar.getFornecedor().getFantasia() + " - " + imp.getNumeroNF());
		imp.setContaPagar(cp);
		calcularImpostoPorTipo(contaPagar, tipoImposto, imp);
		imp.setTotal(imp.getValor());
		return imp;
	}


	private void calcularImpostoPorTipo(ContaPagar contaPagar, String tipoImposto, Imposto imp) {
		
		if(tipoImposto.equals("INSS")) {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 15));
			imp.setValor(CalculoINSS.calculoINSS(contaPagar.getValor(), TETO_INSS, ALIQUOTA_INSS));
		
		}else if(tipoImposto.equals("IRRF")) {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 20));

			if(contaPagar.getFornecedor().getTipo().equals("F")) {
				BigDecimal valorBase = contaPagar.getValor();
				imp.setValor(regraRetencaoIRRFPessoaFisica(contaPagar, valorBase));
			}else {
				imp.setValor(regraRetencaoIRRFPessoaJuridica(contaPagar));
			}
		}else if(tipoImposto.equals("PCCS")) {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 20));
			TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);
			imp.setValor(CalculoImpostoRenda.calculoPCCS(contaPagar.getValor(), tabela.getAliquotaPIS(), tabela.getAliquotaCOFINS(), tabela.getAliquotaCSLL()));
		}else {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 10));
			BigDecimal iss = new BigDecimal(0);
			iss = contaPagar.getValor().multiply(contaPagar.getIssPorcentagem()).divide(new BigDecimal(100).setScale(2, RoundingMode.HALF_UP));
			imp.setValor(iss);
		}
	}


	private BigDecimal regraRetencaoIRRFPessoaFisica(ContaPagar contaPagar, BigDecimal valorBase) {
		if(contaPagar.getReterINSS()) {
			valorBase = valorBase.subtract(CalculoINSS.calculoINSS(contaPagar.getValor(), TETO_INSS, ALIQUOTA_INSS));
		}
		valorBase = valorBase.subtract(CalculoImpostoRenda.calculoTotalDescontoDependente(contaPagar.getFornecedor().getDependente(), DEP_VALOR));
		return setarAFaixaRetencaoIRRF(valorBase);
	}

	
	private BigDecimal regraRetencaoIRRFPessoaJuridica(ContaPagar contaPagar) {
		TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);

		if(tabela.getId() != null) {
			return CalculoImpostoRenda.calculoIRPJ(contaPagar.getValor(), tabela.getAliquotaIR());
		}else {
			return BigDecimal.ZERO;
		}

	}	
	

	private BigDecimal setarAFaixaRetencaoIRRF(BigDecimal valorBase) {
		List<TabelaIRPF> faixas = tabelaIRPFRepository.findByValorFinalGreaterThanEqual(valorBase);
		TabelaIRPF faixa = new TabelaIRPF();

		for(TabelaIRPF f : faixas) {
			if(f.getValorInicial().compareTo(valorBase) == 0 || f.getValorInicial().compareTo(valorBase) == -1) {
				faixa = f;
			}
		}

		if(faixa.getId() != null) {
			return CalculoImpostoRenda.calculoIRRF(valorBase, faixa.getAliquota(), faixa.getDeducao());
		}else {
			return BigDecimal.ZERO;
		}
	}


	private LocalDate setarVencimentoSomarDiasEDiaUtil(LocalDate dataBase, Integer dias) {
		LocalDate data = (CalculosComDatas.sumDays(dataBase, dias));
		if (CalculosComDatas.dataFinalSemana(data)) {
			return CalculosComDatas.dataDomingo(data) ? data.minusDays(2) : data.minusDays(1);
		}else {
			return data;
		}
	}

}
