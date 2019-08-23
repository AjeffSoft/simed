package com.ajeff.simed.financeiro.repository.helper.extrato;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;

public interface ExtratosRepositoryQueries {

	public Page<ExtratoBancario> filtrar(ExtratoFilter filtro, Pageable pageable);
	
}
