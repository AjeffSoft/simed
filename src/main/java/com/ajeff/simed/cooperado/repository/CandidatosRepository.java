package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Candidato;

@Repository
public interface CandidatosRepository extends JpaRepository<Candidato, Long>{

}
