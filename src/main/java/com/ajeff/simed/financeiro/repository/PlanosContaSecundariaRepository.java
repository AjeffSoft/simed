package com.ajeff.simed.financeiro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;

public interface PlanosContaSecundariaRepository extends JpaRepository<PlanoContaSecundaria, Long> {

	Optional<PlanoContaSecundaria> findByNomeAndPlanoContaTipoIgnoreCase(String nome, String tipo);

	List<PlanoContaSecundaria> findByPlanoContaIdAndSituacaoTrue(Long idPlanoConta);

	List<PlanoContaSecundaria> findByPlanoContaId(Long idPlanoConta);
	
	
	@Query("select s from PlanoContaSecundaria s where s.tipo = 'DÉBITO' order by s.nome asc")
	List<PlanoContaSecundaria> listarTodosPlanosContaSecundariaDebito();

	@Query("select s from PlanoContaSecundaria s where s.tipo = 'CRÉDITO' order by s.nome asc")
	List<PlanoContaSecundaria> listarTodosPlanosContaSecundariaCredito();


}
