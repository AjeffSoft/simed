package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.helper.transferencia.TransferenciasContasRepositoryQueries;

public interface TransferenciasContasRepository extends JpaRepository<TransferenciaContas, Long>, TransferenciasContasRepositoryQueries{

	List<TransferenciaContas> findByMovimentacao(MovimentacaoBancaria m);

}
