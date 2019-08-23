package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.helper.extrato.ExtratosRepositoryQueries;
import com.ajeff.simed.geral.model.ContaEmpresa;

public interface ExtratosRepository extends JpaRepository<ExtratoBancario, Long>, ExtratosRepositoryQueries{

	ExtratoBancario findByPagamento(Pagamento pagamento);

	List<ExtratoBancario> findByTransferencia(TransferenciaContas transferencia);

	ExtratoBancario findByRecebimento(Recebimento recebimento);

	List<ExtratoBancario> findByMovimentacao(Movimentacao mov);

	List<ExtratoBancario> findByContaBancaria(ContaEmpresa conta);

	@Query("select ex from ExtratoBancario ex where ex.movimentacao = ?1 and ex.contaBancaria = ?2 order by ex.data, ex.id")
	List<ExtratoBancario> findByMovimentacaoAndContaBancariaOrderByDataAndId(MovimentacaoBancaria mov, ContaEmpresa contaEmpresa);

}
