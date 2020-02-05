package com.ajeff.simed.financeiro.service;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.TransferenciasContasRepository;
import com.ajeff.simed.financeiro.repository.filter.TransferenciaFilter;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.TransferenciaNaoEfetuadaException;

@Service
public class TransferenciaService {

	
	@Autowired
	private TransferenciasContasRepository repository;
	@Autowired
	private ExtratosRepository extratoRepository;
	@Autowired
	private MovimentacaoService movimentacaoService;

	
	
	@Transactional
	public void salvar(TransferenciaContas transferencia) {

		movimentacaoService.verificarSeMovimentacaoEstaFechado(transferencia.getContaOrigem().getEmpresa(), transferencia.getData());
		movimentacaoService.verificarSeMovimentacaoEstaFechado(transferencia.getContaDestino().getEmpresa(), transferencia.getData());

		try {
			if(transferencia.isNovo()) {
				emissaoTransferencia(transferencia);
			}
			
		} catch (EntityNotFoundException e) {
			throw new TransferenciaNaoEfetuadaException("Não foi possível encontrar uma entidade no banco de dados");
		} catch (Exception e) {
			throw new TransferenciaNaoEfetuadaException("Algo deu errado!! Transferencia não efetuada");
		}
					
	}
	
	
	private void emissaoTransferencia(TransferenciaContas transferencia) {
		Movimentacao movimentacao = movimentacaoService.findByEmpresaAnStatusAtivo(transferencia.getContaOrigem().getEmpresa());
		transferencia.setMovimentacao(movimentacao);
		transferencia.setStatus("ABERTO");
		transferencia.setFechado(false);
		repository.save(transferencia);
		criarMovimentacaoNoExtrato(transferencia);

	}

//	private Boolean verificarSeMovimentacaoEstaFechado(TransferenciaContas transferencia, ContaEmpresa contaEmpresa) {
//		Optional<Movimentacao> mov = movRepository.findByEmpresaAndStatusAberto(contaEmpresa.getEmpresa());	
//		if(!mov.isPresent()) {
//			return true;
//		}else if (transferencia.getData().isBefore(mov.get().getDataInicio()) || transferencia.getData().isAfter(mov.get().getDataFinal())) {
//			return true;
//		}else {
//			return false;
//		}	
//	}
	
//	private MovimentacaoBancaria setarMovimentacao(ContaEmpresa contaEmpresa) {
//		Movimentacao movimento = movRepository.findByEmpresaAndStatus(contaEmpresa.getEmpresa());
//		MovimentacaoBancaria movBancaria = movBancariaRepository.findByMovimentacaoAndContaEmpresa(movimento, contaEmpresa);
//		return movBancaria;
//	}	




//	private void creditarContaDestino(TransferenciaContas transferenciaContas) {
//		TransferenciaContas transf = repository.findOne(transferenciaContas.getId());
//		Boolean fechado = verificarSeMovimentacaoEstaFechado(transf, transf.getContaDestino());
//		if(fechado) {
//			throw new PeriodoMovimentacaoException("Não existe movimentação em aberto ou a data da transferencia esta fora do período na conta de destino!");
//		}else {
//			criarMovimentoNoExtrato(transf, transf.getContaDestino(), true);
//			creditarTransferenciaCreditosMovimentacao(transferenciaContas);
//		}	
//	}


	private void criarMovimentacaoNoExtrato(TransferenciaContas transferencia) {
		criarMovimentoDaContaPorTipo(transferencia, false);
		criarMovimentoDaContaPorTipo(transferencia, true);

	}


	private void criarMovimentoDaContaPorTipo(TransferenciaContas transferencia, boolean tipo) {
		ExtratoBancario extrato = new ExtratoBancario();
		extrato.setData(transferencia.getData());
		extrato.setCredito(tipo);
		extrato.setHistorico("TRANSFERENCIA Nº " + transferencia.getId() +" ENTRE AS CONTAS: "+ transferencia.getContaOrigem().getNome()+" E: "+ transferencia.getContaDestino().getNome() );
		extrato.setTransferencia(transferencia);
		extrato.setStatus("ABERTO");
		extrato.setTipo("TRANSFERENCIA");
		extrato.setMovimentacao(transferencia.getMovimentacao());
		extrato.setValor(transferencia.getValor());
		
		if(tipo) {
			extrato.setContaBancaria(transferencia.getContaDestino());
		}else {
			extrato.setContaBancaria(transferencia.getContaOrigem());
		}
		extratoRepository.save(extrato);
	}



//	//Creditar em debitos no movimento da conta de ORIGEM
//	private void creditarTransferenciaDebitosMovimentacao(TransferenciaContas transferenciaContas) {
//		try {
//			MovimentacaoBancaria movBanco = movBancariaRepository.findOne(transferenciaContas.getMovimentacao().getId());
//			movBanco.setValorDebitos(movBanco.getValorDebitos().add(transferenciaContas.getValor()));
//			movBancariaRepository.save(movBanco);
//		} catch (Exception e) {
//			LOG.error("Transferencia não efetuada. Erro ao incluir o debito no movimento bancário--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//			throw new TransferenciaNaoEfetuadaException("Algo deu errado!! Transferencia não efetuada");
//		}
//	}
	

//	private void debitarTransferenciaDebitosMovimentacao(TransferenciaContas transferenciaContas) {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(transferenciaContas.getMovimentacao().getId());
//		movBanco.setValorDebitos(movBanco.getValorDebitos().subtract(transferenciaContas.getValor()));
//		movBancariaRepository.save(movBanco);
//	}
	
	
//	//Creditar em creditos no movimento da conta de DESTINO
//	private void creditarTransferenciaCreditosMovimentacao(TransferenciaContas transferenciaContas) {
//		try {
//			Movimentacao mov = movRepository.findByEmpresaAndStatus(transferenciaContas.getContaDestino().getEmpresa());
//			MovimentacaoBancaria movBanco = movBancariaRepository.findByMovimentacaoAndContaEmpresa(mov, transferenciaContas.getContaDestino());
//			movBanco.setValorCreditos(movBanco.getValorCreditos().add(transferenciaContas.getValor()));
//			movBancariaRepository.save(movBanco);
//		} catch (Exception e) {
//			LOG.error("Transferencia não efetuada. Erro ao incluir o credito no movimento bancário--" + e.getMessage()+"/"+e.getLocalizedMessage());	
//			throw new TransferenciaNaoEfetuadaException("Algo deu errado!! Transferencia não efetuada");
//		}
//	}	
	
	
//	private void debitarTransferenciaCreditosMovimentacao(TransferenciaContas transferenciaContas) {
//		Movimentacao mov = movRepository.findByEmpresaAndStatus(transferenciaContas.getContaDestino().getEmpresa());
//		MovimentacaoBancaria movBanco = movBancariaRepository.findByMovimentacaoAndContaEmpresa(mov, transferenciaContas.getContaDestino());
//		movBanco.setValorCreditos(movBanco.getValorCreditos().subtract(transferenciaContas.getValor()));
//		movBancariaRepository.save(movBanco);
//	}	
	

//	private void criarMovimentoNoExtrato(TransferenciaContas transferenciaContas, ContaEmpresa contaEmpresa, boolean b) {
//		ExtratoBancario extrato = new ExtratoBancario();
//		extrato.setData(transferenciaContas.getData());
//		extrato.setContaBancaria(contaEmpresa);
//		extrato.setCredito(b);
//		extrato.setHistorico("Transferencia nº: " + transferenciaContas.getContaOrigem().getId() +" Empresa: "+ transferenciaContas.getContaOrigem().getEmpresa().getFantasia() + " - Conta: "+ transferenciaContas.getContaOrigem().getNome());
//		extrato.setTransferencia(transferenciaContas);
//		extrato.setStatus("ABERTO");
//		extrato.setTipo("TRANSFERENCIA");
//		extrato.setMovimentacao(setarMovimentacao(contaEmpresa));
//		extrato.setValor(transferenciaContas.getValor());
//		extratoRepository.save(extrato);
//}	
	
//	
//	private void criarMovimentoNoExtratoOrigem(TransferenciaContas transferenciaContas) {
//		try {
//			ExtratoBancario extrato = new ExtratoBancario();
//			extrato.setData(transferenciaContas.getData());
//			extrato.setContaBancaria(transferenciaContas.getContaOrigem());
//			extrato.setCredito(false);
//			extrato.setHistorico("Transferencia nº "+transferenciaContas.getId()+" para: " + transferenciaContas.getContaDestino().getEmpresa().getFantasia() +" Conta nº: "+transferenciaContas.getContaDestino().getConta());
//			extrato.setTransferencia(transferenciaContas);
//			extrato.setStatus("CONFERIDO");
//			extrato.setTipo("TRANSFERENCIA");
//			extrato.setMovimentacao(transferenciaContas.getMovimentacao());
//			extrato.setValor(transferenciaContas.getValor());
//			debitarValorContaPendente(transferenciaContas);
//			extratoRepository.save(extrato);
//		} catch (Exception e) {
//			LOG.error("Transferencia não efetuada pois ocorreu um erro ao gerar o movimento no extrato.--"+e.getMessage());
//		}
//	}	
//
//	
//	private void debitarValorContaPendente(TransferenciaContas transferenciaContas) {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(transferenciaContas.getMovimentacao().getId());
//		movBanco.setTotalFinal(movBanco.getTotalFinal().subtract(transferenciaContas.getValor()));
//		movBancariaRepository.save(movBanco);
//	}
//
//
//
//	private void criarMovimentoNoExtratoDestino(TransferenciaContas transferenciaContas, MovimentacaoBancaria movBancariaDestino) {
//		try {
//			ExtratoBancario extrato = new ExtratoBancario();
//			extrato.setData(transferenciaContas.getData());
//			extrato.setContaBancaria(transferenciaContas.getContaDestino());
//			extrato.setCredito(true);
//			extrato.setHistorico("Transferencia nº "+transferenciaContas.getId()+" de: " + transferenciaContas.getContaOrigem().getEmpresa().getFantasia() +" Conta nº: "+transferenciaContas.getContaOrigem().getConta());
//			extrato.setTransferencia(transferenciaContas);
//			extrato.setStatus("CONFERIDO");
//			extrato.setTipo("TRANSFERENCIA");
//			extrato.setMovimentacao(movBancariaDestino);
//			extrato.setValor(transferenciaContas.getValor());
//			creditarRecebimentoMovimento(transferenciaContas, movBancariaDestino);
//			
//			extratoRepository.save(extrato);
//		} catch (Exception e) {
//			LOG.error("Transferencia não efetuada pois ocorreu um erro ao gerar o movimento no extrato.--"+e.getMessage());
//		}
//	}	
//	
//
//	
//	private void creditarRecebimentoMovimento(TransferenciaContas transferenciaContas,
//			MovimentacaoBancaria movBancariaDestino) {
//		MovimentacaoBancaria movBanco = movBancariaRepository.findOne(movBancariaDestino.getId());
//		movBanco.setTotalFinal(movBanco.getTotalFinal().add(transferenciaContas.getValor()));
//		movBancariaRepository.save(movBanco);
//	}
//


	public Page<TransferenciaContas> filtrar(TransferenciaFilter transferenciaFilter, Pageable pageable) {
		return repository.filtrar(transferenciaFilter, pageable);
	}

	
	public TransferenciaContas findOne(Long id) {
		return repository.findOne(id);
	}

	
	@Transactional
	public void excluir(Long id) {
		TransferenciaContas transferencia = repository.findOne(id);
		List<ExtratoBancario> extratos = extratoRepository.findByTransferencia(transferencia);
		try {
			for (ExtratoBancario extratoBancario : extratos) {
				extratoRepository.delete(extratoBancario);
			}
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a transferencia. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}
	
	
	@Transactional
	public void cancelarOuPagar(TransferenciaContas transferencia) {
		
		if(transferencia.isAberto()) {
			transferencia.setStatus("CONFERIDO");
			alterarStatusExtrato(transferencia, "CONFERIDO");
		}else {
			transferencia.setStatus("ABERTO");
			alterarStatusExtrato(transferencia, "ABERTO");
		}
		
		repository.save(transferencia);
	}



	private void alterarStatusExtrato(TransferenciaContas transferencia, String status) {
		List<ExtratoBancario> extratos = extratoRepository.findByTransferencia(transferencia);
		for (ExtratoBancario extratoBancario : extratos) {
			extratoBancario.setStatus(status);
			extratoRepository.save(extratoBancario);
		}
	}	

}
