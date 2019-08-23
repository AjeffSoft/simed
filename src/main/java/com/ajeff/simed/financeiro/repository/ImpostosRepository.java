package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.repository.helper.imposto.ImpostosRepositoryQueries;

public interface ImpostosRepository extends JpaRepository<Imposto, Long>, ImpostosRepositoryQueries{

	List<Imposto> findByContaPagarOrigem(ContaPagar contaPagar);

	List<Imposto> findByContaPagarOrigemFornecedorOrderByVencimento(Fornecedor fornecedor);

}
