package com.ajeff.simed.satisfacao.repository.helper.pesquisa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.filter.PesquisaFilter;

public interface PesquisasRepositoryQueries {

	public Page<Pesquisa> filtrar(PesquisaFilter filtro, Pageable pageable);
	
	
}
