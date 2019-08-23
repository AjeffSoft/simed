package com.ajeff.simed.financeiro.repository.helper.planoConta;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.repository.filter.PlanoContaFilter;

public interface PlanosContaRepositoryQueries {

	public Page<PlanoConta> filtrar(PlanoContaFilter filtro, Pageable pageable);
	
	
}
