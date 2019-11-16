package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.event.ContaPagarSalvoEvent;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.exception.IdPlanoContaSecundariaNuloException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.CalculosImpostos;

@Service
public class ContaPagarService {

	private static final Logger LOG = LoggerFactory.getLogger(ContaPagarService.class);

	
	@Autowired
	private ContasPagarRepository repository;
	@Autowired
	private ApplicationEventPublisher publisher;
	@Autowired
	private PlanoContaSecundariaService contaSecundariaService;
	
	@Transactional
	public void salvar(ContaPagar contaPagar) {
		if (contaPagar.isNovo()) {
			novoRegistro(contaPagar);
		}else {
			testeVencimentoMaiorEmissao(contaPagar);
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
				conta.setImpostos(retencaoImpostos(contaPagar, conta));
				impostoRetido = conta.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
			}
		}
		conta.setValor((contaPagar.getValor().subtract(impostoRetido)).divide(new BigDecimal(totalParcela),
			MathContext.DECIMAL32));
		contaPagar.setValor(contaPagar.getValor().subtract(impostoRetido));
		return conta;
	}

/* ************************** RETENÇÃO DE IMPOSTOS ***************/
	
	private List<Imposto> retencaoImpostos(ContaPagar contaPagar, ContaPagar conta) {
		String tipoImposto = new String();
		List<Imposto> impostos = new ArrayList<>();
		if(contaPagar.getReterIR()) {
			tipoImposto = "IR";
			impostos.add(calculoImpostoRenda(contaPagar, conta, tipoImposto));
		}	
		if(contaPagar.getFornecedor().getTipo().equals("J")) {
			
			if(contaPagar.getReterCOFINS()) {
				tipoImposto = "PCCS";
				impostos.add(impostoPCCS(contaPagar, conta, tipoImposto));
			}
		}
		if(contaPagar.getFornecedor().getTipo().equals("F")) {
			if(contaPagar.getReterINSS()) {
				tipoImposto = "INSS";
				impostos.add(retencaoINSS(contaPagar, conta, tipoImposto));
			}
		}	
		if(contaPagar.getReterISS()) {
			tipoImposto = "ISS";
			impostos.add(impostoISS(contaPagar, conta, tipoImposto));
		}
		impostos.forEach(i -> System.out.println(i));
		return impostos;
	}
	
	private Imposto impostoISS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
		conta.setValorIss(imposto.getValor());
		conta.setReterISS(true);
		return imposto;
	}


	private Imposto retencaoINSS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
		conta.setValorInss(imposto.getValor());
		conta.setReterINSS(true);
		return imposto;
	}



	private Imposto impostoPCCS(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
		conta.setValorCofins(imposto.getValor());
		conta.setReterCOFINS(true);
		return imposto;
	}


	private Imposto calculoImpostoRenda(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = impostoRegistroUnico(contaPagar, conta, tipoImposto);
		if(imposto.getValor().compareTo(BigDecimal.ZERO) == 1) {
			conta.setValorIR(imposto.getValor());
			conta.setReterIR(true);
		}
		return imposto;
	}

	private Imposto impostoRegistroUnico(ContaPagar contaPagar, ContaPagar conta, String tipoImposto) {
		Imposto imposto = new Imposto();
		imposto.setApuracao(contaPagar.getDataEmissao().minusMonths(0).with(TemporalAdjusters.lastDayOfMonth()));
		imposto.setContaPagarOrigem(conta);
		imposto.setJuros(BigDecimal.ZERO);
		imposto.setMulta(BigDecimal.ZERO);
		imposto.setValorNF(contaPagar.getValor());
		imposto.setNumeroNF(contaPagar.getNotaFiscal());
		imposto.setStatus("ABERTO");
		imposto.setEmissaoNF(contaPagar.getDataEmissao());
		imposto.setEmpresa(contaPagar.getEmpresa());
		imposto.setUpload(contaPagar.getUpload());
		imposto.setContentType(contaPagar.getContentType());
		if(tipoImposto.equals("IR")) {
			if(contaPagar.getFornecedor().getTipo().equals("J")) {
				imposto.setValor(calculoImpostoRendaPJ(contaPagar));
				imposto.setCodigo("1708");
				imposto.setNome("IRRF");
			}else {
				imposto.setValor(calculoImpostoRendaPF(contaPagar));
				imposto.setCodigo("0588");
				imposto.setNome("IRRF");
			}
			imposto.setVencimento(dataUtil(imposto.getApuracao(), 19));
		}else if(tipoImposto.equals("PCCS")) {
			imposto.setValor(CalculosImpostos.calculoPCCS(contaPagar.getValor()));
			imposto.setCodigo("5952");
			imposto.setNome("PIS/COFINS/CSLL");
			imposto.setVencimento(dataUtil(imposto.getApuracao(), 19));
		}else if(tipoImposto.equals("INSS")) {
			imposto.setValor(CalculosImpostos.calculoINSS(contaPagar.getValor()));
			imposto.setCodigo("INSS");
			imposto.setNome("INSS");
			imposto.setVencimento(dataUtil(imposto.getApuracao(), 14));
		}else {
			imposto.setValor(CalculosImpostos.calculoISS(contaPagar.getValor(), contaPagar.getIssPorcentagem()));
			imposto.setCodigo("ISS");
			imposto.setNome("ISS");
			imposto.setVencimento(dataUtil(imposto.getApuracao(), 9));
		}
		imposto.setHistorico(imposto.getNome() + " - " + conta.getFornecedor().getFantasia() + " - " + imposto.getNumeroNF());
		imposto.setTotal(imposto.getValor());
		return imposto;
	}

	private BigDecimal calculoImpostoRendaPJ(ContaPagar contaPagar) {
		//2000 * 1,5% = 30,00
		return contaPagar.getValor()
				.multiply(new BigDecimal(1.5))
				.divide(new BigDecimal(100), MathContext.DECIMAL32);
	}
	
	private BigDecimal calculoImpostoRendaPF(ContaPagar contaPagar) {
		BigDecimal deducaoPorDependente = new BigDecimal(0);
		BigDecimal valorBruto = contaPagar.getValor();
		BigDecimal totalIR = new BigDecimal(0);
		// TODO : OPÇÃO DE CADASTRO DA TABELA DE IMPOSTO DE RENDA PELO BANCO DE DADOS
		/* TABELA IR EM  13/04/2019
		*FAIXA 1 = 1.903,99 A 2.826,65 ALIQUOTA 7,5 DEDUZIR 142,80
		*FAIXA 2 = 2.826,66 A 3.751,05 ALIQUOTA 15 DEDUZIR 354,80
		*FAIXA 3 = 3.751,06 A 4.664,68 ALIQUOTA 22,5 DEDUZIR 636,13
		*FAIXA 4 = 4.664,69 A ........ ALIQUOTA 27,5 DEDUZIR 869,36
		*DEDUÇÃO POR DEPENDENTE: 189,50
		*/
		//SUBTRAIR DO VALOR DA CONTA A PAGAR O VALÇOR DA RETENÇÃO DE DEPENDENTES
		// VALOR ORIGINAL 2.000,00 - POSSUI 1 DEPENDENTE (-189,50) VALOR ATUAL: 1.810,50
		if(contaPagar.getFornecedor().getDependente() != null && contaPagar.getFornecedor().getDependente().compareTo(BigDecimal.ZERO) ==1) {
			if(contaPagar.getFornecedor().getDependente().compareTo(BigDecimal.ZERO) == 1) {
				deducaoPorDependente = contaPagar.getFornecedor().getDependente().multiply(new BigDecimal(189.50));
				valorBruto = valorBruto.subtract(deducaoPorDependente);
			}
		}
		if(contaPagar.getReterINSS()) {
			valorBruto = valorBruto.subtract(CalculosImpostos.calculoINSS(contaPagar.getValor()));
		}
		//FAIXA 1
		if(valorBruto.compareTo(new BigDecimal(1903.99)) >= 1 && valorBruto.compareTo(new BigDecimal(2826.65)) <= -1){
			totalIR = calculoIRPFFaixa1(valorBruto, totalIR);
		}
		//FAIXA 2
		if(valorBruto.compareTo(new BigDecimal(2826.66)) >= 1 && valorBruto.compareTo(new BigDecimal(3751.05)) <= -1){
			totalIR = calculoIRPFFaixa2(valorBruto, totalIR);
		}
		//FAIXA 3
		if(valorBruto.compareTo(new BigDecimal(3751.06)) >= 1 && valorBruto.compareTo(new BigDecimal(4664.68)) <= -1){
			totalIR = calculoIRPFFaixa3(valorBruto, totalIR);
		}
		//FAIXA 4
		if(valorBruto.compareTo(new BigDecimal(4664.69)) >= 1){
			totalIR = calculoIRPFFaixa4(valorBruto, totalIR);
		}
		return totalIR;
	}

	private BigDecimal calculoIRPFFaixa1(BigDecimal valorBruto, BigDecimal totalIR) {
		return totalIR = totalIR.add(valorBruto
				.multiply(new BigDecimal(7.5))
				.divide(new BigDecimal(100))
				.subtract(new BigDecimal(142.80), MathContext.DECIMAL32));
	}

	private BigDecimal calculoIRPFFaixa2(BigDecimal valorBruto, BigDecimal totalIR) {
		return totalIR = totalIR.add(valorBruto
				.multiply(new BigDecimal(15))
				.divide(new BigDecimal(100))
				.subtract(new BigDecimal(354.80), MathContext.DECIMAL32));
	}
	
	private BigDecimal calculoIRPFFaixa3(BigDecimal valorBruto, BigDecimal totalIR) {
		return totalIR = totalIR.add(valorBruto
				.multiply(new BigDecimal(22.5))
				.divide(new BigDecimal(100))
				.subtract(new BigDecimal(636.13), MathContext.DECIMAL32));
	}

	private BigDecimal calculoIRPFFaixa4(BigDecimal valorBruto, BigDecimal totalIR) {
		return totalIR = totalIR.add(valorBruto
				.multiply(new BigDecimal(27.5))
				.divide(new BigDecimal(100))
				.subtract(new BigDecimal(869.36), MathContext.DECIMAL32));
	}

/* ************************** FINAL ----- RETENÇÃO DE IMPOSTOS ***************/

	private LocalDate cadastroVencimento(ContaPagar contaPagar, Integer parcela) {
		LocalDate novoVencimento;
		if(contaPagar.getVencimento() == null) {
			contaPagar.setVencimento(contaPagar.getDataEmissao());
		}
		switch (contaPagar.getPrazoParcelamento()) {
		case 5:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(5 * parcela);
			break;
		case 10:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(10 * parcela);
			break;
		case 15:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(15 * parcela);
			break;
		case 20:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(20 * parcela);
			break;
		case 30:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(30 * parcela);
			break;
		case 45:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(45 * parcela);
			break;
		case 60:	
			novoVencimento = contaPagar.getDataEmissao().plusDays(60 * parcela);
			break;
		default:
			testeVencimentoMaiorEmissao(contaPagar);
			novoVencimento = contaPagar.getVencimento();
		}
		return novoVencimento;
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
	
	private void testeVencimentoMaiorEmissao(ContaPagar contaPagar) {
		if (contaPagar.getDataEmissao() != null) {
			if (!contaPagar.isVencimentoMaiorEmissao()) {
				throw new VencimentoMenorEmissaoException(
						"A data de vencimento não pode ser menor que a data de emissão");
			}
		}
	}
	
	
	private String cadastroHistoricoDaConta(ContaPagar contaPagar){
		String novoHistorico = new String();
		if(contaPagar.getHistorico().isEmpty() && contaPagar.getPlanoContaSecundaria().getId() == null) {
			throw new IdPlanoContaSecundariaNuloException("Para histórico automático, é necessário escolher um plano de conta secundário!");
		}else {
			PlanoContaSecundaria planoConta = contaSecundariaService.findOne(contaPagar.getPlanoContaSecundaria().getId());

			//Se histórico não informado, é cadastrado com o nome da conta secundária
			if(contaPagar.getHistorico().isEmpty() && contaPagar.getPlanoContaSecundaria().getId() != null) {
				novoHistorico = planoConta.getNome();
		
			}else {
				novoHistorico = contaPagar.getHistorico();
			}
		}	
		return novoHistorico;
	}	

	
	private void testeRegistroJaCadastrado(ContaPagar contaPagar) {
		Optional<ContaPagar> optional = repository.findByDocumentoAndFornecedor(contaPagar.getDocumento(), contaPagar.getFornecedor());
		if (optional.isPresent() && !optional.get().equals(contaPagar)) {
			throw new DocumentoEFornecedorJaCadastradoException(
					"Já existe uma conta com este documento e fornecedor cadastrado!");
		}
	}

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
	
	
	private LocalDate dataUtil(LocalDate dataApuracao, Integer n) {
		LocalDate primeiroDiaMesSeguinte = dataApuracao.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
		LocalDate vencimento = primeiroDiaMesSeguinte.plusDays(n);
		DayOfWeek diaSemana = vencimento.getDayOfWeek();
		//TODO: Tratar quando o dia do vencimento for um feriado
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

	public ContaPagar findByAnexo(String cod) {
		return repository.findByAnexo(cod);
	}

	public ContaPagar findByImpostoGerado(Imposto imposto) {
		return repository.findByImpostoGerado(imposto);
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
