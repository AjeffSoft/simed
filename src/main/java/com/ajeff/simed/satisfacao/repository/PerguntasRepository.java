package com.ajeff.simed.satisfacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.helper.pergunta.PerguntasRepositoryQueries;

public interface PerguntasRepository extends JpaRepository<Pergunta, Long>, PerguntasRepositoryQueries{

	Optional<Pergunta> findByNomeIgnoreCase(String nome);

	@Query("select p from Pergunta p order by p.nome")
	List<Pergunta> findAllOrderByNome();



}
