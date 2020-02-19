package com.ajeff.simed.satisfacao.repository.helper.resposta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.repository.filter.RespostaFilter;

public interface RespostasRepositoryQueries {

	public Page<Resposta> filtrar(RespostaFilter filtro, Pageable pageable);
	
	
}
