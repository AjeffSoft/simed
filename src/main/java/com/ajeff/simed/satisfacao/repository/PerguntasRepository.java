package com.ajeff.simed.satisfacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Pergunta;

public interface PerguntasRepository extends JpaRepository<Pergunta, Long>{

	List<Pergunta> findByEmpresa(Empresa empresa);
}
