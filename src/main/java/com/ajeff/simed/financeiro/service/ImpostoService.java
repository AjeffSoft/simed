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
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.ImpostosRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
import com.ajeff.simed.financeiro.service.calculos.CalculoINSS;
import com.ajeff.simed.financeiro.service.calculos.CalculoImpostoRenda;
import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;
import com.ajeff.simed.util.CalculosComDatas;

@Service
public class ImpostoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ImpostoService.class);	
	
	@Autowired
	private ImpostosRepository repository;
	@Autowired
	private ContaPagarService contaPagarService;
	@Autowired
	private ContasPagarRepository contasPagarRepository;
	@Autowired
	private TabelasIrpfRepository tabelaIRPFRepository;
	@Autowired
	private TabelasIrpjRepository tabelaIRPJRepository;
	
	//TODO: Disponibilizar valor desconto dependente em banco de dados
	private static final BigDecimal DEP_VALOR = new BigDecimal(189.90);
	private static final BigDecimal TETO_INSS = new BigDecimal(6000.00);
	
	//TODO: Criar tabela para calculos INSS (Pessoa Fisica e futuros funcionários)
	private static final BigDecimal ALIQUOTA_INSS = new BigDecimal(11);
	
	
	
	public BigDecimal retencaoImpostos(ContaPagar contaPagar) {
		String tipoImposto = new String();
		List<Imposto> impostos = new ArrayList<>();
		
		if (contaPagar.getReterINSS()) {
			tipoImposto = "INSS";
			impostos.add(imposto(contaPagar, tipoImposto));
		}

		if (contaPagar.getReterIR()) {
			tipoImposto = "IRRF";
			impostos.add(imposto(contaPagar, tipoImposto));
		}
		
		
		if (contaPagar.getReterCOFINS()) {
			tipoImposto = "PCCS";
			impostos.add(imposto(contaPagar, tipoImposto));
		}
		
		if (!contaPagar.getIssPorcentagem().equals(null)) {
			tipoImposto = "ISS";
			impostos.add(imposto(contaPagar, tipoImposto));
		}
		
		
		return impostos.stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		
		
//		if(contaPagar.getReterIR()) {
//			tipoImposto = "IRRF";
//			BigDecimal vlImposto = contaPagar.getValor();
//			vlImposto = CalculosImpostoIR.descontoDeRetencaoDependentes(vlImposto, contaPagar.getFornecedor().getDependente(), DEP_VALOR);
//			
//			if(contaPagar.getReterINSS()) {
//				vlImposto = CalculoINSS.calculoINSS(vlImposto, TETO_INSS,  ALIQUOTA_INSS);
//			}
			
			
//			setarFaixaImpostoRenda(vlImposto, contaPagar);
			
//			impostos.add(imposto(contaPagar, tipoImposto));
//			System.out.println(">>>>>>>>>>>>>>" + vlImposto);
//		}
	}

		
//		private void setarFaixaImpostoRenda(BigDecimal vlImposto, ContaPagar contaPagar) {
//			TabelaIR tabela = tabelaRepository.findByValorInicialLessThanEqualAndValorFinalGreaterThanEqual(vlImposto);
//			
//		}
//
//		return impostos;
//	}
//	
//
//
	private Imposto imposto(ContaPagar contaPagar, String tipoImposto) {
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
		calcularImpostoPorTipo(contaPagar, tipoImposto, imp);
		return imp;
	}


	private void calcularImpostoPorTipo(ContaPagar contaPagar, String tipoImposto, Imposto imp) {
		
		if(tipoImposto.equals("INSS")) {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 15));
			imp.setValor(CalculoINSS.calculoINSS(contaPagar.getValor(), TETO_INSS, ALIQUOTA_INSS));
		
		}else if(tipoImposto.equals("IRRF")) {
			BigDecimal valorBase = contaPagar.getValor();
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 20));

			if(contaPagar.getFornecedor().getTipo().equals("F")) {
				imp.setValor(regraRetencaoIRRFPessoaFisica(contaPagar, valorBase));
			}else {
				imp.setValor(regraRetencaoIRRFPessoaJuridica(contaPagar));
			}
		}else if(tipoImposto.equals("PCCS")) {
			imp.setVencimento(setarVencimentoSomarDiasEDiaUtil(imp.getApuracao(), 20));
			TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);
			imp.setValor(CalculoImpostoRenda.calculoPCCS(contaPagar.getValor(), tabela.getAliquotaPIS(), tabela.getAliquotaCOFINS(), tabela.getAliquotaCOFINS()));
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
	

	private BigDecimal regraRetencaoPCCSPessoaJuridica(ContaPagar contaPagar) {
		TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1L);

		if(tabela.getId() != null) {
			return CalculoImpostoRenda.calculoPCCS(contaPagar.getValor(), tabela.getAliquotaPIS(), tabela.getAliquotaCOFINS(), tabela.getAliquotaCSLL());
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


//	private void setarFaixaImpostoRenda(ContaPagar contaPagar, Imposto imp) {
//		TabelaIR tabela = tabelaRepository.findByValorInicialLessThanEqualAndValorFinalGreaterThanEqual(contaPagar.getValor());
//	}
//
//
	private LocalDate setarVencimentoSomarDiasEDiaUtil(LocalDate dataBase, Integer dias) {
		LocalDate data = (CalculosComDatas.sumDays(dataBase, dias));
		if (CalculosComDatas.dataFinalSemana(data)) {
			return CalculosComDatas.dataDomingo(data) ? data.minusDays(2) : data.minusDays(1);
		}else {
			return data;
		}
	}
	
	
	private void verificarSeDataEUtil(LocalDate data) {
		if(CalculosComDatas.dataFinalSemana(data)) {
			if(CalculosComDatas.dataSabado(data)) {
				data.minusDays(1);
			}else {
				data.minusDays(2);
			}
		}
	}

//
//	private void calculoParaISS(ContaPagar contaPagar, Imposto imposto) {
//		imposto.setValor(CalculosImpostos.calculoISS(contaPagar.getValor(), contaPagar.getIssPorcentagem()));
//		imposto.setCodigo("ISS");
//		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 10));
//	}
//	
//	private void calculoParaINSS(ContaPagar contaPagar, Imposto imposto) {
//		imposto.setValor(CalculosImpostos.calculoINSS(contaPagar.getValor()));
//		imposto.setCodigo("INSS");
//		imposto.setNome("INSS");
//		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 15));
//	}
//	
//	private void calculoParaPCCS(ContaPagar contaPagar, Imposto imposto) {
//		imposto.setValor(CalculosImpostos.calculoPCCS(contaPagar.getValor()));
//		imposto.setCodigo("5952");
//		imposto.setNome("PIS/COFINS/CSLL");
//		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 20));
//	}
//
//	private void calculoParaImpostoRenda(ContaPagar contaPagar, Imposto imposto) {
//		imposto.setNome("IRRF");
//		if(contaPagar.getFornecedor().getTipo().equals("J")) {
//			imposto.setValor(CalculosImpostos.calculoIRJuridico(contaPagar.getValor()));
//			imposto.setCodigo("1708");
//		}else {
//			imposto.setValor(CalculosImpostos.calculoIRFisica(contaPagar.getValor(), contaPagar.getFornecedor().getDependente(), contaPagar.getReterINSS()));
//			imposto.setCodigo("0588");
//		}
//	}
//	
//	private Imposto impostoISS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
//		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
////		conta.setValorIss(imposto.getValor());
//		conta.setReterISS(true);
//		return imposto;
//	}
//	
//
//
//	public Imposto findOne(Long id) {
//		return repository.findOne(id);
//	}
//	
//	@Transactional
//	public void gerar(Imposto imposto) {
//		try {
//			imposto.setStatus("GERADO");
//			imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
//			imposto.setContaPagarGerada(contasPagarRepository.save(criarRegistroContaPagar(imposto)));
//			repository.save(imposto);
//		} catch (Exception e) {
//			throw new PagamentoNaoEfetuadoException("Algo deu errado! Imposto não gerado!");
//		}
//	}
//
//	
//	private ContaPagar criarRegistroContaPagar(Imposto imposto) {
//		try {
//			ContaPagar contaOrigem = contaPagarService.findOne(imposto.getContaPagarOrigem().getId());
//			ContaPagar conta = new ContaPagar();
//			conta.setDataEmissao(imposto.getApuracao());
//			conta.setDocumento(imposto.getCodigo() +"-" + contaOrigem.getFornecedor().getSigla()+ "-"+imposto.getId());
//			conta.setEmpresa(contaOrigem.getEmpresa());
//			conta.setHistorico(imposto.getNome() +" RETIDO " + contaOrigem.getFornecedor().getNome()+ "- DOC nº: "+imposto.getNumeroNF());
//			conta.setNotaFiscal("IMP:"+imposto.getNumeroNF());
//			conta.setParcela(1);
//			conta.setStatus("ABERTO");
//			conta.setTotalParcela(1);
//			conta.setValor(imposto.getTotal());
//			conta.setVencimento(imposto.getVencimento());
//			conta.setFornecedor(inserirFornecedor(imposto.getNome()));
//			conta.setPlanoContaSecundaria(inserirPlanoContaSecundaria(imposto.getNome()));
//			conta.setPrazoParcelamento(0);
//			return conta;
//		} catch (Exception e) {
//			throw new PagamentoNaoEfetuadoException("Não foi possível incluir a conta a pagar do imposto selecionado");
//		}
//	}
//		
//	private Fornecedor inserirFornecedor(String nome) {
//		Fornecedor forn = new Fornecedor();
//		if(nome.equals("IRRF") || nome.equals("PIS/COFINS/CSLL")) {
//			forn.setId(54L); //RECEITA FEDERAL
//		}else if(nome.equals("INSS")) {
//			forn.setId(55L); //PREVIDENCIA SOCIAL
//		}else {
//			forn.setId(4L); //PREFEITURA DE IGUATU
//		}
//		return forn;
//	}	
//
//	private PlanoContaSecundaria inserirPlanoContaSecundaria(String nome) {
//		PlanoContaSecundaria planoConta = new PlanoContaSecundaria();
//		if(nome.equals("IRRF")) {
//			planoConta.setId(58L); //IRRF RETIDO NOTAS PJ
//		}else if(nome.equals("PIS/COFINS/CSLL")) {
//			planoConta.setId(59L); //COFINS RETIDO NOTAS PJ
//		}else if(nome.equals("INSS")) {
//			planoConta.setId(55L); //PREVIDENCIA SOCIAL
//		}else {
//			planoConta.setId(58L); //
//		}
//		return planoConta;
//	}
//	
//	@Transactional
//	public void cancelarGerar(Imposto imposto) {
//		try {
//			ContaPagar conta = contaPagarService.findOne(imposto.getContaPagarGerada().getId());
//			if(conta.getStatus().equals("ABERTO")) {
//				imposto.setContaPagarGerada(null);
//				contasPagarRepository.delete(conta);
//				imposto.setJuros(BigDecimal.ZERO);
//				imposto.setMulta(BigDecimal.ZERO);
//				imposto.setTotal(imposto.getValor());
//				imposto.setStatus("ABERTO");
//				repository.save(imposto);
//			}
//		} catch (Exception e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel cancelar o imposto gerado!!!"); 
//		}
//	}	
//	
//	@Transactional
//	public void excluir(Long id) {
//		try {
//			repository.delete(id);
//			repository.flush();
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o imposto. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//		}
//		
//	}	
//	
//	public Page<Imposto> filtrar(ImpostoFilter impostoFilter, Pageable pageable) {
//		return repository.filtrar(impostoFilter, pageable);
//	}
//
//
//	public List<Imposto> findByContaPagarOrigemFornecedorOrderByVencimento(Fornecedor fornecedor) {
//		return repository.findByContaPagarOrigemFornecedorOrderByVencimento(fornecedor);
//	}	


	
	
	
////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	
//	private Imposto retencaoINSS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
//		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
////		conta.setValorInss(imposto.getValor());
//		conta.setReterINSS(true);
//		return imposto;
//	}
//
//	private Imposto impostoPCCS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
//		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
////		conta.setValorCofins(imposto.getValor());
//		conta.setReterCOFINS//	@Transactional
//	public void gerar(Imposto imposto) {
//	
//	imposto.setStatus("GERADO");
//	if(imposto.getMulta() == null) {
//		imposto.setMulta(BigDecimal.ZERO);
//	}
//	if(imposto.getJuros() == null) {
//		imposto.setJuros(BigDecimal.ZERO);
//	}
//
//	try {
//		imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
//		if(imposto.getNome().equals("IRRF")  || imposto.getCodigo().equals("PIS/COFINS/CSLL")) {
//			imposto.setGerarDarf(true);
//		}
//		contasPagarRepository.save(criarRegistroContaPagar(imposto));
//		repository.save(imposto);
//	} catch (Exception e) {
//		throw new PagamentoNaoEfetuadoException("Algo deu errado! Imposto não gerado!");
//	}
//}
//(true);
//		return imposto;
//	}	
	

}
