package com.ajeff.simed.satisfacao.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.PerguntasRepository;
import com.ajeff.simed.satisfacao.repository.filter.PerguntaFilter;

@Service
public class PerguntaService {

	
	@Autowired
	private PerguntasRepository repository;
	
	
	@Transactional
	public Pergunta salvar(Pergunta pergunta) {
		
		testeRegistroJaCadastrado(pergunta);
		return repository.save(pergunta);
	}

	public Page<Pergunta> filtrar(PerguntaFilter perguntaFilter, Pageable pageable) {
		return repository.filtrar(perguntaFilter, pageable);
	}

	public List<Pergunta> findAll() {
		return repository.findAll();
	}
	
	public Pergunta findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a pergunta. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Pergunta pergunta) {
		Optional<Pergunta> optional = repository.findByNomeIgnoreCase(pergunta.getNome());
		if(optional.isPresent() && !optional.get().equals(pergunta)) {
			throw new RegistroJaCadastradoException("Já existe uma pergunta com esse nome cadastrada!");
		}
	}

	public List<Pergunta> findAllOrderByNome() {
		return repository.findAllOrderByNome();
	}

	
}
