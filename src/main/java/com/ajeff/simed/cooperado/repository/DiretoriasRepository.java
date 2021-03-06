package com.ajeff.simed.cooperado.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.repository.helper.diretoria.DiretoriasRepositoryQueries;

@Repository
public interface DiretoriasRepository extends JpaRepository<Diretoria, Long>, DiretoriasRepositoryQueries {

}
