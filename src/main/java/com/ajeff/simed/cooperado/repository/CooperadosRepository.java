package com.ajeff.simed.cooperado.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.helper.cooperado.CooperadosRepositoryQueries;

public interface CooperadosRepository extends JpaRepository<Cooperado, Long>, CooperadosRepositoryQueries{

	Optional<Cooperado> findByDocumentoCpf(String cpf);

	@Query("select c from Cooperado c order by c.nome asc")
	List<Cooperado> listarCooperados();

	List<Cooperado> findByAtivoFalse();

	List<Cooperado> findByAtivoTrue();

}
