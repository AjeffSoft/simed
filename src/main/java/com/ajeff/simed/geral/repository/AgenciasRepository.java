package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.helper.agencia.AgenciasRepositoryQueries;

public interface AgenciasRepository extends JpaRepository<Agencia, Long>, AgenciasRepositoryQueries{

	Optional<Agencia> findByAgenciaAndBanco(String agencia, Banco banco);

	@Query("select a from Agencia a order by a.agencia asc")
	List<Agencia> findAllOrderByAgencia();

}
