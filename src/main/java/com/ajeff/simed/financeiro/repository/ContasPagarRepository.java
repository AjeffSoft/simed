package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.helper.contaPagar.ContasPagarRepositoryQueries;

public interface ContasPagarRepository extends JpaRepository<ContaPagar, Long>, ContasPagarRepositoryQueries{


	@Query("select c from ContaPagar c where c.status like 'AUTORIZADO' order by c.valor desc")
	List<ContaPagar> buscarTodasContasAutorizadas();

	@Query("select c from ContaPagar c where c.id in ?1 order by c.valor desc")
	List<ContaPagar> buscarContasPagarSelecionadas(List<Long> ids);

	List<ContaPagar> findByIdIn(List<Long> ids);

	@Query("select c from ContaPagar c where c.fornecedor = ?1 order by c.vencimento")
	List<ContaPagar> findByContaPagarFornecedor(Fornecedor fornecedor);

	List<ContaPagar> findByPagamentoId(Long id);

}
