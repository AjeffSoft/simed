package com.ajeff.simed.satisfacao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.helper.pergunta.PerguntasRepositoryQueries;

public interface PerguntasRepository extends JpaRepository<Pergunta, Long>, PerguntasRepositoryQueries{

	Optional<Pergunta> findByNomeIgnoreCase(String nome);



}
