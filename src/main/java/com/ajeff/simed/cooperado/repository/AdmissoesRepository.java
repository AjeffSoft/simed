package com.ajeff.simed.cooperado.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.helper.admissao.AdmissoesRepositoryQueries;

public interface AdmissoesRepository extends JpaRepository<AdmissaoCooperado, Long>, AdmissoesRepositoryQueries{

	Optional<AdmissaoCooperado> findByRegistro(String registro);



}
