package com.ajeff.simed.financeiro.repository.helper.pagamento;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;

public interface PagamentosRepositoryQueries {

	public Page<Pagamento> filtrar(PagamentoFilter filtro, Pageable pageable);
	
	public BigDecimal totalGeral(PagamentoFilter filtro);
}
