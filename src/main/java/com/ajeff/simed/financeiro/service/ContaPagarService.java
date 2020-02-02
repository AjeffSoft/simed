package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.hibernate.TransientObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class ContaPagarService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ContaPagarService.class);

	
	@Autowired
	private ContasPagarRepository repository;
	@Autowired
	private PlanoContaSecundariaService contaSecundariaService;
	@Autowired
	private ImpostoService impostoService;
	
	
	@Transactional
	public void salvar(ContaPagar contaPagar) {
		try {
			if (contaPagar.isNovo()) {
				List<ContaPagar> contas = gerarContasPagar(contaPagar);
				repository.save(contas);
			}else {
				testeVencimentoMaiorEmissao(contaPagar);
				testeRegistroJaCadastrado(contaPagar);
				repository.save(contaPagar);
			}
		} catch (RegistroNaoCadastradoException e) {
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
	}
	

	private List<ContaPagar> gerarContasPagar(ContaPagar contaPagar) {
		List<ContaPagar> contas = new ArrayList<>();
		
		if (contaPagar.getTotalParcela() == null || contaPagar.getTotalParcela() <=0) {
			contaPagar.setTotalParcela(1);
		}
		
		Long days = calculoDiasDataEmissaoParaVencimento(contaPagar);

		BigDecimal impostos = impostoService.retencaoImpostos(contaPagar);
		contaPagar.setValor(contaPagar.getValor().subtract(impostos));
		
		try {
			for(int i = 1; i <= contaPagar.getTotalParcela(); i++) {
		
				ContaPagar cp = new ContaPagar();
				cp.setDataEmissao(contaPagar.getDataEmissao());
				cp.setVencimento(contaPagar.getDataEmissao().plusDays(days * i));
				cp.setEmpresa(contaPagar.getEmpresa());
				cp.setStatus("ABERTO");
				cp.setDocumento(contaPagar.getFornecedor().getSigla() + contaPagar.getNotaFiscal() +"-"+i);
				cp.setParcela(i);
				cp.setRecibo(contaPagar.getRecibo());
				cp.setTemNota(contaPagar.getTemNota());
				cp.setNotaFiscal(contaPagar.getNotaFiscal());
				cp.setTotalParcela(contaPagar.getTotalParcela());
				cp.setPlanoContaSecundaria(contaPagar.getPlanoContaSecundaria());
				cp.setFornecedor(contaPagar.getFornecedor());
				cp.setValor(contaPagar.getValor().divide(new BigDecimal(contaPagar.getTotalParcela()),
						MathContext.DECIMAL32));		
				setarHistoricoDaContaPagar(contaPagar, cp);
				testeVencimentoMaiorEmissao(cp);
				testeRegistroJaCadastrado(cp);
				contas.add(cp);
			}
		} catch (RegistroNaoCadastradoException e) {
			throw new RegistroNaoCadastradoException("Algo deu errado! Conta não cadastrada.");
		}
		
		return contas;
	}
	
	
	private Long calculoDiasDataEmissaoParaVencimento(ContaPagar contaPagar) {
		return ChronoUnit.DAYS.between(contaPagar.getDataEmissao(), contaPagar.getVencimento());
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


	private void testeVencimentoMaiorEmissao(ContaPagar contaPagar) {
		if(contaPagar.getDataEmissao() != null) {
			if(!contaPagar.isVencimentoMaiorEmissao()) {
				throw new VencimentoMenorEmissaoException("A data de vencimento não pode ser menor que a emissão");
			}
		}
	}
		

	
	private void testeRegistroJaCadastrado(ContaPagar contaPagar) {
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
	

//	@Transactional
//	public void autorizarPagamento(ContaPagar contaPagar) {
//		contaPagar.setStatus("AUTORIZADO");
//		repository.save(contaPagar);
//	}
//	
//	@Transactional	
//	public void cancelarAutorizarPagamento(ContaPagar contaPagar) {
//		contaPagar.setStatus("ABERTO");
//		repository.save(contaPagar);
//	}
}

