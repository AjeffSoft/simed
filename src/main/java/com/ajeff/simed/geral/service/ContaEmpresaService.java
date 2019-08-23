package com.ajeff.simed.geral.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//	public List<ContaEmpresa> findByEmpresaIdETipoCaixinha(Long id) {
//		return repository.findByEmpresaIdETipoCaixinha(id);
//	}

	public List<ContaEmpresa> findByEmpresaId(Long id) {
		return repository.findByEmpresaId(id);
	}

//	public List<ContaEmpresa> buscarTodasContasBancarias() {
//		return repository.buscarTodasContasBancarias();
//	}

	public List<ContaEmpresa> listarTodosOrdenadoPorNome() {
		return repository.listarTodosOrdenadoPorNome();
	}


}
