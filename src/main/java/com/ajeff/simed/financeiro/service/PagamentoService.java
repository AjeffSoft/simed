package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesBancariasRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.PagamentosRepository;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.RecebimentoNaoEfetuadoException;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.ContaEmpresaRepository;
import com.ajeff.simed.geral.repository.EmpresasRepository;

@Service
public class PagamentoService {

	private static final Logger LOG = LoggerFactory.getLogger(PagamentoService.class);	
	
	@Autowired
	private PagamentosRepository repository;
	@Autowired
	private ContasPagarRepository contaRepository;
	@Autowired
	private ExtratosRepository extratoRepository;
	@Autowired
	private ContaEmpresaRepository contaEmpresaRepository;
	@Autowired
	private EmpresasRepository empresaRepository;
	@Autowired
	private MovimentacoesRepository movRepository;
	@Autowired
	private MovimentacoesBancariasRepository movBancariaRepository;
	
	
	
	@Transactional
	public void salvar(Pagamento pagamento, List<Long> ids){
		Boolean fechado = verificarSeMovimentacaoEstaFechado(pagamento);
		if(fechado) {
			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
		}else {
			try {
				Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
				if(tipoCheque) {
					emitirPagamentoTipoCheque(pagamento, ids);
				}else {
					emitirPagamento(pagamento, ids);
				}
			} catch (PersistenceException e) {
				throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
			} catch (Exception e) {
				throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
			}
		}	
	}


	private Boolean verificarSeMovimentacaoEstaFechado(Pagamento pagamento) {
		//Busca pela movimentação da conta empresa que foi selecionada
		Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(pagamento.getContaEmpresa().getEmpresa());	
		if(!mov.isPresent()) {
			return true;
		}else if (pagamento.getData().isBefore(mov.get().getDataInicio()) || pagamento.getData().isAfter(mov.get().getDataFinal())) {
			return true;
		}else {
			return false;
		}	
	}

	
	private Boolean verificarSeMovimentacaoEstaFechadoNaConfirmacao(Pagamento pagamento) {
		Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(pagamento.getContaEmpresa().getEmpresa());	
		if(!mov.isPresent()) {
			return true;
		}else if (pagamento.getDataPago().isBefore(mov.get().getDataInicio()) || pagamento.getDataPago().isAfter(mov.get().getDataFinal())) {
			return true;
		}else {
			return false;
		}	
	}
	
	
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
	public List<ContaPagar> listaDeContasSelecionadasParaPagamento(List<Long> ids) {
		List<ContaPagar> itens = contaRepository.buscarContasPagarSelecionadas(ids);
		return itens;
	}		
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

	private void emitirPagamento(Pagamento pagamento, List<Long> ids) {
		if (pagamento.getUnico()) {
				emitirPagamentoAgrupado(pagamento, ids);
			}else {
				emitirPagamentoIndividual(pagamento, ids);
			}
	}
	
	
	private void emitirPagamentoTipoCheque(Pagamento pagamento, List<Long> ids) {
		try {
			if (pagamento.getUnico()) {
				emitirPagamentoAgrupado(pagamento, ids);
			}else {
				emitirPagamentoIndividual(pagamento, ids);
			}
		} catch (NullPointerException e) {
			LOG.error("Pagamento não efetuado pois o numero do cheque esta vazio--" + e.getMessage()+"/"+e.getLocalizedMessage());	
			throw new PagamentoNaoEfetuadoException("Informar o número do cheque");
		} catch (Exception e) {
			LOG.error("Pagamento não efetuado--" + e.getMessage()+"/"+e.getLocalizedMessage());	
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
		
	}	


	private void emitirPagamentoIndividual(Pagamento pagamento, List<Long> ids) {
		List<ContaPagar> contasAutorizadas = contaRepository.buscarContasPagarSelecionadas(ids);
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		try {
			for(ContaPagar conta : contasAutorizadas ) {
				Pagamento pgto = registroUnico(pagamento, conta);
				alterarDadosContaPagar("EMITIDO", conta, pgto);
				
				if(tipoCheque) {
					creditarChequePendenteEmContaEmpresa(pgto);
				}else {
					criarPagamentoNoExtrato(pgto);
					debitarPagamentoMovimento(pgto);
					
				}
				
				repository.save(pgto);
			}
		} catch (PagamentoNaoEfetuadoException e) {
			throw new PagamentoNaoEfetuadoException("Ocorreu um erro e o pagamento não foi efetuado!" );
		}
	}

	
	private void emitirPagamentoAgrupado(Pagamento pagamento, List<Long> ids) {
		List<ContaPagar> contasAutorizadas = contaRepository.buscarContasPagarSelecionadas(ids);
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		BigDecimal total = new BigDecimal(0);
		
		pagamento.setMovimentacao(setarMovimentacao(pagamento));
		
		try {
			for(ContaPagar conta : contasAutorizadas ) {
				total = total.add(conta.getValor());
				alterarDadosContaPagar("EMITIDO",conta, pagamento);
			}
			pagamento.setValor(total);
			pagamento.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
			pagamento.setStatus("EMITIDO");
			pagamento.setFechado(false);
			pagamento.setDocumento(setarNumeroDocumento(pagamento));
			
			if(tipoCheque) {
				creditarChequePendenteEmContaEmpresa(pagamento);
			}else {
				criarPagamentoNoExtrato(pagamento);
				debitarPagamentoMovimento(pagamento);
			}
			
			repository.save(pagamento);
		} catch (Exception e) {
			LOG.error("Pagamento não foi efetuado!--"+e.getMessage());
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}	

	
	private void criarPagamentoNoExtrato(Pagamento pagamento) {
		
		try {
			if(pagamento.isNovo()) {
				criarMovimentoNoExtrato(pagamento, "ABERTO", pagamento.getData());
			}else {
				criarMovimentoNoExtrato(pagamento, "CONFERIDO", pagamento.getDataPago());
			}
		} catch (Exception e) {
			LOG.error("Pagamento não foi efetuado. Erro ao criar o pagamento no extrato!--"+e.getMessage());
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}
	
	
	private void creditarChequePendenteEmContaEmpresa(Pagamento pagamento) {
		try {
			ContaEmpresa conta = contaEmpresaRepository.findOne(pagamento.getContaEmpresa().getId());
			conta.setValorPendente(conta.getValorPendente().add(pagamento.getValor()));
			contaEmpresaRepository.save(conta);
		} catch (Exception e) {
			LOG.error("Pagamento não foi efetuado. Erro ao incluir o valor do pagamento nos cheques pendentes do movimento!--"+e.getMessage());
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}	
	

	private void debitarPagamentoMovimento(Pagamento pagamento) {
		try {
			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(pagamento.getMovimentacao().getId());
			movBanco.setValorDebitos(movBanco.getValorDebitos().add(pagamento.getValor()));
			movBancariaRepository.save(movBanco);
		} catch (Exception e) {
			LOG.error("Pagamento não efetuado. Erro ao incluir o debito no movimento bancário--" + e.getMessage()+"/"+e.getLocalizedMessage());	
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}
	
	
	private void criarMovimentoNoExtrato(Pagamento pagamento, String status, LocalDate data) {
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
			extratoRepository.save(extrato);
	}
	

	private MovimentacaoBancaria setarMovimentacao(Pagamento pagamento) {
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		if(tipoCheque) {
			return null;
		}else {
			Movimentacao movimento = movRepository.findByEmpresaAndStatus(pagamento.getContaEmpresa().getEmpresa());
			MovimentacaoBancaria movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, pagamento.getContaEmpresa());
			return movBancaria;
		}
	}	
	
	
	private void alterarDadosContaPagar(String status, ContaPagar conta, Pagamento pgto) {
		conta.setStatus(status);
		conta.setPagamento(pgto);
	}


	private Pagamento registroUnico(Pagamento pagamento, ContaPagar conta) {
		Pagamento pgto = new Pagamento(); 
		pgto.setValor(conta.getValor());
		pgto.setContaEmpresa(pagamento.getContaEmpresa());
		pgto.setStatus("EMITIDO");
		pgto.setData(pagamento.getData());
		pgto.setTipo(pagamento.getTipo());
		pgto.setFechado(false);
		pgto.setEmpresa(pagamento.getContaEmpresa().getEmpresa());
		pgto.setMovimentacao(setarMovimentacao(pagamento));
		pgto.setDocumento(setarNumeroDocumento(pagamento));
		return pgto;
	}	
	
	
	private String setarNumeroDocumento(Pagamento pagamento) {
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		if(tipoCheque) {
			return setarNumeroCheque(pagamento);
		}else {
			return numeroDocumentoPagamentos(pagamento);
		}
			
	}

	
	private String numeroDocumentoPagamentos(Pagamento pagamento) {
		Empresa empresa = empresaRepository.findOne(pagamento.getContaEmpresa().getEmpresa().getId());
		Long codigo = empresa.getCodigoPagamento();
		codigo ++;
		empresa.setCodigoPagamento(codigo);
		empresaRepository.save(empresa);
		return codigo.toString();
	}	
	

	private String setarNumeroCheque(Pagamento pagamento) {
		Integer cheque = pagamento.getNumCheque();
		String novoDocumento = cheque.toString();
		cheque++;
		pagamento.setNumCheque(cheque);
		return novoDocumento;
	}


	private Boolean verificarTipoPagtoCheque(Pagamento pgto) {
		if (pgto.getTipo().equals("CHEQUE")) {
			return true;
		}else {
			return false;
		}	
	}

	
	@Transactional
	public void excluir(Long id) {
		Pagamento pagamento = repository.findOne(id);
		Boolean fechado = verificarSeMovimentacaoEstaFechado(pagamento);
		if(fechado) {
			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
		}else {
			try {
				
				List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
				for(ContaPagar conta : itens ) {
					alterarDadosContaPagar("AUTORIZADO", conta, null);			
				}
			
				Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
				if(tipoCheque) {
					cancelarLancamentoChequePendenteEmContaEmpresa(pagamento);					
				}else {
					excluirMovimentoDoExtrato(pagamento);
					cancelarLancamentoValorDebitosMovimentacao(pagamento);
				}
				repository.delete(id);
				repository.flush();
			} catch (ImpossivelExcluirEntidade e) {
				throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. \nPeríodo de movimentação fechado!"); 
			} catch (PersistenceException e) {
				throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. \nExclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
			}
		}
	}	
	
	
	private void cancelarLancamentoValorDebitosMovimentacao(Pagamento pagamento) {
		try {
			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(pagamento.getMovimentacao().getId());
			movBanco.setValorDebitos(movBanco.getValorDebitos().subtract(pagamento.getValor()));
			movBancariaRepository.save(movBanco);
		} catch (Exception e) {
			LOG.error("Pagamento não excluído. Erro ao cancelar o lançamento do pagamento nos débitos do movimento--" + e.getMessage()+"/"+e.getLocalizedMessage());	
		}
	}
	
	
	private void cancelarLancamentoChequePendenteEmContaEmpresa(Pagamento pagamento) {
		try {
			ContaEmpresa conta = contaEmpresaRepository.findOne(pagamento.getContaEmpresa().getId());
			conta.setValorPendente(conta.getValorPendente().subtract(pagamento.getValor()));
			contaEmpresaRepository.save(conta);
		} catch (Exception e) {
			LOG.error("Pagamento não foi excluido. Erro ao cancelar o lançamento do valor do pagamento nos cheques pendentes do movimento!--"+e.getMessage());
		}
	}	
		
	
	private void excluirMovimentoDoExtrato(Pagamento pagamento) {
		ExtratoBancario extrato = extratoRepository.findByPagamento(pagamento);
		extratoRepository.delete(extrato);
	}


	@Transactional
	public void pagar(Pagamento pagamento) {
		List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		
		if(!tipoCheque) {
			pagamento.setDataPago(pagamento.getData());
		}
		
		Boolean fechado = verificarSeMovimentacaoEstaFechadoNaConfirmacao(pagamento);

		if(fechado) {
			LOG.error("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do pagamento esta fora do período!");
		}else {
			try {
				if(tipoCheque){
					confirmarPagamentoCheques(pagamento);
				}else {
					alterarExtratoConfirmacaoPagamentoDebito(pagamento, "CONFERIDO");
				}
				for (ContaPagar conta : itens) {
					alterarDadosContaPagar("PAGO",conta, pagamento);
				}
				pagamento.setStatus("PAGO");
				repository.save(pagamento);
			} catch (Exception e) {
				LOG.error("Algo deu errado!! Fechamento não efetuado. Erro ao fechar o mês no método principal");
				throw new PagamentoNaoEfetuadoException("Algo deu errado!! Fechamento não efetuado");
			}
		}
	}
	
	
	private void alterarExtratoConfirmacaoPagamentoDebito(Pagamento pagamento, String status) {
		ExtratoBancario extrato = extratoRepository.findByPagamento(pagamento);
		extrato.setStatus(status);
		extratoRepository.save(extrato);
	}


	private void confirmarPagamentoCheques(Pagamento pagamento) {
		//Setar o pagamento com a movimentação bancária corrente
		Movimentacao mov = movRepository.findByEmpresaAndStatus(pagamento.getContaEmpresa().getEmpresa());	
		MovimentacaoBancaria movBancario = movBancariaRepository.findByMovimentacaoAndContaEmpresa(mov, pagamento.getContaEmpresa());
		pagamento.setMovimentacao(movBancario);
		cancelarLancamentoChequePendenteEmContaEmpresa(pagamento);
		criarPagamentoNoExtrato(pagamento);
		debitarPagamentoMovimento(pagamento);
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
		List<ContaPagar> itens = contaRepository.findByPagamentoId(pagamento.getId());
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		
		for (ContaPagar conta : itens) {
			conta.setDataPago(null);
			alterarDadosContaPagar("EMITIDO",conta, pagamento);
		}
		
		if(tipoCheque) {
			cancelarConfirmacaoPagamentoCheques(pagamento);
		}else {
			alterarExtratoConfirmacaoPagamentoDebito(pagamento, "ABERTO");			
		}
		pagamento.setStatus("EMITIDO");
		pagamento.setDataPago(null);
		repository.save(pagamento);
	}	


	private void cancelarConfirmacaoPagamentoCheques(Pagamento pagamento) {
		cancelarLancamentoValorDebitosMovimentacao(pagamento);
		pagamento.setMovimentacao(null);
		creditarChequePendenteEmContaEmpresa(pagamento);
		excluirMovimentoDoExtrato(pagamento);
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
	public Pagamento findOne(Long id) {
		return repository.findOne(id);
	}

	
	public Page<Pagamento> filtrar(PagamentoFilter pagamentoFilter, Pageable pageable) {
		return repository.filtrar(pagamentoFilter, pageable);
	}
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
	public BigDecimal totalGeral(PagamentoFilter pagamentoFilter) {
		return repository.totalGeral(pagamentoFilter);
	}
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

}
