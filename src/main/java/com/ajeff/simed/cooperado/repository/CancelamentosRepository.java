package com.ajeff.simed.cooperado.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.helper.cancelamento.CancelamentosRepositoryQueries;

public interface CancelamentosRepository extends JpaRepository<Cooperado, Long>, CancelamentosRepositoryQueries{

	List<Cooperado> findByAtivoFalseOrderByRegistro();


}
