package com.ajeff.simed.geral.service;

import java.util.ArrayList;
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

import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.BancosRepository;
import com.ajeff.simed.geral.repository.filter.BancoFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class BancoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BancoService.class);	
	
	@Autowired
	private BancosRepository repository;
	
	
	@Transactional
	public Banco salvar(Banco banco) {
		
		testeRegistroJaCadastrado(banco);
		return repository.save(banco);
	}

	public Page<Banco> filtrar(BancoFilter bancoFilter, Pageable pageable) {
		return repository.filtrar(bancoFilter, pageable);
	}

	public List<Banco> findAll() {
		return repository.findAll();
	}
	
	public Banco findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o banco";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Banco banco) {
		Optional<Banco> optional = repository.findByCodigoOrNomeIgnoreCase(banco.getCodigo(), banco.getNome());
		if(optional.isPresent() && !optional.get().equals(banco)) {
			throw new RegistroJaCadastradoException("Já existe um banco com esse código ou nome cadastrado!");
		}
	}

	public List<Banco> findByNomeOrderByNomeAsc() {
		return repository.findByNomeOrderByNomeAsc();
	}
	
}
