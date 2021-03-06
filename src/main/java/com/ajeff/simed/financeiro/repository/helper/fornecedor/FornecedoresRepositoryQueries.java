package com.ajeff.simed.financeiro.repository.helper.fornecedor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;

public interface FornecedoresRepositoryQueries {
	
	Page<Fornecedor> filtrar(FornecedorFilter filtro, Pageable pageable);
	
	public Fornecedor buscarComCidadeEstado(Long id);
	
	public List<Fornecedor> buscarTodosFornecedores();
	
	
}
