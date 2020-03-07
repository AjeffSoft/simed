package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.PagamentosRepository;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.financeiro.service.exception.DataInformadaMenorDataEmissaoException;
import com.ajeff.simed.financeiro.service.exception.ErroAoFecharMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;

@Service
public class PagamentoService {


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
	private MovimentacaoItensService movimentacaoItemService;
	@Autowired
	private MovimentacaoService movimentacaoService;
	
	
	@Transactional
	public void salvar(Pagamento pagamento, List<Long> ids){
		Movimentacao movimentacao = movimentacaoService.verificarSeMovimentacaoEstaFechado(pagamento.getContaEmpresa().getEmpresa(), pagamento.getData());
		MovimentacaoItem movimentacaoItem = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacao, pagamento.getContaEmpresa());
		emitirPagamento(pagamento, ids, movimentacaoItem);
	}
	

	private void emitirPagamento(Pagamento pagamento, List<Long> ids, MovimentacaoItem movimentacaoItem) {
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		if (pagamento.getAgrupado()) {
				emitirPagamentoAgrupado(pagamento, ids, tipoCheque, movimentacaoItem);
			}else {
				emitirPagamentoIndividual(pagamento, ids, tipoCheque, movimentacaoItem);
			}
	}	

	
	private Boolean verificarTipoPagtoCheque(Pagamento pgto) {
		return pgto.getTipo().equals("CHEQUE") ? true: false; 
	}	
	
	
	private void emitirPagamentoAgrupado(Pagamento pagamento, List<Long> ids, Boolean tipoCheque, MovimentacaoItem movimentacaoItem) {
		
		try {
			List<ContaPagar> contasAutorizadas = contaPagarService.buscarContasPagarSelecionadas(ids);
			BigDecimal total = new BigDecimal(0);
			for(ContaPagar conta : contasAutorizadas ) {
				total = total.add(conta.getValor());
				alterarDadosContaPagar("EMITIDO",conta, pagamento);
			}
			pagamento.setValor(total);
			pagamento.setStatus("EMITIDO");
			pagamento.setDataPago(pagamento.getData());
			pagamento.setFechado(false);
			pagamento.setDocumento(setarNumeroDocumento(pagamento, tipoCheque));
			comportamentoDoPagamentoPorTipo(pagamento, tipoCheque, movimentacaoItem);
			repository.save(pagamento);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new PagamentoNaoEfetuadoException("Algo deu errado!! Pagamento não efetuado");
		}
	}
	
	
	public List<ContaPagar> listaDeContasSelecionadasParaPagamento(List<Long> ids) {
		List<ContaPagar> itens = contaPagarService.buscarContasPagarSelecionadas(ids);
		return itens;
	}		


	private String setarNumeroDocumento(Pagamento pagamento, Boolean tipoCheque) {
		if(tipoCheque) {
			Integer cheque = pagamento.getNumCheque();
			String novoDocumento = cheque.toString();
			cheque++;
			pagamento.setNumCheque(cheque);
			return "CH-" + novoDocumento;
		}else {
			Long codigo = empresaService.setarNumeroPagamentoDaEmpresa(pagamento.getContaEmpresa().getEmpresa());
			return "PG-" + codigo.toString();		}
	}
	
	
	private void comportamentoDoPagamentoPorTipo(Pagamento pagamento, Boolean tipoCheque, MovimentacaoItem movimentacaoItem) {
		if(tipoCheque) {
			creditarChequePendenteEmContaEmpresa(pagamento);
		}else {
			pagamento.setMovimentacaoItem(movimentacaoItem);
			criarPagamentoNoExtrato(pagamento);
		}
	}		
	
	
	private void creditarChequePendenteEmContaEmpresa(Pagamento pagamento) {
		contaEmpresaService.creditarValorNoTotalPendencias(pagamento.getContaEmpresa(), pagamento.getValor());
	}	
	
	
	private void emitirPagamentoIndividual(Pagamento pagamento, List<Long> ids, Boolean tipoCheque, MovimentacaoItem movimentacaoItem) {
		List<ContaPagar> contasAutorizadas = contaPagarService.buscarContasPagarSelecionadas(ids);
		try {
			for(ContaPagar conta : contasAutorizadas ) {
				Pagamento pgto = registroUnico(pagamento, conta, tipoCheque);
				alterarDadosContaPagar("EMITIDO", conta, pgto);
				comportamentoDoPagamentoPorTipo(pgto, tipoCheque, movimentacaoItem);
				repository.save(pgto);
			}
		} catch (PagamentoNaoEfetuadoException e) {
			throw new PagamentoNaoEfetuadoException("Ocorreu um erro e o pagamento não foi efetuado!" );
		}
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
		pgto.setDataPago(pagamento.getData());
		pgto.setFechado(false);
		pgto.setDocumento(setarNumeroDocumento(pagamento, tipoCheque));
		return pgto;
	}		
	

	public Page<Pagamento> filtrar(PagamentoFilter pagamentoFilter, Pageable pageable) {
		return repository.filtrar(pagamentoFilter, pageable);
	}
	
	
	public BigDecimal totalGeral(PagamentoFilter pagamentoFilter) {
		return repository.totalGeral(pagamentoFilter);
	}
	
	
	public Pagamento findOne(Long id) {
		return repository.findOne(id);
	}
	
	private Movimentacao setarMovimentacao(Pagamento pagamento) {
		Movimentacao movimentacao = movimentacaoService.verificarSeMovimentacaoEstaFechado(pagamento.getContaEmpresa().getEmpresa(), pagamento.getDataPago());
		return movimentacao;
	}
	
	private MovimentacaoItem setarMovimentacaoItem(Pagamento pagamento, Movimentacao movimentacao) {
		MovimentacaoItem movimentacaoItem = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacao, pagamento.getContaEmpresa());
		return movimentacaoItem;
	}	
	
	
	@Transactional
	public void confirmar(Pagamento pagamento) {
		List<ContaPagar> itens = contaPagarService.findByPagamentoId(pagamento.getId());
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		Movimentacao movimentacao = setarMovimentacao(pagamento);
		MovimentacaoItem movimentacaoItem = setarMovimentacaoItem(pagamento, movimentacao);
		verificarSeDataPagoMenorDataEmissao(pagamento);
		if(tipoCheque) {
			confirmarPagamentoCheques(pagamento, movimentacaoItem);
		}else {
			pagamento.setDataPago(pagamento.getData());
			movimentacaoItemService.creditarValorNosDebitos(movimentacaoItem, pagamento.getValor());
			extratoService.alterarStatusEMovimentacaoDoExtratoPorPagamento(pagamento, "CONFERIDO", movimentacaoItem);
		}
		
		for (ContaPagar conta : itens) {
			alterarDadosContaPagar("PAGO",conta, pagamento);
		}
		pagamento.setStatus("PAGO");
		repository.save(pagamento);
	}	
	
	
	private void confirmarPagamentoCheques(Pagamento pagamento, MovimentacaoItem movimentacaoItem) {
		pagamento.setMovimentacaoItem(movimentacaoItem);
		contaEmpresaService.debitarValorNoTotalPendencias(pagamento.getContaEmpresa(), pagamento.getValor());
		criarPagamentoNoExtrato(pagamento);
		movimentacaoItemService.creditarValorNosDebitos(movimentacaoItem, pagamento.getValor());
	}

	
	private void cancelarConfirmacaoPagamentoCheques(Pagamento pagamento, MovimentacaoItem movimentacaoItem) {
		contaEmpresaService.creditarValorNoTotalPendencias(pagamento.getContaEmpresa(), pagamento.getValor());
		movimentacaoItemService.debitarValorNosDebitos(movimentacaoItem, pagamento.getValor());
		pagamento.setMovimentacaoItem(null);
		excluiPagamentoDoExtrato(pagamento);
	}	
	

	@Transactional
	public void cancelarConfirmacao(Pagamento pagamento) {
		List<ContaPagar> itens = contaPagarService.findByPagamentoId(pagamento.getId());
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		Movimentacao movimentacao = movimentacaoService.verificarSeMovimentacaoEstaFechado(pagamento.getContaEmpresa().getEmpresa(), pagamento.getData());
		MovimentacaoItem movimentacaoItem = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacao, pagamento.getContaEmpresa());

		for (ContaPagar conta : itens) {
			alterarDadosContaPagar("EMITIDO", conta, pagamento);
		}

		if (tipoCheque) {
			cancelarConfirmacaoPagamentoCheques(pagamento, movimentacaoItem);
		} else {
			pagamento.setDataPago(null);
			movimentacaoItemService.debitarValorNosDebitos(movimentacaoItem, pagamento.getValor());
			extratoService.alterarStatusEMovimentacaoDoExtratoPorPagamento(pagamento, "ABERTO", movimentacaoItem);
		}
		pagamento.setStatus("EMITIDO");
		pagamento.setDataPago(null);
		repository.save(pagamento);
	}	


	@Transactional
	public void excluir(Long id) {
		Pagamento pagamento = repository.findOne(id);
		Boolean tipoCheque = verificarTipoPagtoCheque(pagamento);
		List<ContaPagar> itens = contaPagarService.findByPagamentoId(pagamento.getId());
		try {
			for(ContaPagar conta : itens ) {
				alterarDadosContaPagar("AUTORIZADO", conta, null);			
			}
			if (tipoCheque) {
				contaEmpresaService.debitarValorNoTotalPendencias(pagamento.getContaEmpresa(), pagamento.getValor());
			} else {
				extratoService.excluirPagamentoDoExtrato(pagamento);
			}
			repository.delete(id);
			repository.flush();
		} catch (ImpossivelExcluirEntidade e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o pagamento. \nPeríodo de movimentação fechado!"); 
		}
	}	


	private void criarPagamentoNoExtrato(Pagamento pagamento) {
		if(pagamento.isNovo()) {
			criarMovimentoPagamentoNoExtrato(pagamento, "ABERTO", pagamento.getData());
		}else {
			//Cheque
		    criarMovimentoPagamentoNoExtrato(pagamento, "CONFERIDO", pagamento.getDataPago());
		}
	}	
	
	
	private void criarMovimentoPagamentoNoExtrato(Pagamento pagamento, String status, LocalDate data) {
		extratoService.criarMovimentoPagamentoNoExtrato(pagamento.getValor(), pagamento, pagamento.getContaEmpresa(), false, status, data, "PAGAMENTO", pagamento.getMovimentacaoItem());

	}
	
	private void verificarSeDataPagoMenorDataEmissao (Pagamento pagamento){
		if (pagamento.isDataPagamentoMenorEmissao()) {
			throw new DataInformadaMenorDataEmissaoException("A data de pagamento não pode ser menor que a data de emissão");
		}
	}
	
	
	private void excluiPagamentoDoExtrato(Pagamento pagamento) {
		extratoService.excluirPagamentoDoExtrato(pagamento);
	}



	private Boolean verificarSeTemPagamentoAberto(MovimentacaoItem movimentacaoItem) {
		List<Pagamento> pagamentos = repository.findByMovimentacaoItem(movimentacaoItem);
		List<Pagamento> pagamentosAbertos = new ArrayList<>();
		pagamentos.stream().filter(i -> i.getStatus().equals("EMITIDO") && i.getTipo() != "CHEQUE").forEach(pagamentosAbertos::add);
		return pagamentosAbertos.isEmpty() ? false: true;
	}
	
	
	private List<Pagamento> retornaTodosPagamentoPagosDaMovimentacao(MovimentacaoItem movimentacaoItem) {
		List<Pagamento> pagamentos = repository.findByMovimentacaoItem(movimentacaoItem);
		List<Pagamento> pagamentosFechados = new ArrayList<>();
		pagamentos.stream().filter(i -> i.getStatus().equals("PAGO")).forEach(pagamentosFechados::add);
		return pagamentosFechados;
	}
	
	
	public void fecharPagamentos(MovimentacaoItem m) {
		if(verificarSeTemPagamentoAberto(m)){
			throw new ErroAoFecharMovimentacaoException("Não foi possível fechar o movimento. Existe pagamento em aberto!");
		}else {
			List<Pagamento> pagamentos = retornaTodosPagamentoPagosDaMovimentacao(m);
			if(!pagamentos.isEmpty()) {
				pagamentos.stream().forEach(t -> t.setFechado(true));
				repository.save(pagamentos);
			}
		}
	}
	
}
