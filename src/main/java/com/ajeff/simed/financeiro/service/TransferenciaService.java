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
