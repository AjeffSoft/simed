package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.ContasReceberRepository;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesBancariasRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.RecebimentosRepository;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.RecebimentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;
import com.ajeff.simed.geral.model.ContaEmpresa;

@Service
public class RecebimentoService {

	private static final Logger LOG = LoggerFactory.getLogger(RecebimentoService.class);	
	
	@Autowired
	private RecebimentosRepository repository;
//	@Autowired
//	private ContaEmpresaRepository contaEmpresaRepository;
	@Autowired
	private ContaReceberService contaReceberService;
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private ExtratoService extratoService;
	@Autowired
	private MovimentacoesBancariasRepository movBancariaRepository;
	
	
	@Transactional
	public void salvar(Recebimento recebimento, ContaReceber contaReceber) {

		movimentacaoService.verificarSeMovimentacaoEstaFechado(recebimento.getContaEmpresa().getEmpresa(), recebimento.getData());
		try {
			receberConta(recebimento, contaReceber);
			recebimento.setContaReceber(contaReceber);
			recebimento.setFechado(false);

//			recebimento.setMovimentacao(setarMovimentacao(recebimento));
//			creditarRecebimentoMovimento(recebimento);
			extratoService.criarRecebimentoNoExtrato(recebimento, "ABERTO");
			repository.save(recebimento);
		} catch (PersistenceException e) {
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
//			cancelarLancamentoValorCreditosMovimentacao(recebimento);
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

	
	
	
//	private void cancelarLancamentoValorCreditosMovimentacao(Recebimento recebimento) {
//		try {
//			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(recebimento.getMovimentacao().getId());
//			movBanco.setValorCreditos(movBanco.getValorCreditos().subtract(recebimento.getValor()));
//			movBancariaRepository.save(movBanco);
//		} catch (Exception e) {
//			LOG.error("recebimento não excluído. Erro ao cancelar o lançamento do recebimento nos créditos do movimento--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//		}
//	}
	
//	private void creditarRecebimentoMovimento(Recebimento recebimento) {
//	try {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(recebimento.getMovimentacao().getId());
//		movBanco.setValorCreditos(movBanco.getValorCreditos().add(recebimento.getValor()));
//		movBancariaRepository.save(movBanco);
//	} catch (Exception e) {
//		LOG.error("recebimento não efetuado. Erro ao incluir o creditar no movimento bancário--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//		throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
//	}
//}	


//private MovimentacaoBancaria setarMovimentacao(Recebimento recebimento) {
//	Movimentacao movimento = movRepository.findByEmpresaAndStatus(recebimento.getContaEmpresa().getEmpresa());
//	MovimentacaoBancaria movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, recebimento.getContaEmpresa());
//	return movBancaria;
//}	

}
