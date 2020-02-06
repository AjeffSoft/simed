package com.ajeff.simed.financeiro.service;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.TransferenciasContasRepository;
import com.ajeff.simed.financeiro.repository.filter.TransferenciaFilter;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.TransferenciaNaoEfetuadaException;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;

@Service
public class TransferenciaService {

	
	@Autowired
	private TransferenciasContasRepository repository;
	@Autowired
	private ExtratosRepository extratoRepository;
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private MovimentacaoItensService movimentacaoItemService;
	
	
	@Transactional
	public void salvar(TransferenciaContas transferencia) {

		Movimentacao movimentacaoOrigem = setarMovimentacao(transferencia.getContaOrigem().getEmpresa(), transferencia.getData());
		@SuppressWarnings("unused")
		Movimentacao movimentacaoDestino = setarMovimentacao(transferencia.getContaDestino().getEmpresa(), transferencia.getData());
		MovimentacaoItem movimentacaoItemOrigem = setarMovimentacaoItem(transferencia.getContaOrigem(), movimentacaoOrigem);

		try {
			if(transferencia.isNovo()) {
				emissaoTransferencia(transferencia, movimentacaoItemOrigem);
			}
			
		} catch (EntityNotFoundException e) {
			throw new TransferenciaNaoEfetuadaException("Não foi possível encontrar uma entidade no banco de dados");
		} catch (Exception e) {
			throw new TransferenciaNaoEfetuadaException("Algo deu errado!! Transferencia não efetuada");
		}
					
	}
	
	
	private MovimentacaoItem setarMovimentacaoItem(ContaEmpresa contaEmpresa, Movimentacao movimentacao) {
		MovimentacaoItem movimentacaoItem = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacao, contaEmpresa);
		return movimentacaoItem;
	}


	private Movimentacao setarMovimentacao(Empresa empresa, LocalDate data) {
		Movimentacao movimentacao = movimentacaoService.verificarSeMovimentacaoEstaFechado(empresa, data);
		return movimentacao;
	}	
	
	
	private void emissaoTransferencia(TransferenciaContas transferencia, MovimentacaoItem movimentacaoItem) {
		transferencia.setMovimentacaoItem(movimentacaoItem);
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
		Extrato extrato = new Extrato();
		extrato.setData(transferencia.getData());
		extrato.setCredito(tipo);
		extrato.setHistorico("TRANSFERENCIA Nº " + transferencia.getId() +" ENTRE AS CONTAS: "+ transferencia.getContaOrigem().getNome()+" E: "+ transferencia.getContaDestino().getNome() );
		extrato.setTransferencia(transferencia);
		extrato.setStatus("ABERTO");
		extrato.setTipo("TRANSFERENCIA");
		extrato.setMovimentacaoItem(transferencia.getMovimentacaoItem());
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
		List<Extrato> extratos = extratoRepository.findByTransferencia(transferencia);
		try {
			for (Extrato extratoBancario : extratos) {
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
		List<Extrato> extratos = extratoRepository.findByTransferencia(transferencia);
		for (Extrato extratoBancario : extratos) {
			extratoBancario.setStatus(status);
			extratoRepository.save(extratoBancario);
		}
	}	

}
