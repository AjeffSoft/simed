package com.ajeff.simed.geral.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.geral.model.Grupo;
import com.ajeff.simed.geral.repository.helper.grupo.GruposRepositoryQueries;

@Repository
public interface GruposRepository extends JpaRepository<Grupo, Long>, GruposRepositoryQueries{

	public Optional<Grupo> findByNomeIgnoreCase(String nome);
}
