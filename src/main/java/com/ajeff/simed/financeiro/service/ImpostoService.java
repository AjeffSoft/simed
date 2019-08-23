package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;

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

@Service
public class ImpostoService {

	private static final Logger LOG = LoggerFactory.getLogger(ImpostoService.class);	
	
	@Autowired
	private ImpostosRepository repository;
	@Autowired
	private ContaPagarService contaPagarService;
	@Autowired
	private ContasPagarRepository contasPagarRepository;
	
	
	@Transactional
	public void gerar(Imposto imposto) {
		
		imposto.setStatus("GERADO");
		if(imposto.getMulta() == null) {
			imposto.setMulta(BigDecimal.ZERO);
		}
		if(imposto.getJuros() == null) {
			imposto.setJuros(BigDecimal.ZERO);
		}

		try {
			imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
			if(imposto.getNome().equals("IRRF")  || imposto.getCodigo().equals("PIS/COFINS/CSLL")) {
				imposto.setGerarDarf(true);
			}
			contasPagarRepository.save(criarRegistroContaPagar(imposto));
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
			conta.setEmpresa(imposto.getEmpresa());
			conta.setHistorico(imposto.getNome() +" RETIDO " + contaOrigem.getFornecedor().getFantasia()+ "- DOC nº: "+imposto.getNumeroNF());
			conta.setNotaFiscal(imposto.getNumeroNF()+" :IMP");
			conta.setParcela(1);
			conta.setStatus("ABERTO");
			conta.setTotalParcela(1);
			conta.setValor(imposto.getTotal());
			conta.setVencimento(imposto.getVencimento());
			conta.setFornecedor(inserindoFornecedor(imposto.getNome()));
			conta.setPlanoContaSecundaria(inserindoPlanoContaSecundaria(imposto.getNome()));
			conta.setPrazoParcelamento(0);
			conta.setReterCOFINS(false);
			conta.setReterINSS(false);
			conta.setReterIR(false);
			conta.setReterISS(false);
			conta.setImpostoGerado(imposto);
			return conta;
		} catch (Exception e) {
			LOG.error("Erro ao criar o registro conta a pagar!--" + e.getMessage());
			return null;
		}
	}


	private PlanoContaSecundaria inserindoPlanoContaSecundaria(String nome) {
		PlanoContaSecundaria planoConta = new PlanoContaSecundaria();
		try {
			if(nome.equals("IRRF")) {
				planoConta.setId(58L); //IRRF RETIDO NOTAS PJ
			}else if(nome.equals("PIS/COFINS/CSLL")) {
				planoConta.setId(59L); //COFINS RETIDO NOTAS PJ
			}else if(nome.equals("INSS")) {
				planoConta.setId(55L); //PREVIDENCIA SOCIAL
			}else {
				planoConta.setId(58L); //
			}
		} catch (Exception e) {
			LOG.error("Erro ao setar o plano de conta para o imposto gerado");
		}
		return planoConta;
	}


	private Fornecedor inserindoFornecedor(String nome) {
		Fornecedor forn = new Fornecedor();
		try {
			if(nome.equals("IRRF") || nome.equals("PIS/COFINS/CSLL")) {
				forn.setId(54L); //RECEITA FEDERAL
			}else if(nome.equals("INSS")) {
				forn.setId(55L); //PREVIDENCIA SOCIAL
			}else {
				forn.setId(4L); //PREFEITURA DE IGUATU
			}
		} catch (Exception e) {
			LOG.error("Erro ao setar o fornecedor para o imposto gerado");
		}
		return forn;
	}


	@Transactional
	public void cancelarGerar(Imposto imposto) {
		
		ContaPagar conta = contaPagarService.findByImpostoGerado(imposto);
		
		//Imposto copiaImposto = copiaImposto(imposto);
		
		if(conta.getStatus().equals("ABERTO")) {
			contasPagarRepository.delete(conta);
			//repository.save(copiaImposto);
			//repository.delete(imposto);
			imposto.setJuros(BigDecimal.ZERO);
			imposto.setMulta(BigDecimal.ZERO);
			imposto.setTotal(imposto.getValor());
			imposto.setStatus("ABERTO");
			imposto.setGerarDarf(false);
			repository.save(imposto);
		}else {
			LOG.error("Conta a Pagar Destino com Status diferente de ABERTO");
		}
	}
	

	public Page<Imposto> filtrar(ImpostoFilter impostoFilter, Pageable pageable) {
		return repository.filtrar(impostoFilter, pageable);
	}

	@Transactional
	public void excluir(Long id) {
		String tipo = "o imposto";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}

	public Imposto findOne(Long id) {
		return repository.findOne(id);
	}
}
