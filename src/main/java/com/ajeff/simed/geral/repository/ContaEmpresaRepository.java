package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.helper.contaEmpresa.ContaEmpresaRepositoryQueries;

public interface ContaEmpresaRepository extends JpaRepository<ContaEmpresa, Long>, ContaEmpresaRepositoryQueries{
	
	public Optional<ContaEmpresa> findByContaAndEmpresaAndTipo(String conta, Empresa empresa, String tipo);

	@Query("select c from ContaEmpresa c where tipo like 'CORRENTE' order by c.nome, c.empresa.fantasia, c.agencia.banco.nome, c.conta")
	public List<ContaEmpresa> findByEmpresaIdOrderByConta();

	@Query("select c from ContaEmpresa c order by c.nome, c.empresa.fantasia, c.agencia.banco.nome, c.conta")
	public List<ContaEmpresa> listarTodosOrdenadoPorNome();

	public List<ContaEmpresa> findByEmpresa(Empresa empresa);

	@Query("select c from ContaEmpresa c where c.empresa.id = ?1 order by c.nome, c.empresa.fantasia, c.agencia.banco.nome, c.conta")
	public List<ContaEmpresa> findByEmpresaId(Long id);

}
