package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.repository.helper.demissao.DemissoesRepositoryQueries;

public interface DemissoesRepository extends JpaRepository<DemissaoCooperado, Long>, DemissoesRepositoryQueries{


}
