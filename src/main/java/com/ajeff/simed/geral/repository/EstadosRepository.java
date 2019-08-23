package com.ajeff.simed.geral.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.geral.model.Estado;

@Repository
public interface EstadosRepository extends JpaRepository<Estado, Long>{

	@Query("select e from Estado e order by e.nome")
	public List<Estado> findAllOrderByNome();
}
