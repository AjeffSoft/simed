package com.ajeff.simed.geral.repository.helper.agencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.repository.filter.AgenciaFilter;

public interface AgenciasRepositoryQueries {
	
	Page<Agencia> filtrar(AgenciaFilter filtro, Pageable pageable);

}
