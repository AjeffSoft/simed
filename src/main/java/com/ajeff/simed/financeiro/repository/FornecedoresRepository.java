package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.helper.fornecedor.FornecedoresRepositoryQueries;

public interface FornecedoresRepository extends JpaRepository<Fornecedor, Long>, FornecedoresRepositoryQueries{

	Optional<Fornecedor> findByNomeOrSiglaIgnoreCase(String nome, String sigla);

	Optional<Fornecedor> findByDocumento1(String documento1);

	@Query("select p from Pessoa p where lower(p.nome) like upper(?1) and p.clifor = true order by p.nome")
	List<Fornecedor> findByNomeContainingIgnoreCase(String nome);

	@Query("select f from Pessoa f where f.clifor = true order by nome asc")
	List<Fornecedor> listarTodosFornecedores();


}
