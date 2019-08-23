package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.geral.model.ContaEmpresa;

public interface MovimentacoesBancariasRepository extends JpaRepository<MovimentacaoBancaria, Long>{

	List<MovimentacaoBancaria> findByMovimentacao(Movimentacao mov);

	@Query("select mb from MovimentacaoBancaria mb where mb.movimentacao = ?1 and mb.contaEmpresa = ?2")
	MovimentacaoBancaria findByMovimentacaoAndEmpresa(Movimentacao mov, ContaEmpresa contaEmpresa);

	MovimentacaoBancaria findByMovimentacaoAndContaEmpresa(Movimentacao mov, ContaEmpresa contaEmpresa);

	@Query("select mb from MovimentacaoBancaria mb where mb.contaEmpresa.id = ?1")
	List<MovimentacaoBancaria> findByContaEmpresaEmpresaId(Long codigoEmpresa);


}

