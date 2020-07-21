package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.model.TabelaIRPF;
import com.ajeff.simed.financeiro.model.TabelaIRPJ;
import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.ImpostosRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;
import com.ajeff.simed.financeiro.service.calculos.CalculoINSS;
import com.ajeff.simed.financeiro.service.calculos.CalculoImpostoRenda;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.util.DatasUtils;

@Service
public class ImpostoService {

	@Autowired
	private ImpostosRepository repository;
	@Autowired
	private TabelasIrpfRepository tabelaIRPFRepository;
	@Autowired
	private TabelasIrpjRepository tabelaIRPJRepository;
	@Autowired
	private ContasPagarRepository contaPagarRepository;

	
	
	// TODO: Disponibilizar valor desconto dependente em banco de dados
	private static final BigDecimal DEP_VALOR = new BigDecimal(189.59);
	private static final BigDecimal TETO_INSS = new BigDecimal(6101.06);

	// TODO: Criar tabela para calculos INSS (Pessoa Fisica e futuros funcionários)
	private static final BigDecimal ALIQUOTA_INSS = new BigDecimal(11);

	
	
	public List<Imposto> calcularImpostos(ContaPagar contaPagar) {
		return retencaoImpostosPorTipo(contaPagar);
	}	
	
	
	private List<Imposto> retencaoImpostosPorTipo(ContaPagar contaPagar) {
		List<Imposto> impostos = new ArrayList<>();

		if (contaPagar.getReterINSS() && contaPagar.getFornecedor().getTipo().equals("F")) {
			impostos.add(novoImposto(contaPagar, "INSS"));
		}

		if (contaPagar.getReterIR()) {
			impostos.add(novoImposto(contaPagar, "IRRF"));
		}

		if (contaPagar.getReterCOFINS() && contaPagar.getFornecedor().getTipo().equals("J")) {
			impostos.add(novoImposto(contaPagar, "PCCS"));
		}

		if (contaPagar.getIssPorcentagem() != null && contaPagar.getIssPorcentagem().compareTo(BigDecimal.ZERO) == 1) {
			impostos.add(novoImposto(contaPagar, "ISS"));
		}
		return impostos;
	}
	
	
	public Imposto novoImposto(ContaPagar contaPagar, String tipoImposto) {
		Imposto imp = new Imposto();
		imp.setApuracao(DatasUtils.setarParaUltimoDiaMes(contaPagar.getDataEmissao()));
		imp.setJuros(BigDecimal.ZERO);
		imp.setMulta(BigDecimal.ZERO);
		imp.setValorNF(contaPagar.getValor());
		imp.setNumeroNF(contaPagar.getNotaFiscal());
		imp.setStatus(StatusContaPagar.ABERTO);
		imp.setEmissaoNF(contaPagar.getDataEmissao());
		imp.setNome(tipoImposto);
		imp.setHistorico(imp.getNome() + " - " + contaPagar.getFornecedor().getFantasia() + " - " + imp.getNumeroNF());
		imp.setVencimento(setarDataVencimentoPorTipoImposto(imp.getApuracao(), tipoImposto));
		return imp;
	}
	
	
	private LocalDate setarDataVencimentoPorTipoImposto(LocalDate data, String tipoImposto) {
		if (tipoImposto.equals("INSS")) {
			return DatasUtils.setarDataSomandoDiasNoInicioMes(data.plusMonths(1), 15, true, true);

		} else if (tipoImposto.equals("IRRF") || tipoImposto.equals("PCCS")) {
			return DatasUtils.setarDataSomandoDiasNoInicioMes(data.plusMonths(1), 20, true, true);

		} else {
			return DatasUtils.setarDataSomandoDiasNoInicioMes(data.plusMonths(1), 10, true, false);
		}
	}	
	

	private  BigDecimal setarValorImpostoPorTipo(ContaPagar contaPagar) {

		if (contaPagar.getReterINSS()) {
			return CalculoINSS.calculoINSS(contaPagar.getValor(), TETO_INSS, ALIQUOTA_INSS);

		} else if (contaPagar.getReterIR()) {
			return regrasParaIRRFPorTipoFornecedor(contaPagar);
			
		} else if (contaPagar.getReterCOFINS() && contaPagar.getFornecedor().getTipo().equals("J")) {
			TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);
			return CalculoImpostoRenda.calculoPCCS(contaPagar.getValor(), tabela.getAliquotaPIS(), tabela.getAliquotaCOFINS(), tabela.getAliquotaCSLL());
		} else {
			return contaPagar.getValor().multiply(contaPagar.getIssPorcentagem()).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		}
	}
	

	private BigDecimal regrasParaIRRFPorTipoFornecedor(ContaPagar contaPagar) {
		if (contaPagar.getFornecedor().getTipo().equals("F")) {
			return regraRetencaoIRRFPessoaFisica(contaPagar);
		} else {
			return regraRetencaoIRRFPessoaJuridica(contaPagar);
		}
	}

	private BigDecimal regraRetencaoIRRFPessoaFisica(ContaPagar contaPagar) {
		BigDecimal valorBase = contaPagar.getValor();
		if (contaPagar.getReterINSS()) {
			valorBase = valorBase.subtract(CalculoINSS.calculoINSS(contaPagar.getValor(), TETO_INSS, ALIQUOTA_INSS));
		}
		valorBase = valorBase.subtract(CalculoImpostoRenda
				.calculoTotalDescontoDependente(contaPagar.getFornecedor().getDependente(), DEP_VALOR));
		return setarAFaixaRetencaoIRRF(valorBase);
	}
	

	private BigDecimal regraRetencaoIRRFPessoaJuridica(ContaPagar contaPagar) {
		TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);

		if (tabela.getId() != null) {
			return CalculoImpostoRenda.calculoIRPJ(contaPagar.getValor(), tabela.getAliquotaIR());
		} else {
			return BigDecimal.ZERO;
		}

	}

	private BigDecimal setarAFaixaRetencaoIRRF(BigDecimal valorBase) {
		List<TabelaIRPF> faixas = tabelaIRPFRepository.findByValorFinalGreaterThanEqual(valorBase);
		TabelaIRPF faixa = new TabelaIRPF();

		for (TabelaIRPF f : faixas) {
			if (f.getValorInicial().compareTo(valorBase) == 0 || f.getValorInicial().compareTo(valorBase) == -1) {
				faixa = f;
			}
		}

		if (faixa.getId() != null) {
			return CalculoImpostoRenda.calculoIRRF(valorBase, faixa.getAliquota(), faixa.getDeducao());
		} else {
			return BigDecimal.ZERO;
		}
	}

	public Imposto findOne(Long id) {
		return repository.findOne(id);
	}

	public Page<Imposto> filtrar(ImpostoFilter impostoFilter, Pageable pageable) {
		return repository.filtrar(impostoFilter, pageable);
	}

	@Transactional
	public void gerar(Imposto imposto) {
		
		
		if(imposto.getMulta() == null) {
			imposto.setMulta(BigDecimal.ZERO);
		}
		if(imposto.getJuros() == null) {
			imposto.setJuros(BigDecimal.ZERO);
		}
		imposto.setStatus("GERADO");
		imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
		imposto.setContaPagarGerada(contaPagarRepository.save(criarContaPagarGerada(imposto)));
		verificaDataVencimento(imposto);
		repository.save(imposto);
	}

	private void verificaDataVencimento(Imposto imposto) {
		if(imposto.getVencimento().isBefore(imposto.getApuracao())) {
			throw new PagamentoNaoEfetuadoException("A data do vencimento é menor que a data de apuração");
		}
	}

	private ContaPagar criarContaPagarGerada(Imposto imposto) {	
		try {	
			ContaPagar conta = new ContaPagar();	
			conta.setDataEmissao(imposto.getApuracao());	
			conta.setDocumento(imposto.getNome() +"-" + imposto.getContaPagar().getFornecedor().getSigla()+ "-"+imposto.getId());	
			conta.setEmpresa(imposto.getContaPagar().getEmpresa());	
			conta.setHistorico(imposto.getNome() +" RETIDO " + imposto.getContaPagar().getFornecedor().getNome()+ "- DOC nº: "+imposto.getNumeroNF());	
			conta.setNotaFiscal(imposto.getNumeroNF());	
			conta.setParcela(1);	
			conta.setStatus("ABERTO");	
			conta.setTotalParcela(1);	
			conta.setValor(imposto.getTotal());	
			conta.setVencimento(imposto.getVencimento());	
			conta.setRecibo(true);
			conta.setTemNota(true);
			conta.setFornecedor(inserirFornecedor(imposto.getNome()));	
			conta.setPlanoContaSecundaria(inserirPlanoContaSecundaria(imposto.getNome()));	
			return conta;	
		} catch (Exception e) {	
			throw new PagamentoNaoEfetuadoException("Não foi possível incluir a conta a pagar do imposto selecionado");	
		}	
	}

	private Fornecedor inserirFornecedor(String nome) {
		Fornecedor forn = new Fornecedor();
		if (nome.equals("IRRF") || nome.equals("PCCS")) {
			forn.setId(54L); // RECEITA FEDERAL
		} else if (nome.equals("INSS")) {
			forn.setId(55L); // PREVIDENCIA SOCIAL
		} else {
			forn.setId(4L); // PREFEITURA DE IGUATU
		}
		return forn;
	}

	private PlanoContaSecundaria inserirPlanoContaSecundaria(String nome) {
		PlanoContaSecundaria planoConta = new PlanoContaSecundaria();
		if (nome.equals("IRRF")) {
			planoConta.setId(58L); // IRRF RETIDO NOTAS PJ
		} else if (nome.equals("PIS/COFINS/CSLL")) {
			planoConta.setId(59L); // COFINS RETIDO NOTAS PJ
		} else if (nome.equals("INSS")) {
			planoConta.setId(55L); // PREVIDENCIA SOCIAL
		} else {
			planoConta.setId(58L); //
		}
		return planoConta;
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade(
					"Não foi possivel excluir o imposto. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!");
		}

	}
	
	
	@Transactional	
	public void cancelarGerar(Imposto imposto) {	
		try {	
			ContaPagar conta = contaPagarRepository.findOne(imposto.getContaPagarGerada().getId());	
			if(conta.getStatus().equals("ABERTO")) {	
				imposto.setContaPagarGerada(null);	
				contaPagarRepository.delete(conta);	
				imposto.setJuros(BigDecimal.ZERO);	
				imposto.setMulta(BigDecimal.ZERO);	
				imposto.setTotal(imposto.getValor());	
				imposto.setStatus("ABERTO");	
				repository.save(imposto);	
			}	
		} catch (Exception e) {	
			throw new ImpossivelExcluirEntidade("Não foi possivel cancelar o imposto gerado!!!"); 	
		}	
	}

	public List<Imposto> findByContaPagar(ContaPagar contaPagar) {
		return repository.findByContaPagar(contaPagar);
	}





}
