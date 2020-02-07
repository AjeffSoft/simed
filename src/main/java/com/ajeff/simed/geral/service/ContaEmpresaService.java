package com.ajeff.simed.geral.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.ContaEmpresaRepository;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class ContaEmpresaService {

	@Autowired
	private ContaEmpresaRepository repository;
	

	@Transactional
	public ContaEmpresa salvar(ContaEmpresa contaEmpresa){
		
		testeRegistroJaCadastrado(contaEmpresa);
		testeNovoRegistro(contaEmpresa);
		
		return repository.save(contaEmpresa);
	}

	@Transactional
	public void excluir(Long id) {
		String tipo = "a conta bancária";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}

	public void creditarValorNoTotalPendencias(Long empresa, BigDecimal valor) {
		ContaEmpresa conta = repository.findOne(empresa);
		conta.setValorPendente(conta.getValorPendente().add(valor));
		repository.save(conta);
	}

	public void debitarValorNoTotalPendencias(Long empresa, BigDecimal valor) {
		ContaEmpresa conta = repository.findOne(empresa);
		conta.setValorPendente(conta.getValorPendente().subtract(valor));
		repository.save(conta);
	}
	
	
	public ContaEmpresa findOne(Long id) {
		return repository.findOne(id);
	}

	
	public List<ContaEmpresa> buscarContaCorrenteBanco() {
		return repository.findByEmpresaIdOrderByConta();
	}

	
	private void testeNovoRegistro(ContaEmpresa contaEmpresa) {
		if(contaEmpresa.isNovo()) {
			contaEmpresa.setSaldo(new BigDecimal(0));
			contaEmpresa.setValorPendente(new BigDecimal(0));
		}
	}
	
	
	private void testeRegistroJaCadastrado(ContaEmpresa contaEmpresa) {
		Optional<ContaEmpresa> contaOptional = repository.findByContaAndEmpresaAndTipo(contaEmpresa.getConta(), contaEmpresa.getEmpresa(), contaEmpresa.getTipo());
		
		if(contaOptional.isPresent() && !contaOptional.get().equals(contaEmpresa)){
			throw new RegistroJaCadastradoException("Já existe uma conta bancária com este tipo cadastrada para esta empresa.");
		}
	}

	public List<ContaEmpresa> buscarTodosPorEmpresa(Empresa empresa) {
		return repository.findByEmpresa(empresa);
	}


	public List<ContaEmpresa> findByEmpresaIdAtivo(Long id) {
		return repository.findByEmpresaIdAtivo(id);
	}


	public List<ContaEmpresa> listarTodosOrdenadoPorNome() {
		return repository.listarTodosOrdenadoPorNome();
	}

	public Object findByEmpresaId(Long idEmpresa) {
		return repository.findByEmpresaId(idEmpresa);
	}

	public void atualizarSaldoContaEmpresa(MovimentacaoItem mov) {
		ContaEmpresa contaEmpresa = repository.findOne(mov.getContaEmpresa().getId());
		contaEmpresa.setSaldo(contaEmpresa.getSaldo().add(mov.getSaldoMovimento()));
		contaEmpresa.setChequePendente(contaEmpresa.getChequePendente().add(mov.getChequePendente()));
		repository.save(contaEmpresa);
	}


}
