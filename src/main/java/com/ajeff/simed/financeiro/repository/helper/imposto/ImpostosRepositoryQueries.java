package com.ajeff.simed.financeiro.repository.helper.imposto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;

public interface ImpostosRepositoryQueries {

	public Page<Imposto> filtrar(ImpostoFilter filtro, Pageable pageable);
	
}
