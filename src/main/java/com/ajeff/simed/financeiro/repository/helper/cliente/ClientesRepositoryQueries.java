package com.ajeff.simed.financeiro.repository.helper.cliente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;

public interface ClientesRepositoryQueries {
	
	Page<Fornecedor> filtrar(FornecedorFilter filtro, Pageable pageable);
	
	public Fornecedor buscarComCidadeEstado(Long id);
	
}
