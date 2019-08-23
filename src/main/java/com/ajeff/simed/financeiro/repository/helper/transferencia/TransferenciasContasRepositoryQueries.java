package com.ajeff.simed.financeiro.repository.helper.transferencia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.filter.TransferenciaFilter;

public interface TransferenciasContasRepositoryQueries {

	Page<TransferenciaContas> filtrar(TransferenciaFilter transferenciaFilter, Pageable pageable);
	
	
}
