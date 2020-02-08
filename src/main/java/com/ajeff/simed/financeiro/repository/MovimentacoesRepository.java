package com.ajeff.simed.financeiro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.repository.helper.movimentacao.MovimentacoesRepositoryQueries;
import com.ajeff.simed.geral.model.Empresa;

public interface MovimentacoesRepository extends JpaRepository<Movimentacao, Long>, MovimentacoesRepositoryQueries{


	@Query("select m from Movimentacao m where m.empresa = ?1 and m.fechado = false")
	Optional<Movimentacao> findByEmpresaAndStatusAberto(Empresa empresa);

	@Query("select m from Movimentacao m where m.empresa = ?1 and m.fechado = false")
	Movimentacao findByEmpresaAndStatus(Empresa empresa);


	
}
