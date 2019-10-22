package com.ajeff.simed.cooperado.repository.helper.dependente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.repository.filter.DependenteFilter;

public interface DependentesRepositoryQueries {
	
	Page<Dependente> filtrar(DependenteFilter filtro, Pageable pageable);
	
}
