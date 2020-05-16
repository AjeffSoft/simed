package com.ajeff.simed.cooperado.repository.helper.cancelamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;

public interface CancelamentosRepositoryQueries {
	
	Page<Cooperado> filtrar(CancelamentoFilter filtro, Pageable pageable);
	

	
}
