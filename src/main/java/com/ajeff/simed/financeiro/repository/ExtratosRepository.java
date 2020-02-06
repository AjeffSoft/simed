package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.Extrato;
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
//	@Query("select ex from ExtratoBancario ex where ex.movimentacao_item = ?1 and ex.contaBancaria = ?2 order by ex.data, ex.id")
//	List<Extrato> findByMovimentacaoAndContaBancariaOrderByDataAndId(MovimentacaoItem mov, ContaEmpresa contaEmpresa);

}
