package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.filter.MovimentacaoFilter;
import com.ajeff.simed.financeiro.service.exception.DataForaMovimentacaoAbertaException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.MovimentacaoFechadaException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.model.Empresa;

@Service
public class MovimentacaoService {

	@Autowired
	private MovimentacoesRepository repository;
	@Autowired
	private MovimentacaoItensService movimentacaoItensService;

	
	
	@Transactional
	public void salvar(Movimentacao movimentacao) {
		testeRegistroJaCadastrado(movimentacao);
		movimentacao.setFechado(false);
		movimentacao.setTotalCreditos(BigDecimal.ZERO);
		movimentacao.setTotalDebitos(BigDecimal.ZERO);
		movimentacao.setSaldoMovimento(BigDecimal.ZERO);
		movimentacao.setSaldoGeral(BigDecimal.ZERO);
		movimentacao.setItens(criarItensDeMovimentacao(movimentacao));
		movimentacao.setSaldoInicial(movimentacao.getItens().stream().map(i -> i.getSaldoInicial())
				.reduce(BigDecimal.ZERO, BigDecimal::add));
		repository.save(movimentacao);
	}
	
	
	public Boolean verificarSeMovimentacaoEstaAberta(Empresa empresa) {
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(empresa);
		return mov.isPresent();
	}
	
	
	public Movimentacao verificarSeMovimentacaoEstaFechado(Empresa empresa, LocalDate data) {
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(empresa);	

		if(!mov.isPresent()) {
			throw new MovimentacaoFechadaException("Não existe movimentação em aberto para a empresa: " + empresa.getFantasia());
		}else if (!mov.get().isDataDentroMovimento(data)){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			throw new DataForaMovimentacaoAbertaException("A data informada esta fora do limite do movimento em aberto: " 
			+ mov.get().getDataInicio().format(formatter) + " à "+ mov.get().getDataFinal().format(formatter));
		}else {
			Movimentacao movimentacao = repository.findByEmpresaAndStatus(empresa);
			return movimentacao;
		}
	}
	

	private List<MovimentacaoItem> criarItensDeMovimentacao(Movimentacao movimentacao) {
		return movimentacaoItensService.criarItensDeMovimentacaoPorContaDeEmpresas(movimentacao);
	}
	

	public void creditarValor(Movimentacao movimentacao, BigDecimal valor) {
		movimentacao.setTotalCreditos(movimentacao.getTotalCreditos().add(valor));
		repository.save(movimentacao);
	}

	public void cancelarCredito(Movimentacao movimentacao, BigDecimal valor) {
		movimentacao.setTotalCreditos(movimentacao.getTotalCreditos().subtract(valor));
		repository.save(movimentacao);
	}

	public void debitarValor(Movimentacao movimentacao, BigDecimal valor) {
		movimentacao.setTotalDebitos(movimentacao.getTotalDebitos().add(valor));
		repository.save(movimentacao);
	}

	public void cancelarDebito(Movimentacao movimentacao, BigDecimal valor) {
		movimentacao.setTotalDebitos(movimentacao.getTotalDebitos().subtract(valor));
		repository.save(movimentacao);
	}

	@Transactional
	public void excluir(Long id) {
		Movimentacao movimentacao = repository.findOne(id);
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a movimentação nº " + movimentacao.getId()
					+ ".\nTalvez esteja vinculada a outras tabelas!");
		}
	}

	@Transactional
	public void fecharMovimento(Movimentacao movimentacao) {
		movimentacaoItensService.fecharTransacoes(movimentacao);
		movimentacaoItensService.atualizarSaldoContaBancaria(movimentacao);
		movimentacaoItensService.fecharCalculosEValores(movimentacao);
		movimentacao.setFechado(true);
		movimentacao.setDataFechamento(LocalDate.now());
		repository.save(movimentacao);
	}

	
	private void testeRegistroJaCadastrado(Movimentacao movimentacao) {
		Optional<Movimentacao> movAberto = repository.findByEmpresaAndStatusAberto(movimentacao.getEmpresa());

		if (movAberto.isPresent() && !movAberto.get().equals(movimentacao)) {
			throw new RegistroJaCadastradoException("Existe um movimento em aberto para esta empresa!");
		}
	}

	public Movimentacao findOne(Long id) {
		return repository.findOne(id);
	}

	public Page<Movimentacao> filtrar(MovimentacaoFilter movimentacaoFilter, Pageable pageable) {
		return repository.filtrar(movimentacaoFilter, pageable);
	}


	public Boolean verificarSeDataPagamentoEstaForaPeriodoAberto(Pagamento pagamento, LocalDate data) {
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(pagamento.getContaEmpresa().getEmpresa());
		return data.isBefore(mov.get().getDataInicio()) || data.isAfter(mov.get().getDataFinal());
	}

	public Boolean isAberto(Movimentacao movimentacao) {
		return movimentacao.getFechado();
	}

	public Movimentacao findByEmpresaAndStatus(Empresa empresa) {
		return repository.findByEmpresaAndStatus(empresa);
	}

	public List<Movimentacao> findByEmpresaId(Long codigoEmpresa) {
		return repository.findByEmpresaId(codigoEmpresa);
	}
}
