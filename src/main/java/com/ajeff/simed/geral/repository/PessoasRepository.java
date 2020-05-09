package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.geral.model.Pessoa;
import com.ajeff.simed.geral.repository.helper.pessoa.PessoasRepositoryQueries;


@Repository
public interface PessoasRepository extends JpaRepository<Pessoa, Long>, PessoasRepositoryQueries{


	Optional<Pessoa> findByDocumento1(String documento);

	Optional<Pessoa> findByNomeAndFantasiaAndSiglaIgnoreCase(String nome, String fantasia, String sigla);
	

	List<Pessoa> findAllByOrderByNome();	
	
	List<Pessoa> findByAtivoFalseOrderByNome();

	List<Pessoa> findByAtivoTrueOrderByNome();


	
}
