package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.geral.model.ContaEmpresa;

public interface MovimentacoesItensRepository extends JpaRepository<MovimentacaoItem, Long>{

	MovimentacaoItem findByMovimentacaoAndContaEmpresa(Movimentacao movimentacao, ContaEmpresa contaEmpresa);

	List<MovimentacaoItem> findByMovimentacao(Movimentacao movimentacao);

	List<MovimentacaoItem> findByContaEmpresa(ContaEmpresa contaEmpresa);
}

