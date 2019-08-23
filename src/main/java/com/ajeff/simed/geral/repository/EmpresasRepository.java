package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.helper.empresa.EmpresasRepositoryQueries;

public interface EmpresasRepository extends JpaRepository<Empresa, Long>, EmpresasRepositoryQueries{

	Optional<Empresa> findByNomeAndFantasiaOrCnpjOrSiglaIgnoreCase(String nome, String fantasia, String cnpj, String sigla);

	@Query("select e from Empresa e order by e.nome")
	List<Empresa> buscarTodasEmpresasOrdenadasPorNomeAsc();

}
