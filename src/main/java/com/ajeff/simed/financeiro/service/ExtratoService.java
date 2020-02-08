package com.ajeff.simed.financeiro.service;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesItensRepository;
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
	private MovimentacoesItensRepository movBancariaRepository;
	@Autowired
	private MovimentacoesRepository movRepository;

	
	public void criarMovimentoNoExtratoPorPagamento(Pagamento pagamento, String status, LocalDate data) {
		Extrato extrato = new Extrato();
		extrato.setData(data);
		extrato.setContaBancaria(pagamento.getContaEmpresa());
		extrato.setCredito(false);
		extrato.setHistorico("Pagamento afetuado por: " + pagamento.getTipo() + " - Documento nº: " + pagamento.getDocumento());
		extrato.setPagamento(pagamento);
		extrato.setStatus(status);
		extrato.setTipo("PAGAMENTO");
		extrato.setMovimentacaoItem(pagamento.getMovimentacaoItem());
		extrato.setValor(pagamento.getValor());
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

//	
//	public void alterarStatusDoExtratoPorPagamento(Pagamento pagamento, String status) {
//		Extrato extrato = repository.findByPagamento(pagamento);
//		extrato.setStatus(status);
//		repository.save(extrato);
//	}	
//	
//	

	public void criarRecebimentoNoExtrato(Recebimento recebimento, String status) {
		Extrato extrato = new Extrato();
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
	

	public Page<Extrato> filtrar(ExtratoFilter extratoFilter, Pageable pageable) {
		return repository.filtrar(extratoFilter, pageable);
	}
	
	
	public Extrato findOne(Long id) {
		return repository.findOne(id);
	}


	public MovimentacaoItem setarMovimentacao(ExtratoFilter extratoFilter) {
		Movimentacao movimento = movRepository.findByEmpresaAndStatus(extratoFilter.getEmpresa());
		MovimentacaoItem movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, extratoFilter.getContaEmpresa());
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


//	@Transactional
//	public void exibirSaldo(ExtratoFilter extratoFilter) {
////		Movimentacao mov = movimentacaoRepository.findByEmpresaAndStatus(extratoFilter.getEmpresa());
////		MovimentacaoBancaria movBancaria = movimentacaoBancariaRepository.findByMovimentacaoAndContaEmpresa(mov, extratoFilter.getContaEmpresa());
//		
//		List<Extrato> movimentacoes = repository.findByMovimentacaoAndContaBancariaOrderByDataAndId(extratoFilter.getMovimentacao(), extratoFilter.getContaEmpresa());
//		movimentacoes.forEach(item->System.out.println(item.getValor()));
//		BigDecimal saldoAtual = new BigDecimal(0);
//
//		for (Extrato ex : movimentacoes) {
//			if(ex.getTipo().equals("SALDO INICIAL")) {
//				saldoAtual = ex.getSaldo();
//			}
//			ex.setSaldo(BigDecimal.ZERO);
//			
//			if (ex.getCredito()) {
//				ex.setSaldo(saldoAtual.add(ex.getValor()));
//			}else {
//				ex.setSaldo(saldoAtual.subtract(ex.getValor()));
//			}
//			saldoAtual = ex.getSaldo();
//			repository.save(ex);
//		}
//		
//		
//////
//////		for (ExtratoBancario extratoBancario : movimentacoes) {
//////			BigDecimal saldo = mov.getTotalInicio();
//////			
//////		}
////		
//	}
//

	public Extrato findByRecebimento(Recebimento recebimento) {
		return repository.findByRecebimento(recebimento);
	}


	public void excluir(Extrato extrato) {
		repository.delete(extrato);
	}
















	

}
