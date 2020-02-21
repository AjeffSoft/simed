package com.ajeff.simed.satisfacao.repository.helper.questionario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.filter.QuestionarioFilter;

public interface QuestionariosRepositoryQueries {
	
	Page<Questionario> filtrar(QuestionarioFilter filtro, Pageable pageable);

	public Questionario buscarQuestionarioComRespostas(Long id);

}
