package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.PagamentosRepository;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;

@Service
public class PagamentoService {

	private static final Logger LOG = LoggerFactory.getLogger(PagamentoService.class);	
	
	@Autowired
	private PagamentosRepository repository;
	@Autowired
	private ContaPagarService contaPagarService;
	@Autowired
	private ExtratoService extratoService;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private MovimentacaoBancariaService movBancariaService;
	@Autowired
	private MovimentacaoService movimentacaoService;
	
	
	@Transactional
	public void salvar(Pagamento pagamento, List<Long> ids){
		Boolean aberto = movimentacaoService.verificarSeMovimentacaoEstaAberto(pagamento);
		if(!aberto) {
			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto!");
			
		}else {
			Boolean dataForaPeriodo = movimentacaoService.verificarSeDataPagamentoEstaForaPeriodoAberto(pagamento, pagamento.getData());
			
			if(dataForaPeriodo) {
				throw new PeriodoMovimentacaoException("A data de pagamento esta fora do período em aberto!");
			}else {
				emitirPagamento(pagamento, ids);
			}
		}
	}


	private void emitirPagamento(Pagamento pagamento, List<Long> ids) {
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		if (pagamento.getUnico()) {
				emitirPagamentoAgrupado(pagamento, ids, tipoCheque);
			}else {
				emitirPagamentoIndividual(pagamento, ids, tipoCheque);
			}
	}	
	
	
	private void emitirPagamentoAgrupado(Pagamento pagamento, List<Long> ids, Boolean tipoCheque) {
		
		try {
			List<ContaPagar> contasAutorizadas = contaPagarService.buscarContasPagarSelecionadas(ids);
			BigDecimal total = new BigDecimal(0);
			pagamento.setMovimentacao(setarMovimentacao(pagamento, tipoCheque));

			for(ContaPagar conta : contasAutorizadas ) {
				total = total.add(conta.getValor());
				alterarDadosContaPagar("EMITIDO",conta, pagamento);
			}
			
			pagamento.setValor(total);
			pagamento.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
			pagamento.setStatus("EMITIDO");
			pagamento.setFechado(false);
			pagamento.setDocumento(setarNumeroDocumento(pagamento, tipoCheque));
			comportamentoPorTipo(pagamento, tipoCheque);
			repository.save(pagamento);
		} catch (PersistenceException e) {
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}

	
	private MovimentacaoBancaria setarMovimentacao(Pagamento pagamento, Boolean tipoCheque) {
		if(tipoCheque) {
			return null;
		}else {
			Movimentacao movimento = movimentacaoService.findByEmpresaAndStatus(pagamento.getContaEmpresa().getEmpresa());
			MovimentacaoBancaria movBancaria = movBancariaService.findByMovimentacaoAndContaEmpresa(movimento, pagamento.getContaEmpresa());
			return movBancaria;
		}
	}
	
	
	private String setarNumeroDocumento(Pagamento pagamento, Boolean tipoCheque) {
		if(tipoCheque) {
			return setarNumeroCheque(pagamento);
		}else {
			return numeroDocumentoPagamentos(pagamento);
		}
	}
	
	
	private String numeroDocumentoPagamentos(Pagamento pagamento) {
		try {
			Long codigo = empresaService.incrementarCodigoPagamento(pagamento.getEmpresa().getId());
			codigo --;
			return "PGTO:" + codigo.toString();
		} catch (PersistenceException e) {
			e.getStackTrace();
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Erro ao inserir o numero do documento do pagamento!");
		}
	}		
	
	
	private String setarNumeroCheque(Pagamento pagamento) {
		Integer cheque = pagamento.getNumCheque();
		String novoDocumento = cheque.toString();
		cheque++;
		pagamento.setNumCheque(cheque);
		return "CH:" + novoDocumento;
	}	

	
	private void comportamentoPorTipo(Pagamento pagamento, Boolean tipoCheque) {
		if(tipoCheque) {
			creditarChequePendenteEmContaEmpresa(pagamento);
		}else {
			criarPagamentoNoExtrato(pagamento);
			debitarPagamentoNoDebitoDoMovimento(pagamento);
		}
	}		

	
	private void creditarChequePendenteEmContaEmpresa(Pagamento pagamento) {
		try {
			contaEmpresaService.creditarValorNoTotalPendencias(pagamento.getEmpresa().getId(), pagamento.getValor());
		} catch (PersistenceException e) {
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Não foi possível atualizar o valor de cheques pendentes!");
		}
	}		

	
	public List<ContaPagar> listaDeContasSelecionadasParaPagamento(List<Long> ids) {
		List<ContaPagar> itens = contaPagarService.buscarContasPagarSelecionadas(ids);
		return itens;
	}		

	
	private void emitirPagamentoIndividual(Pagamento pagamento, List<Long> ids, Boolean tipoCheque) {
		List<ContaPagar> contasAutorizadas = contaPagarService.buscarContasPagarSelecionadas(ids);
		try {
			for(ContaPagar conta : contasAutorizadas ) {
				Pagamento pgto = registroUnico(pagamento, conta, tipoCheque);
				alterarDadosContaPagar("EMITIDO", conta, pgto);
				comportamentoPorTipo(pgto, tipoCheque);
				repository.save(pgto);
			}
		} catch (PagamentoNaoEfetuadoException e) {
			throw new PagamentoNaoEfetuadoException("Ocorreu um erro e o pagamento não foi efetuado!" );
		}
	}

	
	private void criarPagamentoNoExtrato(Pagamento pagamento) {
		try {
//			if(pagamento.isNovo()) {
				criarMovimentoNoExtrato(pagamento, "ABERTO", pagamento.getData());
//			}else {
//				criarMovimentoNoExtrato(pagamento, "CONFERIDO", pagamento.getDataPago());
//			}
		} catch (PersistenceException e) {
			e.getStackTrace();
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Não foi possível incluir o movimento no extrato!");
		}
	}
	
	

	private void creditarPagamentoNoDebitoDoMovimento(Pagamento pagamento) {
		try {
			movBancariaService.creditarValorNosDebitosDoMovimento(pagamento.getMovimentacao().getId(), pagamento.getValor());
		} catch (PersistenceException e) {
			e.getStackTrace();
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Erro ao incluir o debito no movimento bancário");
		}
	}
	
	
	private void criarMovimentoNoExtrato(Pagamento pagamento, String status, LocalDate data) {
			extratoService.criarMovimentoNoExtratoPorPagamento(pagamento, status, data);
	}
	
	
	private void alterarDadosContaPagar(String status, ContaPagar conta, Pagamento pgto) {
		conta.setStatus(status);
		conta.setPagamento(pgto);
	}


	private Pagamento registroUnico(Pagamento pagamento, ContaPagar conta, Boolean tipoCheque) {
		Pagamento pgto = new Pagamento(); 
		pgto.setValor(conta.getValor());
		pgto.setContaEmpresa(pagamento.getContaEmpresa());
		pgto.setStatus("EMITIDO");
		pgto.setData(pagamento.getData());
		pgto.setTipo(pagamento.getTipo());
		pgto.setFechado(false);
		pgto.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
		pgto.setMovimentacao(setarMovimentacao(pagamento, tipoCheque));
		pgto.setDocumento(setarNumeroDocumento(pagamento, tipoCheque));
		return pgto;
	}	
	

	private Boolean verificarTipoPagtoCheque(Pagamento pgto) {
		return pgto.getTipo().equals("CHEQUE") ? true: false; 
	}

	
	@Transactional
	public void excluir(Long id) {
		Pagamento pagamento = repository.findOne(id);
//		Boolean fechado = verificarSeMovimentacaoEstaFechado(pagamento);
//		if(fechado) {
//			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
//		}else {
//			try {
//				
//				List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
//				for(ContaPagar conta : itens ) {
//					alterarDadosContaPagar("AUTORIZADO", conta, null);			
//				}
//			
				Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
				if(tipoCheque) {
					cancelarLancamentoChequePendenteEmContaEmpresa(pagamento);					
				}else {
//					excluirMovimentoDoExtrato(pagamento);
				debitarPagamentoNoDebitoDoMovimento(pagamento);
				}
//				repository.delete(id);
//				repository.flush();
//			} catch (ImpossivelExcluirEntidade e) {
//				throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. \nPeríodo de movimentação fechado!"); 
//			} catch (PersistenceException e) {
//				throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. \nExclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//			}
//		}
	}	
	
	
	private void debitarPagamentoNoDebitoDoMovimento(Pagamento pagamento) {
		try {
			movBancariaService.debitarValorNosDebitosDoMovimento(pagamento.getMovimentacao().getId(), pagamento.getValor());
		} catch (PersistenceException e) {
			e.getStackTrace();
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Erro ao excluir o debito no movimento bancário");
		}
	}
	
	
	private void cancelarLancamentoChequePendenteEmContaEmpresa(Pagamento pagamento) {
		try {
			contaEmpresaService.debitarValorNoTotalPendencias(pagamento.getEmpresa().getId(), pagamento.getValor());
		} catch (PersistenceException e) {
			e.getStackTrace();
			throw new PagamentoNaoEfetuadoException("Erro ao cancelar o lançamento do valor do pagamento nos cheques pendentes do movimento!");
		}
	}	
		
	
	private void excluiPagamentoDoExtrato(Pagamento pagamento) {
		extratoService.excluirPagamentoDoExtrato(pagamento);
	}


	@Transactional
	public void pagar(Pagamento pagamento) {
//		List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
//		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
//		
//		if(!tipoCheque) {
//			pagamento.setDataPago(pagamento.getData());
//		}
//		
//		Boolean fechado = verificarSeMovimentacaoEstaFechadoNaConfirmacao(pagamento);
//
//		if(fechado) {
//			LOG.error("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
//			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
//		}else {
//			try {
//				if(tipoCheque){
//					confirmarPagamentoCheques(pagamento);
//				}else {
//					alterarExtratoConfirmacaoPagamentoDebito(pagamento, "CONFERIDO");
//				}
//				for (ContaPagar conta : itens) {
//					alterarDadosContaPagar("PAGO",conta, pagamento);
//				}
//				pagamento.setStatus("PAGO");
//				repository.save(pagamento);
//			} catch (Exception e) {
//				LOG.error("Algo deu errado!! Fechamento não efetuado. Erro ao fechar o mês no método principal");
//				throw new PagamentoNaoEfetuadoException("Algo deu errado!! Fechamento não efetuado");
//			}
//		}
	}
	
	
	private void alterarStatusDoExtratoPorPagamento(Pagamento pagamento, String status) {
		extratoService.alterarStatusDoExtratoPorPagamento(pagamento, status);
	}


	private void confirmarPagamentoCheques(Pagamento pagamento) {
		Movimentacao mov = movimentacaoService.findByEmpresaAndStatus(pagamento.getContaEmpresa().getEmpresa());	
		MovimentacaoBancaria movBancario = movBancariaService.findByMovimentacaoAndContaEmpresa(mov, pagamento.getContaEmpresa());
		pagamento.setMovimentacao(movBancario);
		cancelarLancamentoChequePendenteEmContaEmpresa(pagamento);
		criarPagamentoNoExtrato(pagamento);
		creditarPagamentoNoDebitoDoMovimento(pagamento);
		verificarSeDataPagoMenorDataEmissao(pagamento);
	}

	
	private void verificarSeDataPagoMenorDataEmissao(Pagamento pagamento) {
		if(pagamento.getDataPago().isBefore(pagamento.getData())) {
			LOG.error("Algo deu errado!! A data de pagamento não pode ser menor que a data de emissão");
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Fechamento não efetuado");
		}
	}
	
	
	@Transactional
	public void cancelarConfirmacao(Pagamento pagamento) {
		List<ContaPagar> itens = contaPagarService.findByPagamentoId(pagamento.getId());
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		
		for (ContaPagar conta : itens) {
			alterarDadosContaPagar("EMITIDO",conta, pagamento);
		}
		
		if(tipoCheque) {
			cancelarConfirmacaoPagamentoCheques(pagamento);
		}else {
			alterarStatusDoExtratoPorPagamento(pagamento, "ABERTO");			
		}
		pagamento.setStatus("EMITIDO");
		pagamento.setDataPago(null);
		repository.save(pagamento);
	}	


	private void cancelarConfirmacaoPagamentoCheques(Pagamento pagamento) {
		debitarPagamentoNoDebitoDoMovimento(pagamento);
		pagamento.setMovimentacao(null);
		creditarChequePendenteEmContaEmpresa(pagamento);
		excluiPagamentoDoExtrato(pagamento);
	}

	public Pagamento findOne(Long id) {
		return repository.findOne(id);
	}

	
	public Page<Pagamento> filtrar(PagamentoFilter pagamentoFilter, Pageable pageable) {
		return repository.filtrar(pagamentoFilter, pageable);
	}
	
	
	public BigDecimal totalGeral(PagamentoFilter pagamentoFilter) {
		return repository.totalGeral(pagamentoFilter);
	}	

//	private void alterarStatusContaPagarAposExcluir(Pagamento pagamento) {
//	List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
//	try {
//		for(ContaPagar conta : itens ) {
//			conta.setStatus("AUTORIZADO");
//			conta.setPagamento(null);
//			contaRepository.save(conta);
//		}
//	} catch (Exception e) {
//		LOG.error(">>>>>>>>OCORREU UM ERRO AO ALTERAR O STATUS DAS CONTAS A PAGAR APÓS EXCLUSÃO DA ORDEM PAGAMENTO: " + e.getMessage());
//	}
//}		
	
	
//	@Transactional
//	public void excluir(Long id) {
//		Pagamento pagamento = repository.findOne(id);
//		ExtratoBancario extrato = extratoRepository.findByPagamento(pagamento);
//		ContaEmpresa contabanco = contaEmpresaRepository.findOne(pagamento.getContaEmpresa().getId());
//		Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(contabanco.getEmpresa());
//		
//		try {
//			if(!mov.isPresent()) {
//				throw new PeriodoMovimentacaoException("Não existe nenhuma movimentação em aberto para esta empresa!");
//			}else if (pagamento.getData().isBefore(mov.get().getDataInicio()) || pagamento.getData().isAfter(mov.get().getDataFinal())) {
//				throw new PeriodoMovimentacaoException("Data do pagamento fora do periodo da movimentação em aberto");
//					
//			}else {
//				alterarStatusContaPagarAposExcluir(pagamento);	
//				
//				if (pagamento.getTipo().equals(TipoPagamento.CHEQUE)) {
//					debitarValorContaPendente(pagamento);
//				}else {
//					creditarPagamentoMovimento(pagamento);
//					extratoRepository.delete(extrato);
//				}
//				repository.delete(id);
//				repository.flush();
//			}	
//		} catch (ImpossivelExcluirEntidade e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. Período de movimentação fechado!"); 
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//		}
//	}
//	private void emitirPagamentoIndividual(Pagamento pagamento, List<Long> ids) {
//		List<ContaPagar> contasAutorizadas = contaRepository.buscarContasPagarSelecionadas(ids);
//		BigDecimal cheque = pagamento.getNumCheque();
//			
//		try {
//			for(ContaPagar conta : contasAutorizadas ) {
//				
//				Pagamento pgto = new Pagamento(); 
//				pgto.setValor(conta.getValor());
//				pgto.setContaEmpresa(pagamento.getContaEmpresa());
//				pgto.setStatus("EMITIDO");
//				pgto.setData(pagamento.getData());
//				pgto.setTipo(pagamento.getTipo());
//				pgto.setSituacao(false);
//				pgto.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
//				pgto.setMovimentacao(pagamento.getMovimentacao());
//				conta.setStatus("EMITIDO");
//				conta.setPagamento(pgto);
//
//				if (pagamento.getTipo().equals(TipoPagamento.CHEQUE)) {
//					pgto.setDocumento(cheque.toString());
//					cheque = cheque.add(BigDecimal.ONE);
//				}else {
//					setarNumeroDocumento(pgto);
//				}
//				
//				if (pgto.getTipo().equals(TipoPagamento.CHEQUE)){
//					creditarValorContaPendente(pgto);
//				}else {
//					criarMovimentoNoExtrato(pgto, "ABERTO", pgto.getData());
//					debitarPagamentoMovimento(pgto);
//				}
//				repository.save(pgto);
//			}
//		} catch (PagamentoNaoEfetuadoException e) {
//			throw new PagamentoNaoEfetuadoException("Ocorreu um erro e o pagamento não foi efetuado!" );
//		}
//		
//	}

//	private BigDecimal salvarItensDePagamento(Pagamento pagamento, List<Long> ids) {
//	List<ContaPagar> contasAutorizadas = contaRepository.buscarContasPagarSelecionadas(ids);
//	List<ContaPagar> contas = new ArrayList<>();
//	BigDecimal total = new BigDecimal(0);
//	for(ContaPagar conta : contasAutorizadas) {
//		conta.setStatus("EMITIDO");
//			conta.setPagamento(pagamento);
//		total = total.add(conta.getValor());
//		contas.add(conta);
//	}
//	contaRepository.save(contas);
//	return total;
//}	
	
	
//	private void emitirPagamentoAgrupado(Pagamento pagamento, List<Long> ids) {
//
//		try {
//			pagamento.setValor(salvarItensDePagamento(pagamento, ids));
//			pagamento.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
//			pagamento.setStatus("EMITIDO");
//			pagamento.setSituacao(false);
//			setarNumeroDocumento(pagamento);
//			
//			
//			if (pagamento.getTipo().equals(TipoPagamento.CHEQUE)){
//				creditarValorContaPendente(pagamento);
//			}else {
//				criarMovimentoNoExtrato(pagamento, "ABERTO", pagamento.getData());
//				debitarPagamentoMovimento(pagamento);
//			}
//			
//			repository.save(pagamento);
//		} catch (Exception e) {
//			LOG.error("Pagamento não foi efetuado!--"+e.getMessage());
//			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
//		}
//		
//	}
//

//
//	
//	private void debitarValorContaPendente(Pagamento pagamento) {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(pagamento.getMovimentacao().getId());
//		//RETIRAR DE CHEQUES PENDENTES E DEBITAR NO SALDO DO PERIODO
//		movBanco.setTotalFinal(movBanco.getTotalFinal().subtract(pagamento.getValor()));
//		movBanco.setTotalPendente(movBanco.getTotalPendente().subtract(pagamento.getValor()));
//		
//		ContaEmpresa conta = contaEmpresaRepository.findOne(pagamento.getContaEmpresa().getId());
//		conta.setValorPendente(conta.getValorPendente().subtract(pagamento.getValor()));
//		contaEmpresaRepository.save(conta);
//		movBancariaRepository.save(movBanco);
//	}
//	
//	
//

//
//
//
//
//	private void setarNumeroDocumento(Pagamento pagamento) {
//		if(pagamento.getTipo().equals(TipoPagamento.CHEQUE)) {
//			pagamento.setDocumento(numeroDocumentoPagamentosCheque(pagamento));
//		}else {
//			pagamento.setDocumento(numeroDocumentoPagamentos(pagamento).toString());
//		}
//	}
//

//
//	private BigDecimal salvarItensDePagamento(Pagamento pagamento, List<Long> ids) {
//		List<ContaPagar> contasAutorizadas = contaRepository.buscarContasPagarSelecionadas(ids);
//		List<ContaPagar> contas = new ArrayList<>();
//		BigDecimal total = new BigDecimal(0);
//		for(ContaPagar conta : contasAutorizadas) {
//			conta.setStatus("EMITIDO");
// 			conta.setPagamento(pagamento);
//			total = total.add(conta.getValor());
//			contas.add(conta);
//		}
//		contaRepository.save(contas);
//		return total;
//	}
//	
//	
//	
//	private String numeroDocumentoPagamentosCheque(Pagamento pagamento) {
//		String documento = new String();
//		documento = pagamento.getNumCheque().toString();
//		return documento;
//	}	
//
//	
//	

//
//

//
//
//	
//	//ALTERAR SALDO E VALOR PENDENTE DA CONTA EMPRESA APÓS A EXCLUSÃO DO PAGAMENTO
//	/*
//	 * Saldo atual + valor do pagamento
//	 * Valor Pendente - valor do pagamento
//	 */
////	private void alterarSaldoEValorPendenteContaEmpresaAposExcluir(Pagamento pagamento) {
////		try {
////			ContaEmpresa contaEmpresa = contaEmpresaRepository.findOne(pagamento.getContaEmpresa().getId());
////			contaEmpresa.setSaldo(contaEmpresa.getSaldo().add(pagamento.getValor()));
////			contaEmpresa.setValorPendente(contaEmpresa.getValorPendente().subtract(pagamento.getValor()));
////			contaEmpresaRepository.save(contaEmpresa);
////		} catch (Exception e) {
////			LOG.error(">>>>>>>>OCORREU UM ERRO AO O SALDO DA CONTA EMPRESA APOS EXCLUIR ORDEM PAGAMENTO: " + e.getMessage());
////		}
////		
////	}
//
//	
//	

//	
//
//
//	//Somente quando cheque
//	private void cancelarMovimentoNoExtrato(Pagamento pagamento) {
//		try {
//			ExtratoBancario extrato = extratoRepository.findByPagamento(pagamento);
//			extratoRepository.delete(extrato);
//		} catch (Exception e) {
//			LOG.error("Ocorreu um erro ao CANCELAR o movimento no extrato.....Erro: " + e.getMessage());
//		}
//	}
//
//
//
//
//	
//	
////	private void alterarStatusContaPagar(Pagamento pagamento) {
////		List<PagamentoItem> itens = itensRepository.findByPagamentoId(pagamento.getId());
////
////		try {
////			if(pagamento.getStatus().equals("PAGO")) {
////				for (PagamentoItem i : itens ) {
////					ContaPagar p = contaService.findOne(i.getContaPagar().getId());
////					p.setStatus("PAGO");
////					p.setDataPago(pagamento.getDataPago());
////					p.setAnotacaoPagamento("Pagamento nº " + pagamento.getId() +" - Documento: "+ pagamento.getDocumento() 
////					+" - Conta nº "+ pagamento.getContaEmpresa().getConta()+" - "+ pagamento.getTipo().getDescricao());
////					contaService.salvar(p);
////				}
////			}else {
////				for (PagamentoItem i : itens ) {
////					ContaPagar p = contaService.findOne(i.getContaPagar().getId());
////					p.setStatus("EMITIDO");
////					p.setDataPago(null);
////					p.setAnotacaoPagamento("");
////					contaService.salvar(p);
////				}
////			}
////		} catch (Exception e) {
////			LOG.error(">>>>>ERRO: OCORREU UM ERRO AO ALTERAR O(s) STATUS DA(s) CONTA(s) A PAGAR ");
////		}
////	}	
//	

//
//	public List<Pagamento> findAll(List<Long> ids) {
//		
//		return repository.findAll(ids);
//	}
//
//
//
//	@Transactional
//	public void pagar(Pagamento pagamento) {
//
//		try {
//			if(pagamento.getTipo().equals(TipoPagamento.CHEQUE)) {
//				debitarValorContaPendente(pagamento);
//				MovimentacaoBancaria movBancario = movBancariaRepository.findOne(pagamento.getMovimentacao().getId());
//				pagamento.setMovimentacao(movBancario);
//				criarMovimentoNoExtrato(pagamento, "CONFERIDO", pagamento.getDataPago());
//			}else {
//				pagamento.setDataPago(pagamento.getData());
//				ExtratoBancario extrato = extratoRepository.findByPagamento(pagamento);
//				extrato.setStatus("CONFERIDO");
//				extratoRepository.save(extrato);
//			}
//			pagamento.setStatus("PAGO");
//			List<ContaPagar> contas = contaRepository.findByPagamentoId(pagamento.getId());
//			for (ContaPagar c : contas) {
//				c.setStatus("PAGO");
//				contaRepository.save(c);
//			}
//			repository.save(pagamento);
//		} catch (Exception e) {
//			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
//		}
//	}
//
//
	


//	@Transactional
//	public void cancelarOuPagar(Pagamento pagamento) {
//		//TODO: Implementar modal para informar a data do pagamento
//		
//		if(pagamento.isPago()) {
//			pagamento.setStatus("EMITIDO");
//			pagamento.setDataPago(null);
//			cancelarMovimentoNoExtrato(pagamento);
//			creditarValorFaltaCompensarEmContaBanco(pagamento);
//		}else {
//			pagamento.setStatus("PAGO");
//			pagamento.setDataPago(pagamento.getData());
//			criarMovimentoNoExtrato(pagamento);
//			debitarValorFaltaCompensarEmContaBanco(pagamento);
//		}
//
//		alterarStatusContaPagar(pagamento);
//		
//		repository.save(pagamento);
//	}




//	public List<ContaPagarContaXpagamento> contaPagarXpagamentoMes(Usuario usuario) {
//		return repository.contaPagarXpagamentoMes(usuario);
//	}
//
//	public List<ContaPagarContaXpagamento> contaPagarXpagamentoAno(Usuario usuario) {
//		return repository.contaPagarXpagamentoAno(usuario);
//	}
//	
//
//	public List<PagamentoSemestre> totalPagamentoSemestre(Usuario usuario) {
//		return repository.totalPagamentoSemestre(usuario);
//	}	
//	
//
//	
//	
//	private void emitirPagamentoTipoTransferencia(Pagamento pagamento, List<Long> ids) {
//		if (pagamento.getUnico()) {
//			emitirPagamentoAgrupado(pagamento, ids);
//		}else {
//			emitirPagamentoIndividual(pagamento, ids);
//		}
//	}
//
	
	
	
//
//	
//	private void creditarPagamentoMovimento(Pagamento pagamento) {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(pagamento.getMovimentacao().getId());
//		movBanco.setTotalFinal(movBanco.getTotalFinal().add(pagamento.getValor()));
//		movBancariaRepository.save(movBanco);
//	}
//
//	
//

	
	
//	private void emitirPagamentoTipoCheque(Pagamento pagamento, List<Long> ids) {
//		try {
//			if (pagamento.getUnico()) {
////				emitirPagamentoAgrupado(pagamento, ids);
//			}else {
//				emitirPagamentoIndividual(pagamento, ids);
//			}
//		} catch (NullPointerException e) {
//			LOG.error("Pagamento não efetuado pois o numero do cheque esta vazio--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//			throw new PagamentoNaoEfetuadoException("Informar o número do cheque");
//		} catch (Exception e) {
//			LOG.error("Pagamento não efetuado--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
//		}
//		
//	}	

	
	

}
