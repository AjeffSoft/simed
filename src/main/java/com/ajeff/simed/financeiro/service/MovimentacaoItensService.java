package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.repository.MovimentacoesItensRepository;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.service.ContaEmpresaService;

@Service
public class MovimentacaoItensService {


	@Autowired
	private MovimentacoesItensRepository repository;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private RecebimentoService recebimentoService;
	@Autowired
	private TransferenciaService transferenciaService;
	@Autowired
	private PagamentoService pagamentoService;
	@Autowired
	private ExtratoService extratoService;		
	
	
	
	List<MovimentacaoItem> criarItensDeMovimentacaoPorContaDeEmpresas(Movimentacao movimentacao) {
		List<MovimentacaoItem> itens = new ArrayList<>();
		List<ContaEmpresa> contasEmpresa = contaEmpresaService.findByEmpresaIdAtivo(movimentacao.getEmpresa().getId());
		for(ContaEmpresa c : contasEmpresa) {
			MovimentacaoItem movimentacaoItem = new MovimentacaoItem();
			movimentacaoItem.setSaldoInicial(c.getSaldo());
			movimentacaoItem.setChequePendente(c.getChequePendente());
			movimentacaoItem.setContaEmpresa(c);
			movimentacaoItem.setMovimentacao(movimentacao);
			movimentacaoItem.setCreditos(BigDecimal.ZERO);
			movimentacaoItem.setDebitos(BigDecimal.ZERO);
			movimentacaoItem.setSaldoGeral(BigDecimal.ZERO);
			movimentacaoItem.setSaldoMovimento(BigDecimal.ZERO);
			extratoService.criarMovimentoNoExtrato(c.getSaldo(), null, c, true, "CONFERIDO", movimentacaoItem.getMovimentacao().getDataInicio(), "SALDO INICIAL", movimentacaoItem);
			itens.add(movimentacaoItem);
		}
		return itens;
	}


	public MovimentacaoItem findByMovimentacaoAndContaEmpresa(Movimentacao movimentacao, ContaEmpresa contaEmpresa) {
		return repository.findByMovimentacaoAndContaEmpresa(movimentacao, contaEmpresa);
	}
	
	public void creditarValorNosCreditos(MovimentacaoItem movimentacaoItem, BigDecimal valor) {
		movimentacaoItem.setCreditos(movimentacaoItem.getCreditos().add(valor));
		movimentacaoItem.setSaldoMovimento(movimentacaoItem.getSaldoMovimento().add(valor));
		atualizarSaldoGeralDaMovimentacaoItem(movimentacaoItem);
		repository.save(movimentacaoItem);
	}


	private void atualizarSaldoGeralDaMovimentacaoItem(MovimentacaoItem movimentacaoItem) {
		movimentacaoItem.setSaldoGeral(movimentacaoItem.getSaldoInicial().add(movimentacaoItem.getSaldoMovimento()));
	}
	
	public void debitarValorNosCreditos(MovimentacaoItem movimentacaoItem, BigDecimal valor) {
		movimentacaoItem.setCreditos(movimentacaoItem.getCreditos().subtract(valor));
		movimentacaoItem.setSaldoMovimento(movimentacaoItem.getSaldoMovimento().subtract(valor));
		atualizarSaldoGeralDaMovimentacaoItem(movimentacaoItem);
		repository.save(movimentacaoItem);
	}
	
	public void creditarValorNosDebitos(MovimentacaoItem movimentacaoItem, BigDecimal valor) {
		movimentacaoItem.setDebitos(movimentacaoItem.getDebitos().add(valor));
		movimentacaoItem.setSaldoMovimento(movimentacaoItem.getSaldoMovimento().subtract(valor));
		atualizarSaldoGeralDaMovimentacaoItem(movimentacaoItem);
		repository.save(movimentacaoItem);
	}	

	public void debitarValorNosDebitos(MovimentacaoItem movimentacaoItem, BigDecimal valor) {
		movimentacaoItem.setDebitos(movimentacaoItem.getDebitos().subtract(valor));
		movimentacaoItem.setSaldoMovimento(movimentacaoItem.getSaldoMovimento().add(valor));
		atualizarSaldoGeralDaMovimentacaoItem(movimentacaoItem);
		repository.save(movimentacaoItem);
	}


	public List<MovimentacaoItem> findByMovimentacao(Movimentacao movimentacao) {
		return repository.findByMovimentacao(movimentacao);
	}
	
	
	public void fecharTransacoes(Movimentacao movimentacao) {
		List<MovimentacaoItem> movimentacoesItens = repository.findByMovimentacao(movimentacao);
		for(MovimentacaoItem mov : movimentacoesItens) {
			transferenciaService.fecharTransferencias(mov);
			recebimentoService.fecharRecebimentos(mov);
			pagamentoService.fecharPagamentos();
		}
	}


	public void fecharCalculosEValores(Movimentacao movimentacao) {
		List<MovimentacaoItem> movimentacoesItens = repository.findByMovimentacao(movimentacao);
		movimentacao.setSaldoInicial(movimentacoesItens.stream().map(m -> m.getSaldoInicial()).reduce(BigDecimal.ZERO, BigDecimal::add));
		movimentacao.setTotalCreditos(movimentacoesItens.stream().map(m -> m.getCreditos()).reduce(BigDecimal.ZERO, BigDecimal::add));
		movimentacao.setTotalDebitos(movimentacoesItens.stream().map(m -> m.getDebitos()).reduce(BigDecimal.ZERO, BigDecimal::add));
		movimentacao.setSaldoMovimento(movimentacoesItens.stream().map(m -> m.getSaldoMovimento()).reduce(BigDecimal.ZERO, BigDecimal::add));
		movimentacao.setSaldoGeral(movimentacoesItens.stream().map(m -> m.getSaldoGeral()).reduce(BigDecimal.ZERO, BigDecimal::add));
	}
	
	
	public void atualizarSaldoContaBancaria(Movimentacao movimentacao) {
		List<MovimentacaoItem> movimentacoesItens = repository.findByMovimentacao(movimentacao);
		for(MovimentacaoItem mov : movimentacoesItens) {
			contaEmpresaService.atualizarSaldoContaEmpresa(mov);
		}
	}	


	public MovimentacaoItem findOne(Long id) {
		return repository.findOne(id);
	}


	public List<MovimentacaoItem> findByContaEmpresa(ContaEmpresa contaEmpresa) {
		return repository.findByContaEmpresa(contaEmpresa);
	}
	

}
