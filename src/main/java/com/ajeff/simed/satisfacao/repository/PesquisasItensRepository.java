package com.ajeff.simed.satisfacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.PesquisaItem;

public interface PesquisasItensRepository extends JpaRepository<PesquisaItem, Long>{

	List<PesquisaItem> findByPesquisa(Pesquisa pesquisa);

}
