package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.financeiro.service.contaPagarService.ContaPagarService;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ImpostoServiceTest {
	
	
	
	@InjectMocks
	private ImpostoService service;
	
	@Mock
	private FornecedoresRepository fornecedorRepository;
	@Mock
	private ContaPagarService contaPagarService;
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
	private Fornecedor forn;
	@Mock
	private Endereco end;	
	@Mock
	private ContaPagar cgp;	


	
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
		
		forn = Mockito.mock(Fornecedor.class);
		forn.setNome("FORNECEDOR TESTE LTDA");
		forn.setFantasia("FORNECEDOR TESTE");
		forn.setSigla("FORTESTE");
		forn.setTipo("J");
		forn.setClifor(true);
		forn.setEndereco(end);
		

		
	}
	

	
	
	@Test
	@DisplayName("Deve retornar zerado para retencão de INSS de PJ")
	public void retornarZeradoImpostoINSS_PJ() {
		Fornecedor forn = fornecedor();
		Fornecedor ff = fornecedor();
		ff.setId(1l);
		ff.setTipo("J");
		ContaPagar cp = contaPagar(ff);
		cp.setReterINSS(true);
		Mockito.when(fornecedorRepository.save(forn)).thenReturn(ff);

		List<Imposto> impostos = service.calcularImpostos(cp);
		
		Assertions.assertThat(impostos.size()).isNotNull();
		Assertions.assertThat(impostos.size()).isZero();
	}
	
	
	@Test
	@DisplayName("Deve retornar o valor do INSS para PF sem DEPENDENTE e abaixo do TETO")
	public void retornarImpostoINSS_PF_SemDependenteAbaixoTeto() {
		Fornecedor forn = fornecedor();
		Fornecedor ff = fornecedor();
		ff.setId(1l);
		ff.setTipo("F");
		ff.setDependente(0);
		ContaPagar cp = contaPagar(ff);
		cp.setValor(BigDecimal.valueOf(2000));
		cp.setReterINSS(true);
		Mockito.when(fornecedorRepository.save(forn)).thenReturn(ff);

		List<Imposto> impostos = service.calcularImpostos(cp);
		
		Assertions.assertThat(impostos.size()).isNotNull();
		Assertions.assertThat(impostos.size()).isNotZero();
		Assertions.assertThat(impostos.size()).isEqualTo(1);
		Assertions.assertThat(impostos.get(0).getApuracao()).isEqualTo(LocalDate.of(2020, 3, 31));
		Assertions.assertThat(impostos.get(0).getHistorico()).isEqualTo(impostos.get(0).getNome() + " - " + cp.getFornecedor().getFantasia() + " - " + cp.getNotaFiscal());
		Assertions.assertThat(impostos.get(0).getStatus()).isEqualTo("ABERTO");
		Assertions.assertThat(impostos.get(0).getVencimento()).isEqualTo(LocalDate.of(2020, 4, 15));
		Assertions.assertThat(impostos.get(0).getJuros()).isZero();		
		Assertions.assertThat(impostos.get(0).getMulta()).isZero();		
		Assertions.assertThat(impostos.get(0).getValor()).isEqualTo(new BigDecimal("220.00"));		
	}	




	private ContaPagar contaPagar(Fornecedor ff) {
		ContaPagar cp = new ContaPagar();
		cp.setDataEmissao(LocalDate.of(2020, 3, 20));
		cp.setVencimento(LocalDate.of(2020, 3, 25));
		cp.setValor(new BigDecimal(10000));
		cp.setNotaFiscal("0001");
		cp.setTotalParcela(1);
		cp.setPlanoContaSecundaria(pcs);
		cp.setFornecedor(ff);
		cp.setEmpresa(e);
		cp.setHistorico("TESTANDO A CONTA A PAGAR");
		cp.setReterINSS(false);
		cp.setReterCOFINS(false);
		cp.setReterIR(false);
		cp.setIssPorcentagem(BigDecimal.ZERO);
		cp.setDocumento("123");
		return cp;
	}


	private Fornecedor fornecedor() {
		Fornecedor forn = new Fornecedor();
		forn.setNome("FORNECEDOR TESTE LTDA");
		forn.setFantasia("FORNECEDOR TESTE");
		forn.setSigla("FORTESTE");
		forn.setClifor(true);
		forn.setEndereco(end);
		return forn;
	}

	
}
