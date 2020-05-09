package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.helper.medico.MedicosRepositoryQueries;

@Repository
public interface MedicosRepository extends JpaRepository<Medico, Long>, MedicosRepositoryQueries{

}
