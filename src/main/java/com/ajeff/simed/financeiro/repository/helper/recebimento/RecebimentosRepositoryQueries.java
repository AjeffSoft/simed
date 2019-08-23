package com.ajeff.simed.financeiro.repository.helper.recebimento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.filter.RecebimentoFilter;

public interface RecebimentosRepositoryQueries {

	public Page<Recebimento> filtrar(RecebimentoFilter filtro, Pageable pageable);
	
}
