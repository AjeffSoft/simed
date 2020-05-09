package com.ajeff.simed.geral.repository.helper.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Pessoa;
import com.ajeff.simed.geral.repository.filter.PessoaFilter;

public interface PessoasRepositoryQueries {
	
	Page<Pessoa> filtrar(PessoaFilter filtro, Pageable pageable);
	
	public Pessoa buscarComCidadeEstado(Long id);

}
