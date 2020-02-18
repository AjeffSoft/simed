package com.ajeff.simed.satisfacao.repository.helper.pergunta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.filter.PerguntaFilter;

public interface PerguntasRepositoryQueries {

	public Page<Pergunta> filtrar(PerguntaFilter filtro, Pageable pageable);
	
	
}
