package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Estado;
import com.ajeff.simed.geral.repository.helper.cidade.CidadesRepositoryQueries;

public interface CidadesRepository extends JpaRepository<Cidade, Long>, CidadesRepositoryQueries{

	Optional<Cidade> findByNomeAndUf(String nome, Estado uf);

	@Query("select c from Cidade c where c.estado.id = ?1 order by c.nome asc")
	public List<Cidade> findByEstadoId(Long idEstado);	

}
