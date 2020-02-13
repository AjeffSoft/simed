package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesItensRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;
import com.ajeff.simed.geral.model.ContaEmpresa;

@Service
public class ExtratoService {

	

	@Autowired
	private ExtratosRepository repository;
	@Autowired
	private MovimentacoesItensRepository movBancariaRepository;
	@Autowired
	private MovimentacoesRepository movRepository;

	
	public void criarMovimentoNoExtrato(Extrato extrato, BigDecimal valor, ContaEmpresa conta, Boolean credito, String status, LocalDate data, String tipo, MovimentacaoItem movimentacaoItem) {
		extrato.setData(data);
		extrato.setContaBancaria(conta);
		extrato.setCredito(credito);
		extrato.setTipo(tipo);
		extrato.setStatus(status);
		extrato.setMovimentacaoItem(movimentacaoItem);
		extrato.setValor(valor);
	}

	
	public void criarMovimentoPagamentoNoExtrato(BigDecimal valor, Pagamento pagamento, ContaEmpresa contaEmpresa,
			boolean credito, String status, LocalDate data, String tipo, MovimentacaoItem movimentacaoItem) {
		Extrato extrato = new Extrato();
		extrato.setPagamento(pagamento);
		extrato.setHistorico("Pagamento afetuado por: " + pagamento.getTipo() + " - Documento nº: " + pagamento.getDocumento());
		criarMovimentoNoExtrato(extrato, valor, contaEmpresa, credito, status, data, tipo, movimentacaoItem);
		repository.save(extrato);
	}
	
	public void criarMovimentoInicialNoExtrato(BigDecimal valor, ContaEmpresa contaEmpresa, boolean credito,
			String status, LocalDate data, String tipo, MovimentacaoItem movimentacaoItem) {
		Extrato extrato = new Extrato();
		extrato.setHistorico("Saldo Inicial da Movimentação");
		criarMovimentoNoExtrato(extrato, valor, contaEmpresa, credito, status, data, tipo, movimentacaoItem);
		repository.save(extrato);
	}	
	

	public void criarRecebimentoNoExtrato(BigDecimal valor, Recebimento recebimento, ContaEmpresa contaEmpresa,
			boolean credito, String status, LocalDate data, String tipo, MovimentacaoItem movimentacaoItem) {
		Extrato extrato = new Extrato();
		extrato.setRecebimento(recebimento);
		extrato.setHistorico("Recebimento de: " + recebimento.getContaReceber().getFornecedor().getFantasia() +" - Documento nº: "+ recebimento.getContaReceber().getDocumento());
		criarMovimentoNoExtrato(extrato, valor, contaEmpresa, credito, status, data, tipo, movimentacaoItem);
		repository.save(extrato);
	}	
	

	public void criarMovimentoTransferenciaNoExtrato(BigDecimal valor, TransferenciaContas transferencia,
			ContaEmpresa contaEmpresa, boolean credito, String status, LocalDate data, String tipo,
			MovimentacaoItem movimentacaoItem) {
		Extrato extrato = new Extrato();
		extrato.setTransferencia(transferencia);
		extrato.setHistorico("Transferencia nº " + transferencia.getId() +" entre as contas: "+ transferencia.getContaOrigem().getNome()+" e "+ transferencia.getContaDestino().getNome() );
		criarMovimentoNoExtrato(extrato, valor, contaEmpresa, credito, status, data, tipo, movimentacaoItem);
		repository.save(extrato);
	}	
	
	
	public void alterarStatusEMovimentacaoDoExtratoPorPagamento(Pagamento pagamento, String status,
			MovimentacaoItem movimentacaoItem) {
		Extrato extrato = repository.findByPagamento(pagamento);
		extrato.setStatus(status);
		extrato.setMovimentacaoItem(movimentacaoItem);
		repository.save(extrato);
	}	
	
	
	public void excluirPagamentoDoExtrato(Pagamento pagamento) {
		Extrato extrato = repository.findByPagamento(pagamento);
		repository.delete(extrato);
	}	
	

	public Page<Extrato> filtrar(ExtratoFilter extratoFilter, Pageable pageable) {
		return repository.filtrar(extratoFilter, pageable);
	}
	
	
	public Extrato findOne(Long id) {
		return repository.findOne(id);
	}


	public MovimentacaoItem setarMovimentacao(ExtratoFilter extratoFilter) {
		Movimentacao movimento = movRepository.findByEmpresaAndStatus(extratoFilter.getContaEmpresa().getEmpresa());
		MovimentacaoItem movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, extratoFilter.getContaEmpresa());
		return movBancaria;
	}
	
	
	public Extrato findByRecebimento(Recebimento recebimento) {
		return repository.findByRecebimento(recebimento);
	}


	public void excluir(Extrato extrato) {
		repository.delete(extrato);
	}
	
	
	@Transactional
	public void calcularSaldoExtrato(ExtratoFilter extratoFilter) {
		List<Extrato> extratos = repository.findByMovimentacaoItemAndContaBancariaOrderByDataAndId(extratoFilter.getMovimentacao());
		BigDecimal saldoAtual = new BigDecimal(0);

		for (Extrato ex : extratos) {
			ex.setSaldo(BigDecimal.ZERO);
			if (ex.getCredito()) {
				ex.setSaldo(saldoAtual.add(ex.getValor()));
			}else {
				ex.setSaldo(saldoAtual.subtract(ex.getValor()));
			}
			saldoAtual = ex.getSaldo();
			repository.save(ex);
		}
	}
}
