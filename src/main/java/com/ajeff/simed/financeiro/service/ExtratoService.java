package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesBancariasRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;

@Service
public class ExtratoService {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ExtratoService.class);	
	

	@Autowired
	private ExtratosRepository repository;
//	@Autowired
//	private ContaEmpresaRepository contaRepository;
//	@Autowired
//	private PagamentosRepository pagamentoRepository;
//	@Autowired
//	private ContasPagarRepository contaPagarRepository;
	@Autowired
	private MovimentacoesBancariasRepository movBancariaRepository;
	@Autowired
	private MovimentacoesRepository movRepository;
//	
//	
//	@Transactional
//	public void compensar(ExtratoBancario extrato) {
//		
//		if (extrato.getPagamento().getId() != null) {
//			Pagamento pagamento = pagamentoRepository.findOne(extrato.getPagamento().getId());
//			pagamento.setStatus("PAGO");
//			alterarStatusContaPagar(pagamento);
//			extrato.setStatus("COMPENSADO");
//			pagamento.setDataPago(LocalDate.now());
//			pagamentoRepository.save(pagamento);
//			debitarValorContaPendente(pagamento);
//			debitarValorSaldoConta(pagamento);
//		}
//		repository.save(extrato);
//	}	
//
//
//	private void alterarStatusContaPagar(Pagamento pagamento) {
//		List<ContaPagar> contas = contaPagarRepository.findByPagamentoId(pagamento.getId());
//		
//		for (ContaPagar contaPagar : contas) {
//			if(contaPagar.getStatus().equals("EMITIDO")) {
//				contaPagar.setStatus("PAGO");
//			} else {
//				contaPagar.setStatus("EMITIDO");
//			}
//			contaPagarRepository.save(contaPagar);
//		}
//		
//	}
//
//
	public void excluirPagamentoDoExtrato(Pagamento pagamento) {
		ExtratoBancario extrato = repository.findByPagamento(pagamento);
		repository.delete(extrato);
	}	
	
	
	public void alterarStatusDoExtratoPorPagamento(Pagamento pagamento, String status) {
		ExtratoBancario extrato = repository.findByPagamento(pagamento);
		extrato.setStatus(status);
		repository.save(extrato);
	}	
	
	
	public void criarMovimentoNoExtratoPorPagamento(Pagamento pagamento, String status, LocalDate data) {
		ExtratoBancario extrato = new ExtratoBancario();
		extrato.setData(data);
		extrato.setContaBancaria(pagamento.getContaEmpresa());
		extrato.setCredito(false);
		extrato.setHistorico("Pagamento por " + pagamento.getTipo() +" - Documento nº: "+ pagamento.getDocumento());
		extrato.setPagamento(pagamento);
		extrato.setStatus(status);
		extrato.setTipo("PAGAMENTO");
		extrato.setMovimentacao(pagamento.getMovimentacao());
		extrato.setValor(pagamento.getValor());
		repository.save(extrato);
	}	
	
	
	public void criarRecebimentoNoExtrato(Recebimento recebimento, String status) {
		ExtratoBancario extrato = new ExtratoBancario();
		extrato.setData(recebimento.getData());
		extrato.setContaBancaria(recebimento.getContaEmpresa());
		extrato.setCredito(true);
		extrato.setHistorico("Recebimento de: " + recebimento.getContaReceber().getFornecedor().getFantasia() +" - Documento nº: "+ recebimento.getContaReceber().getDocumento());
		extrato.setRecebimento(recebimento);
		extrato.setStatus(status);
		extrato.setTipo("RECEBIMENTO");
//		extrato.setMovimentacao(recebimento.getMovimentacao());
		extrato.setValor(recebimento.getValor());
		repository.save(extrato);
	}	
	
	
//	@Transactional
//	public void cancelarCompensar(ExtratoBancario extrato) {
//
//		if (extrato.getPagamento().getId() != null) {
//			Pagamento pagamento = pagamentoRepository.findOne(extrato.getPagamento().getId());
//			pagamento.setStatus("EMITIDO");
//			extrato.setStatus("ABERTO");
//			alterarStatusContaPagar(pagamento);
//			pagamento.setDataPago(null);
//			pagamentoRepository.save(pagamento);
//			creditarValorContaPendente(pagamento);
//			creditarValorSaldoConta(pagamento);
//		}
//		repository.save(extrato);
//	}
//	
//	
//	private void debitarValorContaPendente(Pagamento pagamento) {
//		ContaEmpresa contaEmpresa = contaRepository.findOne(pagamento.getContaEmpresa().getId());
//		contaEmpresa.setValorPendente(contaEmpresa.getValorPendente().subtract(pagamento.getValor()));
//		contaRepository.save(contaEmpresa);
//	}
//	
//	private void debitarValorSaldoConta(Pagamento pagamento) {
//		ContaEmpresa contaEmpresa = contaRepository.findOne(pagamento.getContaEmpresa().getId());
//		contaEmpresa.setSaldo(contaEmpresa.getSaldo().subtract(pagamento.getValor()));
//		contaRepository.save(contaEmpresa);
//	}
//
//	
//	private void creditarValorContaPendente(Pagamento pagamento) {
//		ContaEmpresa contaEmpresa = contaRepository.findOne(pagamento.getContaEmpresa().getId());
//		contaEmpresa.setValorPendente(contaEmpresa.getValorPendente().add(pagamento.getValor()));
//		contaRepository.save(contaEmpresa);
//	}
//	
//	private void creditarValorSaldoConta(Pagamento pagamento) {
//		ContaEmpresa contaEmpresa = contaRepository.findOne(pagamento.getContaEmpresa().getId());
//		contaEmpresa.setSaldo(contaEmpresa.getSaldo().add(pagamento.getValor()));
//		contaRepository.save(contaEmpresa);
//	}	
//
////	@Transactional
////	public ExtratoBancario salvar(ExtratoBancario extrato) {
////		
////		try {
////			ContaEmpresa conta = contaRepository.findOne(extrato.getContaBancaria().getId());
////
////				if(extrato.getTipo()) {
////					extrato.setSaldo(conta.getSaldo().add(extrato.getValor()));
////					extrato.setData(LocalDate.now());
////					conta.setSaldo(extrato.getSaldo());
////				}else {
////					extrato.setSaldo(conta.getSaldo().subtract(extrato.getValor()));
////					extrato.setData(LocalDate.now());
////					conta.setSaldo(extrato.getSaldo());
////				}
////
////				contaRepository.save(conta);
////
////		} catch (Exception e) {
////			LOG.error(">>>>>ERRO: OCORREU UM ERRO ALTERAR O SALDO DA CONTA BANCÁRIA*****" + e.getMessage());			
////		}
////		
////		extrato.setTipoMovimento("MOVIMENTAÇÃO");
////		return repository.save(extrato);
////	}
	

	public Page<ExtratoBancario> filtrar(ExtratoFilter extratoFilter, Pageable pageable) {
		return repository.filtrar(extratoFilter, pageable);
	}
	
	
	public ExtratoBancario findOne(Long id) {
		return repository.findOne(id);
	}


	public MovimentacaoBancaria setarMovimentacao(ExtratoFilter extratoFilter) {
		Movimentacao movimento = movRepository.findByEmpresaAndStatus(extratoFilter.getEmpresa());
		MovimentacaoBancaria movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, extratoFilter.getContaEmpresa());
		return movBancaria;
	}


////	public void exibirMovimentacoes(ExtratoFilter extratoFilter) {
////
////		List<Movimentacao> movs = mov
////	}
//
//
//	public List<ExtratoBancario> findByMovimentacao(Movimentacao mov) {
//		return repository.findByMovimentacao(mov);
//	}
//
//
//	public List<ExtratoBancario> findByContaBancaria(ContaEmpresa conta) {
//		return repository.findByContaBancaria(conta);
//	}


	@Transactional
	public void exibirSaldo(ExtratoFilter extratoFilter) {
//		Movimentacao mov = movimentacaoRepository.findByEmpresaAndStatus(extratoFilter.getEmpresa());
//		MovimentacaoBancaria movBancaria = movimentacaoBancariaRepository.findByMovimentacaoAndContaEmpresa(mov, extratoFilter.getContaEmpresa());
		
		List<ExtratoBancario> movimentacoes = repository.findByMovimentacaoAndContaBancariaOrderByDataAndId(extratoFilter.getMovimentacao(), extratoFilter.getContaEmpresa());
		movimentacoes.forEach(item->System.out.println(item.getValor()));
		BigDecimal saldoAtual = new BigDecimal(0);

		for (ExtratoBancario ex : movimentacoes) {
			if(ex.getTipo().equals("SALDO INICIAL")) {
				saldoAtual = ex.getSaldo();
			}
			ex.setSaldo(BigDecimal.ZERO);
			
			if (ex.getCredito()) {
				ex.setSaldo(saldoAtual.add(ex.getValor()));
			}else {
				ex.setSaldo(saldoAtual.subtract(ex.getValor()));
			}
			saldoAtual = ex.getSaldo();
			repository.save(ex);
		}
		
		
////
////		for (ExtratoBancario extratoBancario : movimentacoes) {
////			BigDecimal saldo = mov.getTotalInicio();
////			
////		}
//		
	}


	public ExtratoBancario findByRecebimento(Recebimento recebimento) {
		return repository.findByRecebimento(recebimento);
	}


	public void excluir(ExtratoBancario extrato) {
		repository.delete(extrato);
	}

	

}
