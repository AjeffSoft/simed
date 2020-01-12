package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.event.ContaPagarSalvoEvent;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.CalculosComDatas;

@Service
public class ContaPagarService {

	private static final Logger LOG = LoggerFactory.getLogger(ContaPagarService.class);

	
	@Autowired
	private ContasPagarRepository repository;
	@Autowired
	private ApplicationEventPublisher publisher;
//	@Autowired
//	private PlanoContaSecundariaService contaSecundariaService;
	@Autowired
	private ImpostoService impostoService;
	
	@Transactional
	public void salvar(ContaPagar contaPagar) {
		if (contaPagar.isNovo()) {
			novoRegistro(contaPagar);
		}else {
			testeEmissaoMaiorVencimento(contaPagar);
			repository.save(contaPagar);
		}
	}

	private void novoRegistro(ContaPagar contaPagar) {
		List<ContaPagar> novosRegistros = new ArrayList<>();
		if(contaPagar.getTotalParcela() == null || contaPagar.getTotalParcela() <= 0) {
			contaPagar.setTotalParcela(1);
		}
		for(int i = 1; i<= contaPagar.getTotalParcela(); i++) {
			novosRegistros.add(registroUnicoContaPagar(contaPagar, i , contaPagar.getTotalParcela()));
		}
		repository.save(novosRegistros);
		publisher.publishEvent(new ContaPagarSalvoEvent(contaPagar));
	}	

	
	private ContaPagar registroUnicoContaPagar(ContaPagar contaPagar, Integer parcela, Integer totalParcela) {
		BigDecimal impostoRetido = new BigDecimal(0);
		ContaPagar conta = new ContaPagar();
		conta.setDataEmissao(contaPagar.getDataEmissao());
		conta.setEmpresa(contaPagar.getEmpresa());
		conta.setStatus("ABERTO");
		conta.setNotaFiscal(contaPagar.getNotaFiscal());
		conta.setParcela(parcela);
		conta.setTotalParcela(totalParcela);
		conta.setPlanoContaSecundaria(contaPagar.getPlanoContaSecundaria());
		conta.setUpload(contaPagar.getUpload());
		conta.setContentType(contaPagar.getContentType());
		conta.setFornecedor(contaPagar.getFornecedor());
		conta.setHistorico(cadastroHistoricoDaConta(contaPagar));
		conta.setDocumento(numeroDocumento(contaPagar, parcela));
		conta.setVencimento(cadastroVencimento(contaPagar, parcela));
		
		if(contaPagar.existeRetencaoImpostos()) {
			if(parcela == 1) {
				conta.setImpostos(impostoService.retencaoImpostos(contaPagar, conta));
				impostoRetido = conta.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
			}
		}
		conta.setValor((contaPagar.getValor().subtract(impostoRetido)).divide(new BigDecimal(totalParcela),
			MathContext.DECIMAL32));
		contaPagar.setValor(contaPagar.getValor().subtract(impostoRetido));
		return conta;
	}


	
	private LocalDate cadastroVencimento(ContaPagar contaPagar, Integer parcela) {
		return CalculosComDatas.setarVencimento(contaPagar.getDataEmissao(), parcela, contaPagar.getPrazoParcelamento());
	}


	private String numeroDocumento(ContaPagar contaPagar, Integer parcela) {
		String novoDocumento = new String();
		if(contaPagar.getDocumento().isEmpty()) {
			if(!contaPagar.getNotaFiscal().isEmpty()) {
				novoDocumento = contaPagar.getFornecedor().getSigla() + "-" + contaPagar.getNotaFiscal() +"("+ parcela +"/" + contaPagar.getTotalParcela()+ ")";
			}else {
				LocalDateTime dataAtual = LocalDateTime.now();
				novoDocumento = contaPagar.getFornecedor().getSigla() + "-" + dataAtual.getDayOfMonth() +
						dataAtual.getMonthValue() + dataAtual.getYear() + dataAtual.getHour() +	dataAtual.getMinute()+
						dataAtual.getSecond()+parcela;
			}
		}else {
			novoDocumento = contaPagar.getDocumento();
		}
		if(novoDocumento.length() > 30) {
			LOG.error("LIMITE DE CARACTERE DO CAMPO DOCUMENTO EXCEDIDO!!!");
		}
		return novoDocumento;
	}
	
	
	private void testeEmissaoMaiorVencimento(ContaPagar contaPagar) {
		if (CalculosComDatas.emissaoMaiorVancimento(contaPagar.getDataEmissao(), contaPagar.getVencimento())) {
			throw new VencimentoMenorEmissaoException(
					"A data de vencimento não pode ser menor que a data de emissão");
		}
	}

	
	private String cadastroHistoricoDaConta(ContaPagar contaPagar){
		if(contaPagar.getHistorico().isEmpty() && contaPagar.getPlanoContaSecundaria().getId() != null) {
			return contaPagar.getPlanoContaSecundaria().getNome();
		}else {
			return contaPagar.getHistorico();
		}	
	}	
	
	
//	private void testeRegistroJaCadastrado(ContaPagar contaPagar) {
//		Optional<ContaPagar> optional = repository.findByDocumentoAndFornecedor(contaPagar.getDocumento(), contaPagar.getFornecedor());
//		if (optional.isPresent() && !optional.get().equals(contaPagar)) {
//			throw new DocumentoEFornecedorJaCadastradoException(
//					"Já existe uma conta com este documento e fornecedor cadastrado!");
//		}
//	}

	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir!"  
					+ " Talvez possua algum relacionamento de tabela ativo ou acesso não autorizado!");
		}
	}
	
	//EXTRAIR PARA METODO STATIC
	public LocalDate dataUtil(LocalDate dataApuracao, Integer n) {
		LocalDate primeiroDiaMesSeguinte = dataApuracao.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate vencimento = primeiroDiaMesSeguinte.plusDays(n);
		DayOfWeek diaSemana = vencimento.getDayOfWeek();
		if(diaSemana.getValue() == 0) {
			vencimento = vencimento.minusDays(2);
		}else if (diaSemana.getValue() == 6) {
			vencimento = vencimento.minusDays(1);
		}
		return vencimento;
	}
	
	public Page<ContaPagar> filtrar(ContaPagarFilter contaPagarFilter, Pageable pageable) {
		return repository.filtrar(contaPagarFilter, pageable);
	}

	public Page<ContaPagar> filtrarAutorizar(ContaPagarFilter contaPagarFilter, Pageable pageable) {
		return repository.filtrarAutorizar(contaPagarFilter, pageable);
	}

	public Page<ContaPagar> filtrarAutorizadas(ContaPagarFilter contaPagarFilter, Pageable pageable) {
		return repository.filtrarAutorizadas(contaPagarFilter, pageable);
	}
	
	
	public ContaPagar findOne(Long id) {
		return repository.findOne(id);
	}

	public ContaPagar findOne1(Long id) {
		ContaPagar cp = repository.findOne(id);
		return cp;
	}
	
	
	public BigDecimal calculaTest(BigDecimal v, BigDecimal p) {
		return v.multiply(p);
	}

	public ContaPagar buscarComPlanoConta(Long id) {
		return repository.buscarComPlanoConta(id);
	}

	public BigDecimal total(ContaPagarFilter contaPagarFilter) {
		return repository.total(contaPagarFilter);
	}

	@Transactional
	public void autorizarPagamento(ContaPagar contaPagar) {
		contaPagar.setStatus("AUTORIZADO");
		repository.save(contaPagar);
	}

	@Transactional	
	public void cancelarAutorizarPagamento(ContaPagar contaPagar) {
		contaPagar.setStatus("ABERTO");
		repository.save(contaPagar);
	}

	public List<ContaPagar> buscarTodasContasAutorizadas() {
		return repository.buscarTodasContasAutorizadas();
	}


	public List<ContaPagar> buscarContasPagarSelecionadas(List<Long> ids) {
		return repository.buscarContasPagarSelecionadas(ids);
	}

	public List<ContaPagar> findByPagamentoId(Long id) {
		return repository.findByPagamentoId(id);
	}

}
