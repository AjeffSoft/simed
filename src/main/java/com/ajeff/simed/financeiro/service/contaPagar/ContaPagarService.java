package com.ajeff.simed.financeiro.service.contaPagar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.util.MultiValueMap;

import com.ajeff.simed.financeiro.dto.ParcelamentoDTO;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.imposto.CalculoImpostoIRRF;
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
	public void salvar(ContaPagar contaPagar, MultiValueMap<String, String> requests) {
		testeRegistroJaCadastrado(contaPagar);
		DatasUtils.emissaoMenorOuIgualVencimento(contaPagar.getDataEmissao(), contaPagar.getVencimento());
		List<ContaPagar> contas = new ArrayList<>();
		
		if(contaPagar.isNovo()) {
			
			if(contaPagar.isTemImpostoRetido()) {
				contaPagar.setImpostos(impostosDaConta(contaPagar));
			}
			contaPagar.setValor(contaPagar.getValor().subtract(calculoValorConta(contaPagar)));

			contas = parcelamento(contaPagar, requests);
		}
		
		repository.save(contas);
		publisher.publishEvent(new ContaPagarSalvaEvent(contas.get(0)));
	}
	
	
	private List<ContaPagar> parcelamento(ContaPagar contaPagar, MultiValueMap<String, String> requests) {
		List<ContaPagar> contas = new ArrayList<>();
		List<ParcelamentoDTO> parcelas = montarParcelamentoDTO(requests);
		
		if(parcelas.size() > 0) {
			parcelas.stream().forEach( p -> {
				ContaPagar cp = new ContaPagar();
				Integer totalParcela = parcelas.size();
				montarContaPagar(contaPagar, cp, p, totalParcela);
				contas.add(cp);
			});
		}
		
		parcelas.stream().forEach(System.out::println);
		return contas;
	}


	private void montarContaPagar(ContaPagar contaPagar, ContaPagar cp, ParcelamentoDTO p, Integer totalParcela) {
		cp.setContentType(contaPagar.getContentType());
		cp.setDataEmissao(contaPagar.getDataEmissao());
		cp.setDocumento(contaPagar.getDocumento());
		cp.setEmpresa(contaPagar.getEmpresa());
		cp.setFixo(contaPagar.getFixo());
		cp.setFornecedor(contaPagar.getFornecedor());
		cp.setHistorico(contaPagar.getHistorico());
		cp.setNotaFiscal(contaPagar.getNotaFiscal());
		cp.setParcela(p.getParcela());
		cp.setTotalParcela(totalParcela);
		cp.setPlanoContaSecundaria(contaPagar.getPlanoContaSecundaria());
		cp.setRecibo(contaPagar.getRecibo());
		cp.setStatus(StatusContaPagar.ABERTO);
		cp.setTemNota(contaPagar.getTemNota());
		cp.setUpload(contaPagar.getUpload());

//		contaPagar.getImpostos().stream().filter( i -> i.getValor().compareTo(BigDecimal.ZERO) == 1).forEach( i -> cp.setImpostos(i));
//		cp.setImpostos(impostos);
//		cp.setValor(valor);
//		cp.setVencimento(vencimento);		
	}


	private List<ParcelamentoDTO> montarParcelamentoDTO(MultiValueMap<String, String> requests) {
		List<ParcelamentoDTO> parcelas = new ArrayList<>();
		
		
		List<String> vencs = requests.get("vencimento-parcela");
		List<String> valores = requests.get("valor-parcela");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


		for (int i = 0; i < vencs.size(); i++) {
			if(!vencs.get(i).equals("") && !valores.get(i).equals("")) {
				ParcelamentoDTO p = new ParcelamentoDTO();
				String nn = "";
				
				for(String s : valores.get(i).split("")) {
					
					
					if(s.equals(".")) {
						s = "";
					}else if(s.equals(",")) {
						s = ".";
					}
					nn += s;
				}
				
				p.setParcela(i+1);
				p.setValor(new BigDecimal(nn));
				p.setVencimento(LocalDate.parse(vencs.get(i), formatter));
				parcelas.add(p);
			}
		}
		
		return parcelas;
	}


	private BigDecimal calculoValorConta(ContaPagar contaPagar) {
		if(!contaPagar.getImpostos().isEmpty()) {
			return contaPagar.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
		}else {
			return BigDecimal.ZERO;
		}
	}


	private List<Imposto> impostosDaConta(ContaPagar contaPagar) {
		List<Imposto> impostos = new ArrayList<>();
		BigDecimal inss = BigDecimal.ZERO;
		
		if (contaPagar.isIssRetido()) {
			Imposto iss = impostoService.novoImposto(contaPagar, "ISS");
			iss.setValor(impostoService.valorISSRetido(contaPagar.getValor(), contaPagar.getIssPorcentagem()));
			iss.setTotal(iss.getValor());
			impostos.add(iss);
		}
		
		
		//SOMENTE PARA FORNECEDORES PESSOA FISICA
		if(contaPagar.getReterINSS() && contaPagar.getFornecedor().getTipo().equals("F")) {
			Imposto imp = impostoService.novoImposto(contaPagar, "INSS");
			imp.setValor(impostoService.valorINSSRetido(contaPagar.getValor()));
			imp.setTotal(imp.getValor());
			inss = imp.getValor();
			impostos.add(imp);
		}
		

		//SOMENTE PARA FORNECEDORES PESSOA JURIDICA
		if(contaPagar.getReterCOFINS() && contaPagar.getFornecedor().getTipo().equals("J")) {
			Imposto pccs = impostoService.novoImposto(contaPagar, "PCCS");
			pccs.setValor(impostoService.valorPCCSRetido(contaPagar.getValor()));
			pccs.setTotal(pccs.getValor());
			impostos.add(pccs);
		}
 
		
		if(contaPagar.getReterIR()) {
			Imposto ir = impostoService.novoImposto(contaPagar, "IRRF");
			BigDecimal dependente = CalculoImpostoIRRF.descontoDependente(contaPagar.getFornecedor().getDependente(), contaPagar.getFornecedor().isDependente()); 
			ir.setValor(impostoService.valorIRRFRetido(contaPagar.getValor(), inss, dependente, contaPagar.getFornecedor().getTipo()));
			ir.setTotal(ir.getValor());
			impostos.add(ir);
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

