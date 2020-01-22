package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.ContasReceberRepository;
import com.ajeff.simed.financeiro.repository.PlanosContaSecundariaRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaReceberFilter;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class ContaReceberService {

	private static final Logger LOG = LoggerFactory.getLogger(ContaReceberService.class);
	
	@Autowired
	private ContasReceberRepository repository;
	@Autowired
	private PlanosContaSecundariaRepository planoContarepository;
//	@Autowired
//	private ExtratosRepository extratoRepository;
	
	
	@Transactional
	public void salvar(ContaReceber contaReceber) {

		try {
			if (contaReceber.isNovo()) {
				if(contaReceber.getTotalParcela() == null || contaReceber.getTotalParcela() <= 1) {
					testeVencimentoMaiorEmissao(contaReceber);
					semParcelamento	(contaReceber);
					
				}else {
					testeVencimentoMaiorEmissao(contaReceber);
					parcelamento(contaReceber);
				}
			}else {
				testeRegistroJaCadastrado(contaReceber);
				testeVencimentoMaiorEmissao(contaReceber);
				repository.save(contaReceber);
			}
		} catch (RegistroNaoCadastradoException e) {
			LOG.error("Erro ao cadastrar uma conta a receber - " + e.getMessage());
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
	}


	
	private void semParcelamento(ContaReceber contaReceber) {
		try {
			if(contaReceber.getHistorico().isEmpty()) {
				historicoVazioIgualContaSecundaria(contaReceber);
			}
			cadastrarDocumento(contaReceber);
			contaReceber.setStatus("ABERTO");
			contaReceber.setValorRecebido(BigDecimal.ZERO);
			contaReceber.setTotalParcela(1);
			contaReceber.setParcela(1);
			testeRegistroJaCadastrado(contaReceber);
			repository.save(contaReceber);
		} catch (RegistroNaoCadastradoException e) {
			LOG.error("Erro ao salvar a conta a receber sem parcelamento - ", e.getMessage());
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
	}	

	
	
	private void historicoVazioIgualContaSecundaria(ContaReceber contaReceber) {
		PlanoContaSecundaria planoConta = planoContarepository.findOne(contaReceber.getPlanoContaSecundaria().getId());
		contaReceber.setHistorico(planoConta.getNome());
	}	

	
	
	private void cadastrarDocumento(ContaReceber contaReceber) {
		if (contaReceber.getDocumento().isEmpty()) {
			Random r = new Random();
			int aleatorio = r.nextInt(10000);
			contaReceber.setDocumento(contaReceber.getFornecedor().getSigla() + "-" + aleatorio);
		}
	}

	

	private void parcelamento(ContaReceber contaReceber) {
		
		List<ContaReceber> contasParaParcelamento = new ArrayList<>();

		try {
			for(int i = 1; i <= contaReceber.getTotalParcela(); i++) {
				ContaReceber cp = new ContaReceber();
				cp.setDataEmissao(contaReceber.getDataEmissao());
				cp.setVencimento(contaReceber.getVencimento().plusMonths(i).minusMonths(1));
				cp.setEmpresa(contaReceber.getEmpresa());
				cp.setStatus("ABERTO");
				cp.setAnexo(contaReceber.getAnexo());
				cp.setParcela(i);
				cp.setTotalParcela(contaReceber.getTotalParcela());
				cp.setTipoDocumento(contaReceber.getTipoDocumento());
				cp.setPlanoContaSecundaria(contaReceber.getPlanoContaSecundaria());
				cp.setFornecedor(contaReceber.getFornecedor());
				cp.setValor(contaReceber.getValor().divide(new BigDecimal(contaReceber.getTotalParcela()),
						MathContext.DECIMAL32));
				cp.setValorRecebido(BigDecimal.ZERO);
				
				//Caso o historico esteja nulo 
				if(contaReceber.getHistorico().isEmpty() && contaReceber.getPlanoContaSecundaria().getId() != null) {
					historicoVazioIgualContaSecundaria(cp);
				}else {
					cp.setHistorico(contaReceber.getHistorico());
				}
				
				
				//Caso o documento esteja nulo	
				if (contaReceber.getDocumento().isEmpty()) {
					cp.setDocumento(contaReceber.getFornecedor().getSigla() + "." + contaReceber.getVencimento().plusMonths(i).minusMonths(1).getMonthValue()+ "/"+
							contaReceber.getVencimento().getYear() + "-" + i);
					}else {
						cp.setDocumento(contaReceber.getDocumento()+ "-" + i);
					}
				
				contasParaParcelamento.add(cp);
			}
		} catch (RegistroNaoCadastradoException e) {
			LOG.error("Erro ao salvar a conta a receber parcelada - ", e.getMessage());
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}

		for(ContaReceber p : contasParaParcelamento ) {
			testeRegistroJaCadastrado(p);
		}
		repository.save(contasParaParcelamento);
	}	
	
	
	
	//BUSCAS DE CONTAS A RECEBER
	public Page<ContaReceber> filtrar(ContaReceberFilter contaReceberFilter, Pageable pageable) {
		return repository.filtrar(contaReceberFilter, pageable);
	}
	public ContaReceber findOne(Long id) {
		return repository.findOne(id);
	}
	public ContaReceber buscarComPlanoConta(Long id) {
		return repository.buscarComPlanoConta(id);
	}
	
	
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "a conta";
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}		

	

	//REGRA PARA EVITAR DATA EMISSÃO MAIOR QUE VENCIMENTO
	private void testeVencimentoMaiorEmissao(ContaReceber contaReceber) {
		if(contaReceber.getDataEmissao() != null) {
			if(!contaReceber.isVencimentoMaiorEmissao()) {
				throw new VencimentoMenorEmissaoException("A data de vencimento não pode ser menor que a emissão");
			}
		}
	}
	
	//TESTE DE REGISTRO JÁ CADASTRADO
	private void testeRegistroJaCadastrado(ContaReceber contaReceber) {
		Optional<ContaReceber> optional = repository.findByDocumentoAndFornecedor(contaReceber.getDocumento(), contaReceber.getFornecedor());
		if (optional.isPresent() && !optional.get().equals(contaReceber)) {
			throw new DocumentoEFornecedorJaCadastradoException("Já existe uma conta com este documento e cliente cadastrado!");
		}
	}


	public BigDecimal totalGeral(ContaReceberFilter contaReceberFilter) {
		return repository.totalGeral(contaReceberFilter);
	}



	public List<ContaReceber> findByFornecedor(Fornecedor cliente) {
		return repository.findByFornecedorOrderByVencimento(cliente);
	}

}
