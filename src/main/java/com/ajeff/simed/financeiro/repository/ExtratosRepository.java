package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.helper.extrato.ExtratosRepositoryQueries;

public interface ExtratosRepository extends JpaRepository<Extrato, Long>, ExtratosRepositoryQueries{

//	Extrato findByPagamento(Pagamento pagamento);
//
	List<Extrato> findByTransferencia(TransferenciaContas transferencia);

	Extrato findByRecebimento(Recebimento recebimento);
//
//	List<Extrato> findByMovimentacaoItem(MovimentacaoItem mov);
//
//	List<Extrato> findByContaBancaria(ContaEmpresa conta);
//
	@Query("select e from Extrato e where e.movimentacaoItem = ?1 order by e.data, e.id")
	List<Extrato> findByMovimentacaoItemAndContaBancariaOrderByDataAndId(MovimentacaoItem m);

	Extrato findByPagamento(Pagamento pagamento);

}
