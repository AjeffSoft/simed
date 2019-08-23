package com.ajeff.simed.geral.repository.helper.permissao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Permissao;
import com.ajeff.simed.geral.repository.filter.PermissaoFilter;

public interface PermissoesRepositoryQueries {
	
	public Page<Permissao> filtrar(PermissaoFilter filtro, Pageable pageable);

}
