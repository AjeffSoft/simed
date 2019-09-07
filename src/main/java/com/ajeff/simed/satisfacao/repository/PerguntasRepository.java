package com.ajeff.simed.satisfacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Pergunta;

public interface PerguntasRepository extends JpaRepository<Pergunta, Long>{

	@Query("select q from Questao q where q.empresa = ?1 order by q.nome")
	List<Pergunta> findByQuestaoEmpresa(Empresa empresa);


}
