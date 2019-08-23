package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
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

@Service
public class RecebimentoService {

	private static final Logger LOG = LoggerFactory.getLogger(RecebimentoService.class);	
	
	@Autowired
	private RecebimentosRepository repository;
//	@Autowired
//	private ContaEmpresaRepository contaEmpresaRepository;
	@Autowired
	private ContasReceberRepository contaReceberRepository;
	@Autowired
	private MovimentacoesRepository movRepository;
	@Autowired
	private ExtratosRepository extratoRepository;
	@Autowired
	private MovimentacoesBancariasRepository movBancariaRepository;
	
	
	@Transactional
	public void salvar(Recebimento recebimento, ContaReceber contaReceber) {
		Boolean fechado = verificarSeMovimentacaoEstaFechado(recebimento);
		if(fechado) {
			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data do recebimento esta fora do período!");
		}else {
			try {
				receberConta(recebimento, contaReceber);
				recebimento.setMovimentacao(setarMovimentacao(recebimento));
				recebimento.setFechado(false);
				creditarRecebimentoMovimento(recebimento);
				criarRecebimentoNoExtrato(recebimento, "CONFERIDO");
				repository.save(recebimento);
			} catch (ValorInformadoInvalidoException e) {
				throw new RecebimentoNaoEfetuadoException("O valor informado é inválido!! Recebimento não efetuado");
			} catch (PersistenceException e) {
				throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
			} catch (Exception e) {
				throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
			}

		}	
	}

	private void criarRecebimentoNoExtrato(Recebimento recebimento, String status) {
		ExtratoBancario extrato = new ExtratoBancario();
		extrato.setData(recebimento.getData());
		extrato.setContaBancaria(recebimento.getContaEmpresa());
		extrato.setCredito(true);
		extrato.setHistorico("Recebimento de: " + recebimento.getContaReceber().getFornecedor().getFantasia() +" - Documento nº: "+ recebimento.getContaReceber().getDocumento());
		extrato.setRecebimento(recebimento);
		extrato.setStatus(status);
		extrato.setTipo("RECEBIMENTO");
		extrato.setMovimentacao(recebimento.getMovimentacao());
		extrato.setValor(recebimento.getValor());
		extratoRepository.save(extrato);
	}	
	
	
	private void creditarRecebimentoMovimento(Recebimento recebimento) {
		try {
			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(recebimento.getMovimentacao().getId());
			movBanco.setValorCreditos(movBanco.getValorCreditos().add(recebimento.getValor()));
			movBancariaRepository.save(movBanco);
		} catch (Exception e) {
			LOG.error("recebimento não efetuado. Erro ao incluir o creditar no movimento bancário--" + e.getMessage()+"/"+e.getLocalizedMessage());	
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		}
	}	
	
	
	private MovimentacaoBancaria setarMovimentacao(Recebimento recebimento) {
		Movimentacao movimento = movRepository.findByEmpresaAndStatus(recebimento.getContaEmpresa().getEmpresa());
		MovimentacaoBancaria movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, recebimento.getContaEmpresa());
		return movBancaria;
	}


	private void receberConta(Recebimento recebimento, ContaReceber contaReceber) {
		try {
			String tipo = verificarSeOValorDoRecebimentoEMenorQueSaldo(recebimento, contaReceber);
			if(tipo.equals("PARCIAL")) {
				recebimentoParcial(recebimento, contaReceber);
			}else if (tipo.equals("INTEGRAL")){
				recebimentoIntegral(recebimento, contaReceber);
			}
		} catch (Exception e) {
			LOG.error("Erro ao receber o valor!");
			throw new ValorInformadoInvalidoException("Valor informado é inválido!! Recebimento não efetuado");
		}
	}
	
	
	private void recebimentoParcial(Recebimento recebimento, ContaReceber conta) {
		try {
			conta.setValorRecebido(conta.getValorRecebido().add(recebimento.getValor()));
			contaReceberRepository.save(conta);
		} catch (Exception e) {
			LOG.error("Erro ao alterar e salvar os dados das contas a receber EM PARCIAL!");
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		}
	}


	private void recebimentoIntegral(Recebimento recebimento, ContaReceber conta) {
		try {
			conta.setStatus("RECEBIDO");
			conta.setDataRecebido(recebimento.getData());
			conta.setValorRecebido(conta.getValorRecebido().add(calcularSaldoAReceberDaConta(conta)));
			contaReceberRepository.save(conta);
		} catch (Exception e) {
			LOG.error("Erro ao alterar e salvar os dados das contas a receber!");
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		}
	}


	private String verificarSeOValorDoRecebimentoEMenorQueSaldo(Recebimento recebimento, ContaReceber conta) {
		BigDecimal saldo = calcularSaldoAReceberDaConta(conta);
		
		if(recebimento.getValor().compareTo(saldo) == -1) {
			return "PARCIAL";
		}else if (recebimento.getValor().compareTo(saldo) == 0){
			return "INTEGRAL";
		}else {
			LOG.error("Valor Informado inválido!");
			throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
		}
	}

	
	private BigDecimal calcularSaldoAReceberDaConta(ContaReceber conta) {
		return conta.getValor().subtract(conta.getValorRecebido());
	}


	private Boolean verificarSeMovimentacaoEstaFechado(Recebimento recebimento) {
		Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(recebimento.getContaEmpresa().getEmpresa());	
		if(!mov.isPresent()) {
			return true;
		}else if (recebimento.getData().isBefore(mov.get().getDataInicio()) || recebimento.getData().isAfter(mov.get().getDataFinal())) {
			return true;
		}else {
			return false;
		}	
	}	

	
	
	@Transactional
	public void excluir(Long id) {
		Recebimento recebimento = repository.findOne(id);
		try {
			alterarStatusContaReceber(recebimento, "ABERTO");
			excluirMovimentoDoExtrato(recebimento);
			cancelarLancamentoValorCreditosMovimentacao(recebimento);
		
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o recebimento. \nExclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}
	
	
	private void alterarStatusContaReceber(Recebimento recebimento, String status) {
		ContaReceber contaReceber = contaReceberRepository.findOne(recebimento.getContaReceber().getId());
		contaReceber.setStatus(status);
		contaReceber.setValorRecebido(contaReceber.getValorRecebido().subtract(recebimento.getValor()));
		contaReceberRepository.save(contaReceber);
	}
	

	private void cancelarLancamentoValorCreditosMovimentacao(Recebimento recebimento) {
		try {
			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(recebimento.getMovimentacao().getId());
			movBanco.setValorCreditos(movBanco.getValorCreditos().subtract(recebimento.getValor()));
			movBancariaRepository.save(movBanco);
		} catch (Exception e) {
			LOG.error("recebimento não excluído. Erro ao cancelar o lançamento do recebimento nos créditos do movimento--" + e.getMessage()+"/"+e.getLocalizedMessage());	
		}
	}
	
	
	private void excluirMovimentoDoExtrato(Recebimento recebimento) {
		ExtratoBancario extrato = extratoRepository.findByRecebimento(recebimento);
		extratoRepository.delete(extrato);
	}	
	
	
//		private void creditarRecebimentoMovimento(Recebimento recebimento, MovimentacaoBancaria movBancaria) {
//			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(movBancaria.getId());
//			movBanco.setTotalFinal(movBanco.getTotalFinal().add(recebimento.getValor()));
//			movBancariaRepository.save(movBanco);
//		}
//
//
//
//		private void receberConta(Recebimento recebimento) {
//			try {
//				ContaReceber receber = contaReceberRepository.findOne(recebimento.getContaReceber().getId());
//
//				if(receber.getValorRecebido().compareTo(recebimento.getValor()) ==0 ) {
//					receber.setStatus("RECEBIDO");
//
//				}else if (receber.getValorRecebido().compareTo(recebimento.getValor()) > 0 ) {
//					receber.setStatus("ABERTO");
//				}else {
//					LOG.error("Valor recebido maior que valor devido");
//					throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
//				}
//				receber.setValorRecebido(receber.getValorRecebido().subtract(recebimento.getValor()));
//				contaReceberRepository.save(receber);				
//			} catch (RecebimentoNaoEfetuadoException e) {
//				LOG.error("Ocorreu um erro ao receber o valor da conta.....Erro: " + e.getMessage());
//				throw new RecebimentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
//			}
//		}
//
//	
//		private void criarMovimentoNoExtrato(Recebimento recebimento) {
//			try {
//				ExtratoBancario extrato = new ExtratoBancario();
//				extrato.setData(recebimento.getData());
//				extrato.setContaBancaria(recebimento.getContaEmpresa());
//				extrato.setCredito(true);
//				extrato.setHistorico("Recebimento de: " + recebimento.getContaReceber().getFornecedor().getFantasia() +" - Documento nº: "+ recebimento.getContaReceber().getDocumento());
//				extrato.setRecebimento(recebimento);
//				extrato.setStatus("CONFERIDO");
//				extrato.setTipo("RECEBIMENTO");
//				extrato.setMovimentacao(recebimento.getMovimentacao());
//				extrato.setValor(recebimento.getValor());
//				extratoRepository.save(extrato);
//			} catch (Exception e) {
//				LOG.error("RECEBIMENTO não efetuado pois ocorreu um erro ao gerar o movimento no extrato.--"+e.getMessage());
//				throw new PagamentoNaoEfetuadoException("Algo deu errado!! Recebimento não efetuado");
//			}
//		}	
//
//		
//		
//	public Page<Recebimento> filtrar(RecebimentoFilter recebimentoFilter, Pageable pageable) {
//		return repository.filtrar(recebimentoFilter, pageable);
//	}
//
//	public Recebimento findOne(Long id) {
//		return repository.findOne(id);
//	}
//	
//	@Transactional
//	public void excluir(Long id) {
//		Recebimento recebimento = repository.findOne(id);
//		ExtratoBancario extrato = extratoRepository.findByRecebimento(recebimento);
//		ContaReceber receber = contaReceberRepository.findOne(recebimento.getContaReceber().getId());
//		try {
//			ContaEmpresa contabanco = contaEmpresaRepository.findOne(recebimento.getContaEmpresa().getId());
//			Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(contabanco.getEmpresa());
//			if(!mov.isPresent()) {
//				throw new PeriodoMovimentacaoException("Não existe nenhuma movimentação em aberto para esta empresa!");
//			}else if (recebimento.getData().isBefore(mov.get().getDataInicio()) || recebimento.getData().isAfter(mov.get().getDataFinal())) {
//				throw new PeriodoMovimentacaoException("Data do recebimento fora do periodo da movimentação em aberto");
//			}else {
//				receber.setValorRecebido(receber.getValorRecebido().add(recebimento.getValor()));
//				if (receber.getStatus().equals("RECEBIDO")) {
//					receber.setStatus("ABERTO");
//				}
//				contaReceberRepository.save(receber);
//				extratoRepository.delete(extrato);
//				repository.delete(id);
//				repository.flush();
//			}
//		} catch (ImpossivelExcluirEntidade e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o recebimento. Período de movimentação fechado!"); 
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o recebimento. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//		}
//		
//	}
//


	public List<Recebimento> findByContaReceber(ContaReceber contaReceber) {
		return repository.findByContaReceber(contaReceber);
	}	

}
