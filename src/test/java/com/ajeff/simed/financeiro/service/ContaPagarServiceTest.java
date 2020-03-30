package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ajeff.simed.config.init.AppInitializer;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.service.exception.DataForaMovimentacaoAbertaException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ContaPagarServiceTest {
	
	
	@Mock
	private ContaPagarService service;
	@Mock
	private PlanoConta pc;
	@Mock
	private PlanoContaSecundaria pcs;
	@Mock
	private Empresa e;
	@Mock
	private Cidade c;
	@Mock
	private Estado es;
	@Mock
	private Fornecedor f;
	@Mock
	private Endereco end;	
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		es = new Estado();
		es.setNome("ESTADO TESTE");
		es.setUf("TE");
		
		c = new Cidade();
		c.setNome("CIDADE TESTE");
		c.setUf("TE");
		c.setCapital(true);
		c.setEstado(es);
		
		end = new Endereco();
		end.setBairro("BAIRRO TESTE");
		end.setCep("63500000");
		end.setCidade(c);
		
		pc = new PlanoConta();
		pc.setNome("PLANO DE CONTA TESTE");
		pc.setSituacao(true);
		pc.setTipo("DÉBITO");
		
		pcs = new PlanoContaSecundaria();
		pcs.setNome("PLANO DE CONTA SECUNDÁRIA TESTE");
		pcs.setSituacao(true);
		pcs.setTipo("VARIÁVEL");
		pcs.setPlanoConta(pc);
		
		e = new Empresa();
		e.setNome("EMPRESA TESTE LTDA");
		e.setFantasia("EMPRESA TESTE");
		e.setSigla("EMPTESTE");
		e.setEndereco(end);
		
		f = new Fornecedor();
		f.setNome("FORNECEDOR TESTE LTDA");
		f.setFantasia("FORNECEDOR TESTE");
		f.setSigla("FORTESTE");
		f.setClifor(true);
		f.setEndereco(end);
		
	}
	
	
	@Test
	@DisplayName("Deve salvar um conta a pagar sem parcela e sem impostos")
	public void contaPagar1() {
		
		ContaPagar cp = Mockito.mock(ContaPagar.class);
		LocalDate emissao = LocalDate.of(2020, 3, 20);
		LocalDate vencimento = LocalDate.of(2020, 3, 25);
		cp.setDataEmissao(emissao);
		cp.setVencimento(vencimento);
		cp.setValor(new BigDecimal(1000));
		cp.setNotaFiscal("0001");
		cp.setPlanoContaSecundaria(pcs);
		cp.setFornecedor(f);
		service.salvar(cp);
		
		Mockito.doNothing().when(service).salvar(cp);
		Mockito.verify(service, Mockito.times(1)).salvar(cp);
	}
	
	
	@Test
	@DisplayName("Deve dar erro da data de vencimento menor que a data emissão")
	public void contaPagar2() {
		
		ContaPagar cp = Mockito.mock(ContaPagar.class);
		LocalDate emissao = LocalDate.of(2020, 3, 20);
		LocalDate vencimento = LocalDate.of(2020, 3, 10);
		cp.setDataEmissao(emissao);
		cp.setVencimento(vencimento);
		cp.setValor(new BigDecimal(1000));
		cp.setNotaFiscal("0001");
		cp.setPlanoContaSecundaria(pcs);
		cp.setFornecedor(f);
		
//		try {
//			service.salvar(cp);
//		} catch (VencimentoMenorEmissaoException e) {
//			Assertions.assertEquals("", e.getMessage());
//		}
//		
//		Mockito.doThrow(VencimentoMenorEmissaoException.class).when(service).salvar(cp);
//		Mockito.verify(service, Mockito.times(1)).salvar(cp);
	}	
	
}
