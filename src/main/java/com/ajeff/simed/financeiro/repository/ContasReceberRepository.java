package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.helper.contaReceber.ContasReceberRepositoryQueries;

public interface ContasReceberRepository extends JpaRepository<ContaReceber, Long>, ContasReceberRepositoryQueries{

	Optional<ContaReceber> findByDocumentoAndFornecedor(String documento, Fornecedor fornecedor);

	List<ContaReceber> findByFornecedor(Fornecedor fornecedor);


}
