package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.repository.helper.planoConta.PlanosContaRepositoryQueries;

public interface PlanosContaRepository extends JpaRepository<PlanoConta, Long>, PlanosContaRepositoryQueries{

	Optional<PlanoConta> findByNomeIgnoreCase(String nome);

	@Query("select p from PlanoConta p where p.situacao = true order by p.nome asc")
	List<PlanoConta> findAllOrderByNome();

	@Query("select p from PlanoConta p where p.situacao = true and p.tipo = 'DÉBITO' order by p.nome asc")
	List<PlanoConta> listarTodosPlanosContaDebito();

	@Query("select p from PlanoConta p where p.situacao = true and p.tipo = 'CRÉDITO' order by p.nome asc")
	List<PlanoConta> listarTodosPlanosContaCredito();

	@Query("select p from PlanoConta p where p.situacao = true and p.tipo = 'DÉBITO' and p.id = ?1 order by p.nome asc")
	List<PlanoConta> findByPlanoContaIdOrderByNome(Long idPlanoConta);


}
