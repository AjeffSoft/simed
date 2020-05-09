package com.ajeff.simed.cooperado.repository.helper.cooperado;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;

public interface CooperadosRepositoryQueries {
	
	Page<Cooperado> filtrar(CooperadoFilter filtro, Pageable pageable);
	

	
}
