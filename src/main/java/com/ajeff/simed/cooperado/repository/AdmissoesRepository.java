package com.ajeff.simed.cooperado.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.helper.admissao.AdmissoesRepositoryQueries;

public interface AdmissoesRepository extends JpaRepository<AdmissaoCooperado, Long>, AdmissoesRepositoryQueries{

	Optional<AdmissaoCooperado> findByRegistroIgnoreCase(String registro);

	@Query("select a from AdmissaoCooperado a where a.ativo = true")
	List<AdmissaoCooperado> findByAdmissaoCooperadoAtivoTrue();

	@Query("select a from AdmissaoCooperado a where a.ativo = true order by a.cooperado.nome")
	List<AdmissaoCooperado> findByAdmissaoOrdenadoPorCooperado();

	@Query("select a from AdmissaoCooperado a where a.ativo = false")
	List<AdmissaoCooperado> findByAdmissaoCooperadoAtivoFalse();



}
