package com.ajeff.simed.financeiro.service;

import java.time.LocalDate;
import java.util.ArrayList;
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
import com.ajeff.simed.financeiro.service.exception.ErroAoFecharMovimentacaoException;
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
	private ExtratoService extratoService;	
	@Autowired
	private MovimentacaoService movimentacaoService;
	@Autowired
	private MovimentacaoItensService movimentacaoItemService;
	
	
	@Transactional
	public void salvar(TransferenciaContas transferencia) {

		Movimentacao movimentacaoOrigem = setarMovimentacao(transferencia.getContaOrigem().getEmpresa(), transferencia.getData());
		Movimentacao movimentacaoDestino = setarMovimentacao(transferencia.getContaDestino().getEmpresa(), transferencia.getData());
		MovimentacaoItem movimentacaoItemOrigem = setarMovimentacaoItem(transferencia.getContaOrigem(), movimentacaoOrigem);
		MovimentacaoItem movimentacaoItemDestino = setarMovimentacaoItem(transferencia.getContaDestino(), movimentacaoDestino);

		try {
			if(transferencia.isNovo()) {
				emissaoTransferencia(transferencia, movimentacaoItemOrigem, movimentacaoItemDestino);
			}
			
		} catch (EntityNotFoundException e) {
			throw new TransferenciaNaoEfetuadaException("Não foi possível encontrar uma entidade no banco de dados");
		} catch (Exception e) {
			e.printStackTrace();
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
	
	
	private void emissaoTransferencia(TransferenciaContas transferencia, MovimentacaoItem movimentacaoItemOrigem, MovimentacaoItem movimentacaoItemDestino) {
		transferencia.setMovimentacaoItem(movimentacaoItemOrigem);
		transferencia.setStatus("ABERTO");
		transferencia.setFechado(false);
		atualizarValorNasContasDaMovimentacaoItem(transferencia, movimentacaoItemOrigem, movimentacaoItemDestino);
		repository.save(transferencia);
		criarMovimentacaoNoExtrato(transferencia, movimentacaoItemOrigem, movimentacaoItemDestino);
	}


	private void atualizarValorNasContasDaMovimentacaoItem(TransferenciaContas transferencia,
			MovimentacaoItem movimentacaoItemOrigem, MovimentacaoItem movimentacaoItemDestino) {
	
		
		MovimentacaoItem itemOrigem = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacaoItemOrigem.getMovimentacao(), transferencia.getContaOrigem());
		MovimentacaoItem itemDestino = movimentacaoItemService.findByMovimentacaoAndContaEmpresa(movimentacaoItemDestino.getMovimentacao(), transferencia.getContaDestino());
		if(transferencia.isNovo()) {
			movimentacaoItemService.creditarValorNosDebitos(itemOrigem, transferencia.getValor());
			movimentacaoItemService.creditarValorNosCreditos(itemDestino, transferencia.getValor());
		}else {
			movimentacaoItemService.debitarValorNosDebitos(itemOrigem, transferencia.getValor());
			movimentacaoItemService.debitarValorNosCreditos(itemDestino, transferencia.getValor());
		}
	}


	private void criarMovimentacaoNoExtrato(TransferenciaContas transferencia, MovimentacaoItem movimentacaoItemOrigem, MovimentacaoItem movimentacaoItemDestino) {
		criarMovimentoDaContaPorTipo(transferencia, false, transferencia.getContaOrigem(), movimentacaoItemOrigem);
		criarMovimentoDaContaPorTipo(transferencia, true, transferencia.getContaDestino(), movimentacaoItemDestino);
	}

	
	private void criarMovimentoDaContaPorTipo(TransferenciaContas transferencia, boolean credito, ContaEmpresa conta, MovimentacaoItem movimentacaoItem) {
		extratoService.criarMovimentoTransferenciaNoExtrato(transferencia.getValor(), transferencia, conta, credito, "ABERTO", transferencia.getData(), "TRANSFERENCIA", movimentacaoItem);
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
		MovimentacaoItem movimentacaoItemOrigem = movimentacaoItemService.findOne(transferencia.getMovimentacaoItem().getId());
		Movimentacao movimentacaoDestino = setarMovimentacao(transferencia.getContaDestino().getEmpresa(), transferencia.getData());
		MovimentacaoItem movimentacaoItemDestino = setarMovimentacaoItem(transferencia.getContaDestino(), movimentacaoDestino);

		atualizarValorNasContasDaMovimentacaoItem(transferencia, movimentacaoItemOrigem, movimentacaoItemDestino);
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
	
	
	private Boolean verificarSeTemTransferenciaAberto(MovimentacaoItem movimentacaoItem) {
		List<TransferenciaContas> transferencias = repository.findByMovimentacaoItem(movimentacaoItem);
		List<TransferenciaContas> transferenciasAbertas = new ArrayList<>();
		transferencias.stream().filter(i -> i.getStatus().equals("ABERTO")).forEach(transferenciasAbertas::add);
		return transferenciasAbertas.isEmpty() ? false: true;
	}


	public List<TransferenciaContas> findByMovimentacaoItem(MovimentacaoItem m) {
		return repository.findByMovimentacaoItem(m);
	}
	
	
	public void fecharTransferencias(MovimentacaoItem m) {
		if(verificarSeTemTransferenciaAberto(m)){
			throw new ErroAoFecharMovimentacaoException("Não foi possível fechar o movimento. Existe transferencia em aberto!");
		}else {
			List<TransferenciaContas> transferencias = repository.findByMovimentacaoItem(m);
			if(!transferencias.isEmpty()) {
				transferencias.stream().forEach(t -> t.setFechado(true));
				repository.save(transferencias);
			}
		}
	}

}
