package com.ajeff.simed.satisfacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.helper.pesquisa.PesquisasRepositoryQueries;

public interface PesquisasRepository extends JpaRepository<Pesquisa, Long>, PesquisasRepositoryQueries{

}
