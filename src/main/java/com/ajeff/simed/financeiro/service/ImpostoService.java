package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.ImpostosRepository;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.impostos.CalculosImpostos;

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

	
	public List<Imposto> retencaoImpostos(ContaPagar contaPagar, ContaPagar conta) {
		String tipoImposto = new String();
		List<Imposto> impostos = new ArrayList<>();
		
		if(contaPagar.getReterIR()) {
			tipoImposto = "IR";
			impostos.add(impostoRegistroUnico(contaPagar, conta, tipoImposto));
		}	

		if(contaPagar.getReterCOFINS() && contaPagar.getFornecedor().getTipo().equals("J")) {
			tipoImposto = "PCCS";
			impostos.add(impostoRegistroUnico(contaPagar, conta, tipoImposto));
		}	
		
		if(contaPagar.getReterINSS() && contaPagar.getFornecedor().getTipo().equals("F")) {
			tipoImposto = "INSS";
			impostos.add(impostoRegistroUnico(contaPagar, conta, tipoImposto));
		}	
		
		if(contaPagar.getReterISS()) {
			tipoImposto = "ISS";
			impostos.add(impostoISS(contaPagar, conta, tipoImposto));
		}
		return impostos;
	}
	
	
	private Imposto impostoRegistroUnico(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = new Imposto();
		imposto.setApuracao(contaPagar.getDataEmissao().minusMonths(0).with(TemporalAdjusters.lastDayOfMonth()));
		imposto.setContaPagarOrigem(conta);
		imposto.setJuros(BigDecimal.ZERO);
		imposto.setMulta(BigDecimal.ZERO);
		imposto.setValorNF(contaPagar.getValor());
		imposto.setNumeroNF(contaPagar.getNotaFiscal());
		imposto.setStatus("ABERTO");
		imposto.setEmissaoNF(contaPagar.getDataEmissao());
//		imposto.setEmpresa(contaPagar.getEmpresa());
		imposto.setUpload(contaPagar.getUpload());
		imposto.setContentType(contaPagar.getContentType());
				
		if(tipoImposto.equals("IR")) {
			calculoParaImpostoRenda(contaPagar, imposto);
		}else if(tipoImposto.equals("PCCS") && contaPagar.getFornecedor().getTipo().equals("J")) {
			calculoParaPCCS(contaPagar, imposto);
		}else if(tipoImposto.equals("INSS")) {
			calculoParaINSS(contaPagar, imposto);
		}else {
			calculoParaISS(contaPagar, imposto);
		}
		imposto.setHistorico(imposto.getNome() + " - " + conta.getFornecedor().getFantasia() + " - " + imposto.getNumeroNF());
		imposto.setTotal(imposto.getValor());
		return imposto;
	}
	
	private void calculoParaISS(ContaPagar contaPagar, Imposto imposto) {
		imposto.setValor(CalculosImpostos.calculoISS(contaPagar.getValor(), contaPagar.getIssPorcentagem()));
		imposto.setCodigo("ISS");
		imposto.setNome("ISS");
		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 10));
	}
	
	private void calculoParaINSS(ContaPagar contaPagar, Imposto imposto) {
		imposto.setValor(CalculosImpostos.calculoINSS(contaPagar.getValor()));
		imposto.setCodigo("INSS");
		imposto.setNome("INSS");
		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 15));
	}
	
	private void calculoParaPCCS(ContaPagar contaPagar, Imposto imposto) {
		imposto.setValor(CalculosImpostos.calculoPCCS(contaPagar.getValor()));
		imposto.setCodigo("5952");
		imposto.setNome("PIS/COFINS/CSLL");
		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 20));
	}

	private void calculoParaImpostoRenda(ContaPagar contaPagar, Imposto imposto) {
		imposto.setVencimento(contaPagarService.dataUtil(imposto.getApuracao(), 20));
		imposto.setNome("IRRF");
		if(contaPagar.getFornecedor().getTipo().equals("J")) {
			imposto.setValor(CalculosImpostos.calculoIRJuridico(contaPagar.getValor()));
			imposto.setCodigo("1708");
		}else {
			imposto.setValor(CalculosImpostos.calculoIRFisica(contaPagar.getValor(), contaPagar.getFornecedor().getDependente(), contaPagar.getReterINSS()));
			imposto.setCodigo("0588");
		}
	}
	
	private Imposto impostoISS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
//		conta.setValorIss(imposto.getValor());
		conta.setReterISS(true);
		return imposto;
	}
	


	public Imposto findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void gerar(Imposto imposto) {
		try {
			imposto.setStatus("GERADO");
			imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
			imposto.setContaPagarGerada(contasPagarRepository.save(criarRegistroContaPagar(imposto)));
			repository.save(imposto);
		} catch (Exception e) {
			throw new PagamentoNaoEfetuadoException("Algo deu errado! Imposto não gerado!");
		}
	}

	
	private ContaPagar criarRegistroContaPagar(Imposto imposto) {
		try {
			ContaPagar contaOrigem = contaPagarService.findOne(imposto.getContaPagarOrigem().getId());
			ContaPagar conta = new ContaPagar();
			conta.setDataEmissao(imposto.getApuracao());
			conta.setDocumento(imposto.getCodigo() +"-" + contaOrigem.getFornecedor().getSigla()+ "-"+imposto.getId());
			conta.setEmpresa(contaOrigem.getEmpresa());
			conta.setHistorico(imposto.getNome() +" RETIDO " + contaOrigem.getFornecedor().getNome()+ "- DOC nº: "+imposto.getNumeroNF());
			conta.setNotaFiscal("IMP:"+imposto.getNumeroNF());
			conta.setParcela(1);
			conta.setStatus("ABERTO");
			conta.setTotalParcela(1);
			conta.setValor(imposto.getTotal());
			conta.setVencimento(imposto.getVencimento());
			conta.setFornecedor(inserirFornecedor(imposto.getNome()));
			conta.setPlanoContaSecundaria(inserirPlanoContaSecundaria(imposto.getNome()));
			conta.setPrazoParcelamento(0);
			return conta;
		} catch (Exception e) {
			throw new PagamentoNaoEfetuadoException("Não foi possível incluir a conta a pagar do imposto selecionado");
		}
	}
		
	private Fornecedor inserirFornecedor(String nome) {
		Fornecedor forn = new Fornecedor();
		if(nome.equals("IRRF") || nome.equals("PIS/COFINS/CSLL")) {
			forn.setId(54L); //RECEITA FEDERAL
		}else if(nome.equals("INSS")) {
			forn.setId(55L); //PREVIDENCIA SOCIAL
		}else {
			forn.setId(4L); //PREFEITURA DE IGUATU
		}
		return forn;
	}	

	private PlanoContaSecundaria inserirPlanoContaSecundaria(String nome) {
		PlanoContaSecundaria planoConta = new PlanoContaSecundaria();
		if(nome.equals("IRRF")) {
			planoConta.setId(58L); //IRRF RETIDO NOTAS PJ
		}else if(nome.equals("PIS/COFINS/CSLL")) {
			planoConta.setId(59L); //COFINS RETIDO NOTAS PJ
		}else if(nome.equals("INSS")) {
			planoConta.setId(55L); //PREVIDENCIA SOCIAL
		}else {
			planoConta.setId(58L); //
		}
		return planoConta;
	}
	
	@Transactional
	public void cancelarGerar(Imposto imposto) {
		try {
			ContaPagar conta = contaPagarService.findOne(imposto.getContaPagarGerada().getId());
			if(conta.getStatus().equals("ABERTO")) {
				imposto.setContaPagarGerada(null);
				contasPagarRepository.delete(conta);
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
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o imposto. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	
	
	public Page<Imposto> filtrar(ImpostoFilter impostoFilter, Pageable pageable) {
		return repository.filtrar(impostoFilter, pageable);
	}	

	
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
