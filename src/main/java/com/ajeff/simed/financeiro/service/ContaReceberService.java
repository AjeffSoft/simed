package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.hibernate.TransientObjectException;
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

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ContaReceberService.class);
	
	@Autowired
	private ContasReceberRepository repository;
	@Autowired
	private PlanosContaSecundariaRepository planoContarepository;
	
	
	@Transactional
	public void salvar(ContaReceber contaReceber) {
		try {
			if (contaReceber.isNovo()) {
				List<ContaReceber> contas = gerarContasReceber(contaReceber);
				repository.save(contas);
			}else {
				testeVencimentoMaiorEmissao(contaReceber);
				testeRegistroJaCadastrado(contaReceber);
				repository.save(contaReceber);
			}
		} catch (RegistroNaoCadastradoException e) {
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
	}


	private List<ContaReceber> gerarContasReceber(ContaReceber contaReceber) {
		List<ContaReceber> contas = new ArrayList<>();
		
		if (contaReceber.getTotalParcela() == null || contaReceber.getTotalParcela() <=0) {
			contaReceber.setTotalParcela(1);
		}
		
		try {
			for(int i = 1; i <= contaReceber.getTotalParcela(); i++) {
		
				ContaReceber cr = new ContaReceber();
				cr.setDataEmissao(contaReceber.getDataEmissao());
				cr.setVencimento(contaReceber.getVencimento().plusMonths(i).minusMonths(1));
				cr.setEmpresa(contaReceber.getEmpresa());
				cr.setStatus("ABERTO");
				cr.setDocumento(contaReceber.getFornecedor().getSigla() + contaReceber.getDocumento() +"-"+i);
				cr.setParcela(i);
				cr.setValorRecebido(BigDecimal.ZERO);
				cr.setTotalParcela(contaReceber.getTotalParcela());
				cr.setPlanoContaSecundaria(contaReceber.getPlanoContaSecundaria());
				cr.setFornecedor(contaReceber.getFornecedor());
				cr.setValor(contaReceber.getValor().divide(new BigDecimal(contaReceber.getTotalParcela()),
						MathContext.DECIMAL32));		
				setarHistoricoDaContaReceber(contaReceber, cr);
				testeVencimentoMaiorEmissao(cr);
				testeRegistroJaCadastrado(cr);
				contas.add(cr);
			}
		} catch (RegistroNaoCadastradoException e) {
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
		
		return contas;
	}


	private void setarHistoricoDaContaReceber(ContaReceber contaReceber, ContaReceber cp) {
		if(contaReceber.getHistorico().isEmpty() && contaReceber.getPlanoContaSecundaria().getId() != null) {
			historicoVazioIgualContaSecundaria(cp);
		}else {
			cp.setHistorico(contaReceber.getHistorico());
		}
	}

	
	private void historicoVazioIgualContaSecundaria(ContaReceber contaReceber) {
		PlanoContaSecundaria planoConta = planoContarepository.findOne(contaReceber.getPlanoContaSecundaria().getId());
		contaReceber.setHistorico(planoConta.getNome());
	}	

	
	
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
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a conta. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}		


	private void testeVencimentoMaiorEmissao(ContaReceber contaReceber) {
		if(contaReceber.getDataEmissao() != null) {
			if(!contaReceber.isVencimentoMaiorEmissao()) {
				throw new VencimentoMenorEmissaoException("A data de vencimento não pode ser menor que a emissão");
			}
		}
	}
	
	
	private void testeRegistroJaCadastrado(ContaReceber contaReceber) {
		if(contaReceber.getFornecedor().getId() == null) {
			throw new TransientObjectException ("O cliente não foi selecionado");
		}else {
			
			Optional<ContaReceber> optional = repository.findByDocumentoAndFornecedor(contaReceber.getDocumento(), contaReceber.getFornecedor());
			if (optional.isPresent() && !optional.get().equals(contaReceber)) {
				throw new DocumentoEFornecedorJaCadastradoException("Já existe uma conta com este documento e cliente cadastrado!");
			}
		}
	}


	public BigDecimal totalGeral(ContaReceberFilter contaReceberFilter) {
		return repository.totalGeral(contaReceberFilter);
	}


	public List<ContaReceber> findByFornecedor(Fornecedor cliente) {
		return repository.findByFornecedorOrderByVencimento(cliente);
	}


	public void alterarStatusESubtrairValorConta(Long id, String status, BigDecimal valor) {
		ContaReceber contaReceber = repository.findOne(id);
		contaReceber.setStatus(status);
		contaReceber.setValorRecebido(contaReceber.getValorRecebido().subtract(valor));
		repository.save(contaReceber);
	}
	
}
