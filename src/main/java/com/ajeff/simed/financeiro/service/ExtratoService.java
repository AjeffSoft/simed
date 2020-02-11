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

	
	public void criarMovimentoNoExtrato(BigDecimal valor, Pagamento pagamento, ContaEmpresa conta, Boolean credito, String status, LocalDate data, String tipo, MovimentacaoItem movimentacaoItem) {
		Extrato extrato = new Extrato();
		extrato.setData(data);
		extrato.setContaBancaria(conta);
		extrato.setCredito(credito);
		extrato.setTipo(tipo);
		extrato.setStatus(status);
		extrato.setMovimentacaoItem(movimentacaoItem);
		extrato.setValor(valor);
		criarMovimentacaoPorTipo(tipo, extrato, pagamento);
		repository.save(extrato);
	}

	private void criarMovimentacaoPorTipo(String tipo, Extrato extrato, Pagamento pagamento) {
		if(tipo.equals("PAGAMENTO")) {
			movimentacaoExtratoPagamento(extrato, pagamento);
		}else if (tipo.equals("SALDO INICIAL")) {
			movimentacaoExtratoSaldoInicial(extrato);
		}
	}
	

	private void movimentacaoExtratoSaldoInicial(Extrato extrato) {
		extrato.setHistorico("Saldo Inicial da Movimentacao nº: " + extrato.getMovimentacaoItem().getMovimentacao().getId());
	}

	
	private void movimentacaoExtratoPagamento(Extrato extrato, Pagamento pagamento) {
		extrato.setPagamento(pagamento);
		extrato.setHistorico("Pagamento afetuado por: " + pagamento.getTipo() + " - Documento nº: " + pagamento.getDocumento());
	}
	
	
	public void criarRecebimentoNoExtrato(Recebimento recebimento, String status) {
		Extrato extrato = new Extrato();
		extrato.setData(recebimento.getData());
		extrato.setContaBancaria(recebimento.getContaEmpresa());
		extrato.setCredito(true);
		extrato.setHistorico("Recebimento de: " + recebimento.getContaReceber().getFornecedor().getFantasia() +" - Documento nº: "+ recebimento.getContaReceber().getDocumento());
		extrato.setRecebimento(recebimento);
		extrato.setStatus(status);
		extrato.setTipo("RECEBIMENTO");
		extrato.setMovimentacaoItem(recebimento.getMovimentacaoItem());
		extrato.setValor(recebimento.getValor());
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
			if (ex.getCredito()) {
				ex.setSaldo(saldoAtual.add(ex.getValor()));
			}else {
				ex.setSaldo(saldoAtual.subtract(ex.getValor()));
			}
			saldoAtual = saldoAtual.add(ex.getSaldo());
			repository.save(ex);
		}
	}
}
