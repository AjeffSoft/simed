package com.ajeff.simed.cooperado.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.helper.cooperado.CooperadosRepositoryQueries;

public interface CooperadosRepository extends JpaRepository<Cooperado, Long>, CooperadosRepositoryQueries{

	Optional<Cooperado> findByDocumentoCpf(String cpf);

	@Query("select c from Cooperado c order by c.nome asc")
	List<Cooperado> ordenarNome();

//	Optional<Cooperado> findByNomeOrSiglaIgnoreCase(String nome, String sigla);
//
//	@Query("select f from Fornecedor f where lower(f.nome) like upper(?1) and f.clifor = true order by f.nome")
//	List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
//
//	@Query("select f from Fornecedor f where f.clifor = true order by nome asc")
//	List<Fornecedor> listarTodosFornecedores();

}
