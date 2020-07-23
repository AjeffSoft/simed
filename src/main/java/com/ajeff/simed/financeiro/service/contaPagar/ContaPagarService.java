package com.ajeff.simed.financeiro.service.contaPagar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.imposto.CalculoImpostoINSS;
import com.ajeff.simed.financeiro.service.imposto.CalculoImpostoISS;
import com.ajeff.simed.financeiro.service.imposto.ImpostoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.DatasUtils;

@Service
public class ContaPagarService {

	@Autowired
	private ContasPagarRepository repository;
	@Autowired
	private ImpostoService impostoService;
//	@Autowired
//	private PlanoContaSecundariaService contaSecundariaService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@Transactional
	public void salvar(ContaPagar contaPagar) {
		testeRegistroJaCadastrado(contaPagar);
		DatasUtils.emissaoMenorOuIgualVencimento(contaPagar.getDataEmissao(), contaPagar.getVencimento());
		
		if(contaPagar.isNovo()) {
			contaPagar.setStatus(StatusContaPagar.ABERTO);
			contaPagar.setImpostos(impostosDaConta(contaPagar));
			contaPagar.setValor(contaPagar.getValor().subtract(calculoValorConta(contaPagar)));
		}
		
		repository.save(contaPagar);
		publisher.publishEvent(new ContaPagarSalvaEvent(contaPagar));
	}
	
	
	private BigDecimal calculoValorConta(ContaPagar contaPagar) {
		if(!contaPagar.getImpostos().isEmpty()) {
			return contaPagar.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
		}else {
			return contaPagar.getValor();
		}
	}


	private List<Imposto> impostosDaConta(ContaPagar contaPagar) {
		List<Imposto> impostos = new ArrayList<>();
		if (contaPagar.getIssPorcentagem() != null || contaPagar.getIssPorcentagem().compareTo(BigDecimal.ZERO) == 1) {
			Imposto iss = impostoService.novoImposto(contaPagar, "ISS");
			iss.setValor(CalculoImpostoISS.calculo(contaPagar.getValor(), contaPagar.getIssPorcentagem()));
			iss.setTotal(iss.getValor());
			impostos.add(iss);
		}
		
		if(contaPagar.getReterINSS()) {
			Imposto inss = impostoService.novoImposto(contaPagar, "INSS");
			inss.setValor(CalculoImpostoINSS.calculo(contaPagar.getValor()));
			inss.setTotal(inss.getValor());
			impostos.add(inss);
		}
		return impostos;
	}


	private void testeRegistroJaCadastrado(ContaPagar contaPagar) {
		Optional<ContaPagar> optional = repository.findByNotaFiscalAndFornecedor(contaPagar.getNotaFiscal(), contaPagar.getFornecedor());
		if (optional.isPresent() && !optional.get().equals(contaPagar)) {
			throw new DocumentoEFornecedorJaCadastradoException("Já existe uma conta cadastrada com esta nota fiscal para esse fornecedor!");
		}
	}	
	
//
//	private void regrasAlteracao(List<ContaPagar> contas) {
//		contas.forEach(c -> CalculosComDatas.emissaoMenorIgualVencimento(c.getDataEmissao(), c.getVencimento()));
//		contas.forEach(c -> testeRegistroJaCadastrado(c));
//	}
//
//
//	private List<ContaPagar> gerarContasPagar(ContaPagar contaPagar) {
//		List<ContaPagar> contas = new ArrayList<>();
//		List<Imposto> impostos = new ArrayList<>();
//		
//		testeSeTotalParcelaNulo(contaPagar);
//		Long days = CalculosComDatas.diferencaEntreDuasDatas(contaPagar.getDataEmissao(), contaPagar.getVencimento());
//		impostos = verificarRetencaoImpostos(contaPagar, impostos);
//		for(int i = 1; i <= contaPagar.getTotalParcela(); i++) {
//			contas.add(novaContaPagar(contaPagar, days, i, impostos));
//		}
//		
//		return contas;
//	}
//
//
//	public List<Imposto> verificarRetencaoImpostos(ContaPagar contaPagar, List<Imposto> impostos) {
////		if (contaPagar.isTemImpostoRetido()) {
////			impostos = impostoService.calcularImpostos(contaPagar);
////		}
//		return null;
//	}
//
//
//	private ContaPagar novaContaPagar(ContaPagar contaPagar, Long days, int i, List<Imposto> impostos) {
//		ContaPagar cp = new ContaPagar();
//		cp.setDataEmissao(contaPagar.getDataEmissao());
//		cp.setVencimento(contaPagar.getDataEmissao().plusDays(days * i));
//		cp.setEmpresa(contaPagar.getEmpresa());
//		cp.setStatus("ABERTO");
//		cp.setDocumento(contaPagar.getNotaFiscal() +"-"+i);
//		cp.setParcela(i);
//		cp.setRecibo(contaPagar.getRecibo());
//		cp.setTemNota(contaPagar.getTemNota());
//		cp.setNotaFiscal(contaPagar.getNotaFiscal());
//		cp.setTotalParcela(contaPagar.getTotalParcela());
//		cp.setPlanoContaSecundaria(contaPagar.getPlanoContaSecundaria());
//		cp.setFornecedor(contaPagar.getFornecedor());
//		cp.setUpload(contaPagar.getUpload());
//		cp.setContentType(contaPagar.getContentType());
//		setarHistoricoDaContaPagar(contaPagar, cp);
//		CalculosComDatas.emissaoMenorIgualVencimento(cp.getDataEmissao(), cp.getVencimento());
//		testeRegistroJaCadastrado(cp);
//		calcularValorTotal(contaPagar, cp, impostos);
//		return cp;
//	}
//
//
//	private void calcularValorTotal(ContaPagar contaPagar, ContaPagar cp, List<Imposto> impostos) {
//		BigDecimal valorImpostos = BigDecimal.ZERO;
//		
////		if(!impostos.isEmpty()) {
////			valorImpostos = impostos.stream().map( i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
////			cp.setImpostos(impostos);
////		}
//		cp.setValor(CalculosComValores.setarValorTotalPositivo(contaPagar.getValor(), valorImpostos, BigDecimal.ZERO, contaPagar.getTotalParcela()));
//	}
//
//	
//
//	private void testeSeTotalParcelaNulo(ContaPagar contaPagar) {
//		if (contaPagar.getTotalParcela() == null || contaPagar.getTotalParcela() <=0) {
//			contaPagar.setTotalParcela(1);
//		}
//	}
//
//
//
//	private void setarHistoricoDaContaPagar(ContaPagar contaPagar, ContaPagar cp) {
//		if(contaPagar.getHistorico().isEmpty() && contaPagar.getPlanoContaSecundaria().getId() != null) {
//			historicoVazioIgualContaSecundaria(cp);
//		}else {
//			cp.setHistorico(contaPagar.getHistorico());
//		}
//	}	
//
//	private void historicoVazioIgualContaSecundaria(ContaPagar contaPagar) {
//		PlanoContaSecundaria planoConta = contaSecundariaService.findOne(contaPagar.getPlanoContaSecundaria().getId());
//		contaPagar.setHistorico(planoConta.getNome());
//	}	
//

	
	
	
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
	
	public ContaPagar buscarComPlanoConta(Long id) {
		return repository.buscarComPlanoConta(id);
	}
	
	public BigDecimal total(ContaPagarFilter contaPagarFilter) {
		return repository.total(contaPagarFilter);
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
	
	public List<ContaPagar> findByContaPagarFornecedor(Fornecedor fornecedor) {
		return repository.findByContaPagarFornecedor(fornecedor);
	}
	

	@Transactional
	public void autorizarPagamento(ContaPagar contaPagar) {
		contaPagar.setStatus(StatusContaPagar.AUTORIZADO);
		repository.save(contaPagar);
	}
	
	@Transactional	
	public void cancelarAutorizarPagamento(ContaPagar contaPagar) {
		contaPagar.setStatus(StatusContaPagar.ABERTO);
		repository.save(contaPagar);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	
	@Autowired
	private ContasPagarRepository repository;
	@Autowired
	private PlanoContaSecundariaService contaSecundariaService;
//	@Autowired
//	private ImpostoService impostoService;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	
	@Transactional
	public void salvar(ContaPagar contaPagar) {
		List<ContaPagar> contas = new ArrayList<ContaPagar>();

		if (contaPagar.isNovo()) {
			contas = gerarContasPagar(contaPagar);
			repository.save(contas);
			publisher.publishEvent(new ContaPagarSalvaEvent(contas));
		}else {
			contas.add(contaPagar);
			regrasAlteracao(contas);
			repository.save(contas);
		}

	}
	

	private void regrasAlteracao(List<ContaPagar> contas) {
		contas.forEach(c -> CalculosComDatas.emissaoMenorIgualVencimento(c.getDataEmissao(), c.getVencimento()));
		contas.forEach(c -> testeRegistroJaCadastrado(c));
	}


	private List<ContaPagar> gerarContasPagar(ContaPagar contaPagar) {
		List<ContaPagar> contas = new ArrayList<>();
		List<Imposto> impostos = new ArrayList<>();
		
		testeSeTotalParcelaNulo(contaPagar);
		Long days = CalculosComDatas.diferencaEntreDuasDatas(contaPagar.getDataEmissao(), contaPagar.getVencimento());
		impostos = verificarRetencaoImpostos(contaPagar, impostos);
		for(int i = 1; i <= contaPagar.getTotalParcela(); i++) {
			contas.add(novaContaPagar(contaPagar, days, i, impostos));
		}
		
		return contas;
	}


	public List<Imposto> verificarRetencaoImpostos(ContaPagar contaPagar, List<Imposto> impostos) {
//		if (contaPagar.isTemImpostoRetido()) {
//			impostos = impostoService.calcularImpostos(contaPagar);
//		}
		return null;
	}


	private ContaPagar novaContaPagar(ContaPagar contaPagar, Long days, int i, List<Imposto> impostos) {
		ContaPagar cp = new ContaPagar();
		cp.setDataEmissao(contaPagar.getDataEmissao());
		cp.setVencimento(contaPagar.getDataEmissao().plusDays(days * i));
		cp.setEmpresa(contaPagar.getEmpresa());
		cp.setStatus("ABERTO");
		cp.setDocumento(contaPagar.getNotaFiscal() +"-"+i);
		cp.setParcela(i);
		cp.setRecibo(contaPagar.getRecibo());
		cp.setTemNota(contaPagar.getTemNota());
		cp.setNotaFiscal(contaPagar.getNotaFiscal());
		cp.setTotalParcela(contaPagar.getTotalParcela());
		cp.setPlanoContaSecundaria(contaPagar.getPlanoContaSecundaria());
		cp.setFornecedor(contaPagar.getFornecedor());
		cp.setUpload(contaPagar.getUpload());
		cp.setContentType(contaPagar.getContentType());
		setarHistoricoDaContaPagar(contaPagar, cp);
		CalculosComDatas.emissaoMenorIgualVencimento(cp.getDataEmissao(), cp.getVencimento());
		testeRegistroJaCadastrado(cp);
		calcularValorTotal(contaPagar, cp, impostos);
		return cp;
	}


	private void calcularValorTotal(ContaPagar contaPagar, ContaPagar cp, List<Imposto> impostos) {
		BigDecimal valorImpostos = BigDecimal.ZERO;
		
//		if(!impostos.isEmpty()) {
//			valorImpostos = impostos.stream().map( i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
//			cp.setImpostos(impostos);
//		}
		cp.setValor(CalculosComValores.setarValorTotalPositivo(contaPagar.getValor(), valorImpostos, BigDecimal.ZERO, contaPagar.getTotalParcela()));
	}

	

	private void testeSeTotalParcelaNulo(ContaPagar contaPagar) {
		if (contaPagar.getTotalParcela() == null || contaPagar.getTotalParcela() <=0) {
			contaPagar.setTotalParcela(1);
		}
	}



	private void setarHistoricoDaContaPagar(ContaPagar contaPagar, ContaPagar cp) {
		if(contaPagar.getHistorico().isEmpty() && contaPagar.getPlanoContaSecundaria().getId() != null) {
			historicoVazioIgualContaSecundaria(cp);
		}else {
			cp.setHistorico(contaPagar.getHistorico());
		}
	}	

	private void historicoVazioIgualContaSecundaria(ContaPagar contaPagar) {
		PlanoContaSecundaria planoConta = contaSecundariaService.findOne(contaPagar.getPlanoContaSecundaria().getId());
		contaPagar.setHistorico(planoConta.getNome());
	}	


	public void testeRegistroJaCadastrado(ContaPagar contaPagar) {
		if(contaPagar.getFornecedor().getId() == null) {
			throw new TransientObjectException ("O fornecedor não foi selecionado");
		}else {
			
			Optional<ContaPagar> optional = repository.findByDocumentoAndFornecedorAndEmpresa(contaPagar.getDocumento(), contaPagar.getFornecedor(), contaPagar.getEmpresa());
			if (optional.isPresent() && !optional.get().equals(contaPagar)) {
				throw new DocumentoEFornecedorJaCadastradoException("Já existe uma conta cadastrada com esta nota fiscal para esse fornecedor!");
			}
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
	
	public ContaPagar buscarComPlanoConta(Long id) {
		return repository.buscarComPlanoConta(id);
	}
	
	public BigDecimal total(ContaPagarFilter contaPagarFilter) {
		return repository.total(contaPagarFilter);
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
	
	public List<ContaPagar> findByContaPagarFornecedor(Fornecedor fornecedor) {
		return repository.findByContaPagarFornecedor(fornecedor);
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
	
	
	
*/	
	
}

