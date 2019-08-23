package com.ajeff.simed.geral.repository.helper.cidade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.repository.filter.CidadeFilter;

public interface CidadesRepositoryQueries {

	public Page<Cidade> filtrar(CidadeFilter filtro, Pageable pageable);
	
	
}
