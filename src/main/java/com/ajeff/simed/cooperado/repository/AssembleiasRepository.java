package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.repository.helper.assembleia.AssembleiasRepositoryQueries;

@Repository
public interface AssembleiasRepository extends JpaRepository<Assembleia, Long>, AssembleiasRepositoryQueries {

}
