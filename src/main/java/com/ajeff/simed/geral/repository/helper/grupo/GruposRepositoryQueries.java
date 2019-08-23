package com.ajeff.simed.geral.repository.helper.grupo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Grupo;
import com.ajeff.simed.geral.repository.filter.GrupoFilter;

public interface GruposRepositoryQueries {
	
	public Page<Grupo> filtrar(GrupoFilter filtro, Pageable pageable);

	public Grupo buscarGrupoComPermissoes(Long id);	
	
}
