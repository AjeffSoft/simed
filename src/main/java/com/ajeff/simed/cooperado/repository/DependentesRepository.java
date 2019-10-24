package com.ajeff.simed.cooperado.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.repository.helper.dependente.DependentesRepositoryQueries;

public interface DependentesRepository extends JpaRepository<Dependente, Long>, DependentesRepositoryQueries{

	Optional<Dependente> findByNomeIgnoreCase(String nome);

	List<Dependente> findByCooperado(Cooperado cooperado);


}
