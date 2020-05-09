package com.ajeff.simed.cooperado.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.helper.cooperado.CooperadosRepositoryQueries;

public interface CooperadosRepository extends JpaRepository<Cooperado, Long>, CooperadosRepositoryQueries{

	Optional<Cooperado> findByRegistroIgnoreCase(String registro);

	@Query("select c from Cooperado c where c.ativo = true")
	List<Cooperado> findByAdmissaoCooperadoAtivoTrue();

	@Query("select c from Cooperado c where c.ativo = true order by c.medico.nome")
	List<Cooperado> findByAdmissaoOrdenadoPorCooperado();

	@Query("select c from Cooperado c where c.ativo = false")
	List<Cooperado> findByAdmissaoCooperadoAtivoFalse();



}
