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
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.QuestionariosRepository;
import com.ajeff.simed.satisfacao.repository.filter.QuestionarioFilter;

@Service
public class QuestionarioService {

	
	@Autowired
	private QuestionariosRepository repository;
	
	@Transactional
	public Questionario salvar(Questionario questionario) {
		
		testeRegistroJaCadastrado(questionario);
		return repository.save(questionario);
	}

	public Page<Questionario> filtrar(QuestionarioFilter questionarioFilter, Pageable pageable) {
		return repository.filtrar(questionarioFilter, pageable);
	}

	public List<Questionario> findAll() {
		return repository.findAll();
	}
	
	public Questionario findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o questionario. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Questionario questionario) {
		Optional<Questionario> optional = repository.findByEmpresaAndPergunta(questionario.getEmpresa(), questionario.getPergunta());
		if(optional.isPresent() && !optional.get().equals(questionario)) {
			throw new RegistroJaCadastradoException("Já existe uma questão com essa pergunta cadastrada para essa empresa!");
		}
	}

}
