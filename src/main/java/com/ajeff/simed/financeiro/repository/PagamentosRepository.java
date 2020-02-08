package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.helper.pagamento.PagamentosRepositoryQueries;

public interface PagamentosRepository extends JpaRepository<Pagamento, Long>, PagamentosRepositoryQueries{

	List<Pagamento> findByMovimentacaoItem(MovimentacaoItem m);

}
