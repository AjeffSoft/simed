package com.ajeff.simed.satisfacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Questao;

public interface QuestoesRepository extends JpaRepository<Questao, Long>{

	List<Questao> findByEmpresa(Empresa empresa);
}
