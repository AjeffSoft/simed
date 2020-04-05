package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.assertj.core.api.Assertions;
//import org.assertj.core.api.Assertions;
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
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ContaPagarServiceTest {
	
	
	
	@InjectMocks
	private ContaPagarService service;
	
	@Mock
	private ContasPagarRepository repository;
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
		
		es = Mockito.mock(Estado.class);
		es.setNome("ESTADO TESTE");
		es.setUf("TE");
		
		c = Mockito.mock(Cidade.class);
		c.setNome("CIDADE TESTE");
		c.setUf("TE");
		c.setCapital(true);
		c.setEstado(es);
		
		end = Mockito.mock(Endereco.class);
		end.setBairro("BAIRRO TESTE");
		end.setCep("63500000");
		end.setCidade(c);
		
		pc = Mockito.mock(PlanoConta.class);
		pc.setNome("PLANO DE CONTA TESTE");
		pc.setSituacao(true);
		pc.setTipo("DÉBITO");
		
		pcs = Mockito.mock(PlanoContaSecundaria.class);
		pcs.setNome("PLANO DE CONTA SECUNDÁRIA TESTE");
		pcs.setSituacao(true);
		pcs.setTipo("VARIÁVEL");
		pcs.setPlanoConta(pc);
		
		e = Mockito.mock(Empresa.class);
		e.setNome("EMPRESA TESTE LTDA");
		e.setFantasia("EMPRESA TESTE");
		e.setSigla("EMPTESTE");
		e.setEndereco(end);
		
		f = Mockito.mock(Fornecedor.class);
		f.setNome("FORNECEDOR TESTE LTDA");
		f.setFantasia("FORNECEDOR TESTE");
		f.setSigla("FORTESTE");
		f.setClifor(true);
		f.setEndereco(end);
		
	}
	

	
	
	@Test
	@DisplayName("Deve salvar um conta a pagar sem parcela e sem impostos")
	public void salvarContaPagarSimples() {
		
		ContaPagar cp = contaNova();
		ContaPagar cpSalva = contaNova();
		cpSalva.setId(1l);
		Mockito.when(repository.save(cp)).thenReturn(cpSalva);
		
		service.salvar(cp);
		
		Assertions.assertThat(cpSalva.getId()).isNotNull();
//		Mockito.verify(service, Mockito.times(1)).salvar(cp);
	}




	private ContaPagar contaNova() {
		ContaPagar cp = new ContaPagar();
		cp.setDataEmissao(LocalDate.of(2020, 3, 20));
		cp.setVencimento(LocalDate.of(2020, 3, 25));
		cp.setValor(new BigDecimal(1000));
		cp.setNotaFiscal("0001");
		cp.setTotalParcela(1);
		cp.setPlanoContaSecundaria(pcs);
		cp.setFornecedor(f);
		return cp;
	}

	
}
