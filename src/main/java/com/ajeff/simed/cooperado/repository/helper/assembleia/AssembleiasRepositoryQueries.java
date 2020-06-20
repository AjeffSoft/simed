package com.ajeff.simed.cooperado.repository.helper.assembleia;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.repository.filter.AssembleiaFilter;

@Repository
public interface AssembleiasRepositoryQueries {
	
	Page<Assembleia> filtrar(AssembleiaFilter filtro, Pageable pageable);
	
	
	
}
