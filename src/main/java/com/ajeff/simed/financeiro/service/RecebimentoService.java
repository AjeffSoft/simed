package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.RecebimentosRepository;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.RecebimentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;

@Service
public class RecebimentoService {

	
	@Autowired
	private RecebimentosRepository repository;
	@Autowired
	private ContaReceberService contaReceberService;
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private ExtratoService extratoService;
	
	
	@Transactional
	public void salvar(Recebimento recebimento, Long id) {
		ContaReceber contaReceber = contaReceberService.findOne(id);

		movimentacaoService.verificarSeMovimentacaoEstaFechado(recebimento.getContaEmpresa().getEmpresa(), recebimento.getData());
		try {
			receberConta(recebimento, contaReceber);
			recebimento.setContaReceber(contaReceber);
			recebimento.setFechado(false);
			Movimentacao movimentacao = movimentacaoService.findByEmpresaAnStatusAtivo(recebimento.getContaEmpresa().getEmpresa());

			recebimento.setMovimentacao(movimentacao);
			movimentacaoService.creditarValor(movimentacao, recebimento.getValor());
			extratoService.criarRecebimentoNoExtrato(recebimento, "ABERTO");
			repository.save(recebimento);
		} catch (PersistenceException e) {
			e.printStackTrace();
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		}
	}


	private void receberConta(Recebimento recebimento, ContaReceber contaReceber) {
		String tipo = verificarSeOValorDoRecebimentoEMenorQueSaldo(recebimento, contaReceber);
		if(tipo.equals("PARCIAL")) {
			recebimentoParcial(recebimento, contaReceber);
		}else if (tipo.equals("INTEGRAL")){
			recebimentoIntegral(recebimento, contaReceber);
		}
	}
	
	
	private void recebimentoParcial(Recebimento recebimento, ContaReceber conta) {
		try {
			conta.setValorRecebido(conta.getValorRecebido().add(recebimento.getValor()));
			contaReceberService.salvar(conta);
		} catch (PersistenceException e) {
			throw new RecebimentoNaoEfetuadoException("Erro ao receber a conta e atualizar o valor recebido");
		}
	}


	private void recebimentoIntegral(Recebimento recebimento, ContaReceber conta) {
		try {
			conta.setStatus("RECEBIDO");
			conta.setValorRecebido(conta.getValorRecebido().add(recebimento.getValor()));
			contaReceberService.salvar(conta);
		} catch (PersistenceException e) {
			throw new RecebimentoNaoEfetuadoException("Erro ao receber a conta e atualizar o valor recebido");
		}
	}


	private String verificarSeOValorDoRecebimentoEMenorQueSaldo(Recebimento recebimento, ContaReceber conta) {
		BigDecimal saldo = calcularSaldoAReceberDaConta(conta);
		
		if(recebimento.getValor().compareTo(saldo) == -1) {
			return "PARCIAL";
		}else if (recebimento.getValor().compareTo(saldo) == 0){
			return "INTEGRAL";
		}else {
			throw new ValorInformadoInvalidoException("O saldo da conta é menor que o valor informado");
		}
	}

	
	private BigDecimal calcularSaldoAReceberDaConta(ContaReceber conta) {
		return conta.getValor().subtract(conta.getValorRecebido());
	}



	@Transactional
	public void excluir(Long id) {
		Recebimento recebimento = repository.findOne(id);
		try {
			contaReceberService.alterarStatusESubtrairValorConta(recebimento.getContaReceber().getId(), "ABERTO", recebimento.getValor());
			excluirMovimentoDoExtrato(recebimento);
			Movimentacao movimentacao = movimentacaoService.findByEmpresaAnStatusAtivo(recebimento.getContaEmpresa().getEmpresa());
			movimentacaoService.cancelarCredito(movimentacao, recebimento.getValor());
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o recebimento. \nExclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}
	
	
	private void excluirMovimentoDoExtrato(Recebimento recebimento) {
		ExtratoBancario extrato = extratoService.findByRecebimento(recebimento);
		extratoService.excluir(extrato);
	}	
	

	public List<Recebimento> findByContaReceber(ContaReceber contaReceber) {
		return repository.findByContaReceberOrderByData(contaReceber);
	}	

}
