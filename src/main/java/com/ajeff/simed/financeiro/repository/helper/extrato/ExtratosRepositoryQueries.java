package com.ajeff.simed.financeiro.repository.helper.extrato;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;

public interface ExtratosRepositoryQueries {

	public Page<Extrato> filtrar(ExtratoFilter filtro, Pageable pageable);
	
}
