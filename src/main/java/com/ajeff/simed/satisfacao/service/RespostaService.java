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
import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.repository.RespostasRepository;
import com.ajeff.simed.satisfacao.repository.filter.RespostaFilter;

@Service
public class RespostaService {

	
	@Autowired
	private RespostasRepository repository;
	
	
	@Transactional
	public Resposta salvar(Resposta resposta) {
		
		testeRegistroJaCadastrado(resposta);
		return repository.save(resposta);
	}

	public Page<Resposta> filtrar(RespostaFilter respostaFilter, Pageable pageable) {
		return repository.filtrar(respostaFilter, pageable);
	}

	public List<Resposta> findAll() {
		return repository.findAll();
	}
	
	public Resposta findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a resposta. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Resposta resposta) {
		Optional<Resposta> optional = repository.findByNomeIgnoreCase(resposta.getNome());
		if(optional.isPresent() && !optional.get().equals(resposta)) {
			throw new RegistroJaCadastradoException("Já existe uma resposta com esse nome cadastrada!");
		}
	}

	public List<Resposta> findAllOrderByNome() {
		return repository.findAllOrderByNome();
	}

	
}
