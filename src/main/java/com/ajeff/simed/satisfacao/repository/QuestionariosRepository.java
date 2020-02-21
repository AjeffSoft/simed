package com.ajeff.simed.satisfacao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.helper.questionario.QuestionariosRepositoryQueries;

public interface QuestionariosRepository extends JpaRepository<Questionario, Long>, QuestionariosRepositoryQueries{

	Optional<Questionario> findByEmpresaAndPergunta(Empresa empresa, Pergunta pergunta);

	List<Questionario> findByEmpresaAndAtivoTrue(Empresa empresa);

}
