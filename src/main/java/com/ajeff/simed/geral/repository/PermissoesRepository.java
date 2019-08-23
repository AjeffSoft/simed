package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.geral.model.Permissao;
import com.ajeff.simed.geral.repository.helper.permissao.PermissoesRepositoryQueries;

@Repository
public interface PermissoesRepository extends JpaRepository<Permissao, Long>, PermissoesRepositoryQueries{

	public Optional<Permissao> findByNomeIgnoreCase(String nome);

	@Query("select p from Permissao p order by p.nome")
	public List<Permissao> buscarTodasPermissoes();
}
