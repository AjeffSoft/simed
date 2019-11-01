package com.ajeff.simed.cooperado.repository.helper.demissao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.repository.filter.DemissaoFilter;

public interface DemissoesRepositoryQueries {
	
	Page<DemissaoCooperado> filtrar(DemissaoFilter filtro, Pageable pageable);
	

	
}
