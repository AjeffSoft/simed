package com.ajeff.simed.financeiro.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class FornecedorService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FornecedorService.class);	
	
	@Autowired
	private FornecedoresRepository repository;
	
	
	@Transactional
	public Fornecedor salvar(Fornecedor fornecedor) {
		testeRegistroJaCadastrado(fornecedor);
		
		registroNovo(fornecedor);
		
		return repository.save(fornecedor);
	}


	private void registroNovo(Fornecedor fornecedor) {
		if(fornecedor.isNovo()) {
			fornecedor.setClifor(true);
		}
	}


	public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
		return repository.filtrar(fornecedorFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o fornecedor";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}

	
	public Fornecedor findOne(Long id) {
		return repository.findOne(id);
	}		

	private void testeRegistroJaCadastrado(Fornecedor fornecedor) {
		Optional<Fornecedor> optional = repository.findByNomeOrSiglaIgnoreCase(fornecedor.getNome(), fornecedor.getSigla());
		
		if(optional.isPresent() && !optional.get().equals(fornecedor)) {
			throw new RegistroJaCadastradoException("Nome e/ou sigla já cadastrado!");
		}
	}


	public List<Fornecedor> findByNomeContainingIgnoreCase(String nome) {
		return repository.findByNomeContainingIgnoreCase(nome);
	}


	public Fornecedor buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}


	public List<Fornecedor> listarTodosFornecedores() {
		return repository.listarTodosFornecedores();
	}

}
