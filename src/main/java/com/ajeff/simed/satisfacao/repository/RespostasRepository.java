package com.ajeff.simed.satisfacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.repository.helper.resposta.RespostasRepositoryQueries;

public interface RespostasRepository extends JpaRepository<Resposta, Long>, RespostasRepositoryQueries{

	Optional<Resposta> findByNomeIgnoreCase(String nome);

	@Query("select p from Resposta p order by p.nome")
	List<Resposta> findAllOrderByNome();



}
