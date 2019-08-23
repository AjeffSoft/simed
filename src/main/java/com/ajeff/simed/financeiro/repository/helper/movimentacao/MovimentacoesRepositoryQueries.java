package com.ajeff.simed.financeiro.repository.helper.movimentacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.repository.filter.MovimentacaoFilter;

public interface MovimentacoesRepositoryQueries {

	public Page<Movimentacao> filtrar(MovimentacaoFilter filtro, Pageable pageable);
	
}
