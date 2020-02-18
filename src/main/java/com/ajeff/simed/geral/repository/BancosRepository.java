package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.helper.banco.BancosRepositoryQueries;

public interface BancosRepository extends JpaRepository<Banco, Long>, BancosRepositoryQueries{

	Optional<Banco> findByCodigoOrNomeIgnoreCase(String codigo, String nome);

	@Query("select b from Banco b where b.situacao =true order by b.nome asc")
	List<Banco> findByNomeOrderByNomeAsc();


}
