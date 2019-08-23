package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.helper.fornecedor.FornecedoresRepositoryQueries;

public interface FornecedoresRepository extends JpaRepository<Fornecedor, Long>, FornecedoresRepositoryQueries{

	Optional<Fornecedor> findByNomeOrSiglaIgnoreCase(String nome, String sigla);

	@Query("select f from Fornecedor f where lower(f.nome) like upper(?1) and f.clifor = true order by f.nome")
	List<Fornecedor> findByNomeContainingIgnoreCase(String nome);

	@Query("select f from Fornecedor f where f.clifor = true order by nome asc")
	List<Fornecedor> listarTodosFornecedores();

}
