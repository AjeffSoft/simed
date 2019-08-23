package com.ajeff.simed.geral.repository.helper.banco;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.filter.BancoFilter;

public interface BancosRepositoryQueries {

	public Page<Banco> filtrar(BancoFilter filtro, Pageable pageable);
	
	
}
