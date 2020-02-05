package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.ExtratosRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesBancariasRepository;
import com.ajeff.simed.financeiro.repository.MovimentacoesRepository;
import com.ajeff.simed.financeiro.repository.PagamentosRepository;
import com.ajeff.simed.financeiro.repository.TransferenciasContasRepository;
import com.ajeff.simed.financeiro.repository.filter.MovimentacaoFilter;
import com.ajeff.simed.financeiro.service.exception.ErroAoFecharMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.MovimentacaoFechadaException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.ContaEmpresaRepository;

@Service
public class MovimentacaoService {

	private static final Logger LOG = LoggerFactory.getLogger(MovimentacaoService.class);	
	
	@Autowired
	private MovimentacoesRepository repository;
	@Autowired
	private ContaEmpresaRepository contaRepository;
	@Autowired
	private MovimentacoesBancariasRepository movBancarioRepository;
	@Autowired
	private ExtratosRepository extratoRepository;
	@Autowired
	private PagamentosRepository pagamentosRepository;
	@Autowired
	private TransferenciasContasRepository transferenciaRepository;

	

	@Transactional
	public void salvar(Movimentacao movimentacao) {
		testeRegistroJaCadastrado(movimentacao);
		movimentacao.setFechado(false);
		movimentacao.setTotalCreditos(BigDecimal.ZERO);;
		movimentacao.setTotalDebitos(BigDecimal.ZERO);;
		movimentacao.setSaldoMovimento(BigDecimal.ZERO);;
		repository.save(movimentacao);
	}

	
	public void verificarSeMovimentacaoEstaFechado(Empresa empresa, LocalDate data) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(empresa);	
		if(!mov.isPresent()) {
			throw new MovimentacaoFechadaException("Não existe movimentação em aberto para a empresa: " + empresa.getFantasia());
		}
			
		if (data.isBefore(mov.get().getDataInicio()) || data.isAfter(mov.get().getDataFinal())){
			throw new MovimentacaoFechadaException("A data informada esta fora do limite do movimento em aberto: " + mov.get().getDataInicio().format(formatter) + " à "+ mov.get().getDataFinal().format(formatter));
		}
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
	
	
	

	

//	private void criarRegistroMovimentacaoBancaria(Movimentacao movimentacao) {
//		List<ContaEmpresa> contasBancarias = contaRepository.findByEmpresa(movimentacao.getEmpresa());
//		
//		for(ContaEmpresa c : contasBancarias) {
//			criarMovimentoAberturaNoExtrato(unicoRegistroDeMovimentacaoBancaria(movimentacao, c));
//		}
//		movimentacao.setTotalAbertura(contasBancarias.stream().map(m-> m.getSaldo()).reduce(BigDecimal.ZERO, BigDecimal::add));
//	}


//	//Unico registro de movimentação bancária - para persistir
//	private MovimentacaoBancaria unicoRegistroDeMovimentacaoBancaria(Movimentacao mov, ContaEmpresa c) {
//		MovimentacaoBancaria movBanco = new MovimentacaoBancaria();
//		movBanco.setValorAbertura(c.getSaldo()); //Pega o saldo gravado nas contas da empresa
//		movBanco.setValorCreditos(BigDecimal.ZERO);
//		movBanco.setValorDebitos(BigDecimal.ZERO);
//		movBanco.setSaldoPeriodo(BigDecimal.ZERO);
//		movBanco.setSaldoGeral(BigDecimal.ZERO);
//		movBanco.setValorPendente(c.getValorPendente());
//		movBanco.setContaEmpresa(c);
//		movBanco.setMovimentacao(mov);
//		return movBancarioRepository.save(movBanco);
//	}
	
//	//Cria o movimento de abertura no extrato de cada conta da empresa
//	private void criarMovimentoAberturaNoExtrato(MovimentacaoBancaria movimentacao) {
//		ExtratoBancario ex = new ExtratoBancario();
//		ex.setContaBancaria(movimentacao.getContaEmpresa());
//		ex.setValor(BigDecimal.ZERO);
//		ex.setCredito(true);
//		ex.setTipo("SALDO INICIAL");
//		ex.setData(movimentacao.getMovimentacao().getDataInicio());
//		ex.setHistorico("Saldo inicial do movimento nº: " + movimentacao.getId());
//		ex.setMovimentacao(movimentacao);
//		ex.setSaldo(movimentacao.getValorAbertura());
//		ex.setStatus("CONFERIDO");
//		extratoRepository.save(ex);
//	}

	
	@Transactional
	public void excluir(Long id) {
		Movimentacao movimentacao = repository.findOne(id);
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a movimentação nº "+movimentacao.getId()+".\nTalvez esteja vinculada a outras tabelas!"); 
		}
	}	

	
	@Transactional
	public void fechar(Movimentacao movimentacao) {
		try {
			List<MovimentacaoBancaria> movBancarios = movBancarioRepository.findByMovimentacao(movimentacao);
			
			List<Pagamento> pgtosAberto = new ArrayList<>();
			List<TransferenciaContas> transfAberto = new ArrayList<>();
			
			for (MovimentacaoBancaria m : movBancarios) {
				//movimentacaoDoMovBancario(m); //Pagamentos, recebimentos e transferencias
				verificarSeTemPagamentoAberto(m, pgtosAberto);
				verificarSeTemTransferenciaAberto(m, transfAberto);
				m.setSaldoPeriodo(calculaSaldoDoPeriodo(m));
				m.setSaldoGeral(calculaSaldoGeral(m));
				atualizarSaldoContaEmpresa(m);
				//atualizarSaldoPendenteContaEmpresa(m);				
			}
			calcularTotaisMovimentacao(movimentacao, movBancarios);
			movimentacao.setDataFechamento(LocalDate.now());
			movimentacao.setFechado(true);
			repository.save(movimentacao);
		} catch (Exception e) {
			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado" + e.getMessage());
		}
	}
	
	
	private void verificarSeTemPagamentoAberto(MovimentacaoBancaria m, List<Pagamento> pgtosAberto) {
		List<Pagamento> pgtos = pagamentosRepository.findByMovimentacao(m);
		List<Pagamento> pgtosParaFechar = new ArrayList<>();
		for (Pagamento p : pgtos) {
			if (!p.isCheque() && p.getStatus().equals("EMITIDO")) {
				pgtosAberto.add(p);
			}else {
				p.setFechado(true);
			}
		}
		if(!pgtosAberto.isEmpty()) {
			LOG.error("Erro - Não foi possível fechar, pois há pagamentos abertos");
			throw new ErroAoFecharMovimentacaoException("Não foi possível fechar o movimento!");
		}else {
			pagamentosRepository.save(pgtosParaFechar);
		}
	}


	private void verificarSeTemTransferenciaAberto(MovimentacaoBancaria m, List<TransferenciaContas> transfAberto) {
		List<TransferenciaContas> transfs = transferenciaRepository.findByMovimentacao(m);
		List<TransferenciaContas> transfParaFechar = new ArrayList<>();
		for (TransferenciaContas t : transfs) {
			if (t.getStatus().equals("ABERTO")) {
				transfAberto.add(t);
			}else {
				t.setFechado(true);
			}
		}
		if(!transfAberto.isEmpty()) {
			LOG.error("Erro - Não foi possível fechar, pois há transferencias abertas");
			throw new ErroAoFecharMovimentacaoException("Não foi possível fechar o movimento!");
		}else {
			transferenciaRepository.save(transfParaFechar);
		}
	}

	
	private void calcularTotaisMovimentacao(Movimentacao movimentacao, List<MovimentacaoBancaria> movBancarios) {
		try {
//			movimentacao.setTotalCreditos(calcularTotalCreditosDoMovimento(movBancarios));
//			movimentacao.setTotalDebitos(calcularTotalDebitosDoMovimento(movBancarios));
//			movimentacao.setTotalPeriodo(calcularSaldoPeriodoDoMovimento(movBancarios));
//			movimentacao.setTotalPendente(calcularTotalPendenteDoMovimento(movimentacao));
//			movimentacao.setTotalCreditos(calcularTotalCreditosDoMovimento(movBancarios));
//			movimentacao.setTotalGeral(calcularTotalGeralDoMovimento(movBancarios));
		} catch (Exception e) {
			LOG.error("***** Erro ao fechar calcular os totais da movimentação *****" + e.getMessage());
			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado" + e.getMessage());
		}
	}



	private BigDecimal calcularTotalCreditosDoMovimento(List<MovimentacaoBancaria> movBancarios) {
		return movBancarios.stream().map(m->m.getValorCreditos()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calcularTotalDebitosDoMovimento(List<MovimentacaoBancaria> movBancarios) {
		return movBancarios.stream().map(m->m.getValorDebitos()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calcularSaldoPeriodoDoMovimento(List<MovimentacaoBancaria> movBancarios) {
		return movBancarios.stream().map(m->m.getSaldoPeriodo()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calcularTotalPendenteDoMovimento(Movimentacao movimentacao) {
		List<ContaEmpresa> contas = contaRepository.findByEmpresa(movimentacao.getEmpresa());
		return contas.stream().map(m->m.getChequePendente()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	private BigDecimal calcularTotalGeralDoMovimento(List<MovimentacaoBancaria> movBancarios) {
		return movBancarios.stream().map(m->m.getSaldoGeral()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
	
	private BigDecimal calculaSaldoDoPeriodo(MovimentacaoBancaria m) {
		try {
			return m.getValorCreditos().subtract(m.getValorDebitos()); 
		} catch (Exception e) {
			LOG.error("***** Erro ao calcular o saldo do período *****" + e.getMessage());
			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado." + e.getMessage());
		}
	}	
	
	//Saldo Geral = SaldoAbertura + SaldoPeriodo
	private BigDecimal calculaSaldoGeral(MovimentacaoBancaria m) {
		try {
			return m.getValorAbertura().add(m.getSaldoPeriodo()); 
		} catch (Exception e) {
			LOG.error("***** Erro ao calcular o saldo geral (Abertura + Periodo) *****" + e.getMessage());
			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado." + e.getMessage());
		}
	}	
	
	private void atualizarSaldoContaEmpresa(MovimentacaoBancaria m) {
		try {
			ContaEmpresa conta = contaRepository.findOne(m.getContaEmpresa().getId());
			conta.setSaldo(m.getSaldoGeral());
			contaRepository.save(conta);
		} catch (Exception e) {
			LOG.error("***** Erro ao atualizar o saldo da conta empresa *****" + e.getMessage());
			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado" + e.getMessage());
		}
	}


//	private void atualizarSaldoPendenteContaEmpresa(MovimentacaoBancaria m) {
//		try {
//			ContaEmpresa conta = contaRepository.findOne(m.getContaEmpresa().getId());
//			 conta.setValorPendente(m.getValorPendente());
//			contaRepository.save(conta);
//		} catch (Exception e) {
//			LOG.error("***** Erro ao atualizar o saldo do valor pendente da conta empresa *****" + e.getMessage());
//			throw new ErroAoFecharMovimentacaoException("Algo deu errado!! Fechamento não efetuado" + e.getMessage());
//		}
//	}

//	private void movimentacaoDoMovBancario(MovimentacaoBancaria m) {
//		setarPagamentoComoFechado(m);
//		verificarSeTemPagamentoAbertos(m);
//		verificarSeTemTransferenciasAbertas(m);
		
		
//		///////////////
//		List<Pagamento> pgtos = pagamentosRepository.findByMovimentacao(mb);
//		List<Pagamento> pgtosFechados = new ArrayList<>();
//		
//		for (Pagamento pg : pgtos) {
//			if(pg.getTipo().equals(TipoPagamento.CHEQUE) && pg.getStatus().equals("ABERTO")) {
//				pg.setSituacao(false);
//			}else {
//				pg.setSituacao(true); //SITUAÇÃO TRUE: MOVIMENTO FECHADO
//			}
//			pgtosFechados.add(pg);
//		}
//		pagamentosRepository.save(pgtosFechados);
//		///////////////		
//	}



//	
//	
//	
////	public void fechar(Long id) {
////		Movimentacao mov = repository.findOne(id);
////		
////		try {
////			for(MovimentacaoBancaria m : movBancarios) {
////				ContaEmpresa contaEmp = contaRepository.findOne(m.getContaEmpresa().getId());
////				m.setTotalFinal(contaEmp.getSaldo());
////				m.setSaldoTotal(m.getTotalInicio().subtract(m.getTotalFinal()));
////				m.setTotalPendente(contaEmp.getValorPendente());
////			}
////			mov.setTotalFinal(movBancarios.stream().map(m-> m.getTotalFinal()).reduce(BigDecimal.ZERO, BigDecimal::add));
////			mov.setTotalPendente(movBancarios.stream().map(m-> m.getTotalPendente()).reduce(BigDecimal.ZERO, BigDecimal::add));
////			mov.setSaldoTotal(mov.getTotalInicio().subtract(mov.getTotalFinal()));
////			mov.setStatus("FECHADO");
////			mov.setDataFechamento(LocalDate.now());
////			repository.save(mov);
////		} catch (PersistenceException e) {
////			throw new ImpossivelExcluirEntidade("Não foi possivel fechar o movimento!"); 
////		}
////	}	
	
	
	private void testeRegistroJaCadastrado(Movimentacao movimentacao) {
		Optional<Movimentacao> movAberto = repository.findByEmpresaAndStatusAberto(movimentacao.getEmpresa());

		if(movAberto.isPresent() && !movAberto.get().equals(movimentacao)) {
			throw new RegistroJaCadastradoException("Existe um movimento em aberto para esta empresa!");
		}
	}

	
	public Movimentacao findOne(Long id) {
		return repository.findOne(id);
	}
	
	
	public Page<Movimentacao> filtrar(MovimentacaoFilter movimentacaoFilter, Pageable pageable) {
		return repository.filtrar(movimentacaoFilter, pageable);
	}	

//	
//	public Boolean verificarSeTemMovimentacaoBancariaAberta(Empresa empresa) {
//		List<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(empresa);	
//		return mov.size() >= 1 ? true : false;	
//	}	
//	
//	public Boolean verificarSeDataPagamentoEstaDentroPeriodoAberto(Movimentacao movimentacao, LocalDate data) {
//		return data.isBefore(movimentacao.getDataInicio()) || data.isAfter(movimentacao.getDataFinal());
//	}	
	
	public Boolean verificarSeMovimentacaoEstaAberto(Pagamento pagamento) {
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(pagamento.getContaEmpresa().getEmpresa());	
		return mov.isPresent();
	}	

	
	public Boolean verificarSeDataPagamentoEstaForaPeriodoAberto(Pagamento pagamento, LocalDate data) {
		Optional<Movimentacao> mov = repository.findByEmpresaAndStatusAberto(pagamento.getContaEmpresa().getEmpresa());	
		return data.isBefore(mov.get().getDataInicio()) || data.isAfter(mov.get().getDataFinal());
	}

	public Boolean isAberto(Movimentacao movimentacao) {
		return movimentacao.getFechado();
	}

	public Movimentacao findByEmpresaAnStatusAtivo(Empresa empresa) {
		return repository.findByEmpresaAndStatus(empresa);
	}

	
}
