package com.ajeff.simed.cooperado.repository.helper.diretoria;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.repository.filter.DiretoriaFilter;

@Repository
public interface DiretoriasRepositoryQueries {
	
	Page<Diretoria> filtrar(DiretoriaFilter filtro, Pageable pageable);
	
	
	
}
