package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.cooperado.model.Cooperado;

public interface CooperadosRepository extends JpaRepository<Cooperado, Long>{

//	Optional<Cooperado> findByNomeOrSiglaIgnoreCase(String nome, String sigla);
//
//	@Query("select f from Fornecedor f where lower(f.nome) like upper(?1) and f.clifor = true order by f.nome")
//	List<Fornecedor> findByNomeContainingIgnoreCase(String nome);
//
//	@Query("select f from Fornecedor f where f.clifor = true order by nome asc")
//	List<Fornecedor> listarTodosFornecedores();

}
