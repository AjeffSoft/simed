package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.helper.cliente.ClientesRepositoryQueries;

public interface ClientesRepository extends JpaRepository<Fornecedor, Long>, ClientesRepositoryQueries{

	
	Optional<Fornecedor> findByNomeOrSiglaIgnoreCase(String nome, String sigla);

	Optional<Fornecedor> findByDocumento1(String documento1);
	
	@Query("select f from Fornecedor f where f.nome like ?1 and f.clifor = false")
	Optional<Fornecedor> findByNomeAndFornecedorTrueIgnoreCase(String nome);

	@Query("select f from Fornecedor f where lower(f.nome) like upper(?1) and f.clifor = false order by f.nome")
	List<Fornecedor> findByNomeClienteContainingIgnoreCase(String nome);

	@Query("select f from Fornecedor f where f.clifor = false order by nome asc")
	List<Fornecedor> listarTodosClientes();
	
	
}
