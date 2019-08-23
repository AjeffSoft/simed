package com.ajeff.simed.geral.service;

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

import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.repository.AgenciasRepository;
import com.ajeff.simed.geral.repository.filter.AgenciaFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class AgenciaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AgenciaService.class);	
	
	@Autowired
	private AgenciasRepository repository;
	
	@Transactional
	public Agencia salvar(Agencia agencia) {
		
		testeRegistroJaCadastrado(agencia);
		return repository.save(agencia);
	}

	public Page<Agencia> filtrar(AgenciaFilter agenciaFilter, Pageable pageable) {
		return repository.filtrar(agenciaFilter, pageable);
	}

	public List<Agencia> findAll() {
		return repository.findAll();
	}
	
	public Agencia findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "a agencia";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Agencia agencia) {
		Optional<Agencia> optional = repository.findByAgenciaAndBanco(agencia.getAgencia(), agencia.getBanco());
		if(optional.isPresent() && !optional.get().equals(agencia)) {
			throw new RegistroJaCadastradoException("Já existe uma agencia desse banco cadastrada!");
		}
	}

	public List<Agencia> findAllOrderByAgencia() {
		return repository.findAllOrderByAgencia();
	}

}
