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
import com.ajeff.simed.financeiro.repository.ClientesRepository;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class ClienteService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ClienteService.class);	
	
	@Autowired
	private ClientesRepository repository;
	
	
	@Transactional
	public Fornecedor salvar(Fornecedor cliente) {
		testeRegistroJaCadastrado(cliente);
		
		registroNovo(cliente);
		
		return repository.save(cliente);
	}


	private void registroNovo(Fornecedor cliente) {
		if(cliente.isNovo()) {
			cliente.setClifor(false);
		}
	}

	
	public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
		return repository.filtrar(fornecedorFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o cliente";

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
		Optional<Fornecedor> optional = repository.findByNomeAndFornecedorTrueIgnoreCase(fornecedor.getNome());
		
		if(optional.isPresent() && !optional.get().equals(fornecedor)) {
			throw new RegistroJaCadastradoException("Já existe um cliente com esse nome cadastrado!");
		}
	}

	public Fornecedor buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}


	public List<Fornecedor> findByNomeClienteContainingIgnoreCase(String nome) {
		return repository.findByNomeClienteContainingIgnoreCase(nome);
	}


	public List<Fornecedor> listarTodosClientes() {
		return repository.listarTodosClientes();
	}
}
