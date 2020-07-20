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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ajeff.simed.config.init.AppInitializer;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.financeiro.service.contaPagar.ContaPagarSalvaEvent;
import com.ajeff.simed.financeiro.service.contaPagar.ContaPagarService;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
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
	private ApplicationEventPublisher publisher;
	
	@Mock
	private ContasPagarRepository repository;
	@Mock
	private FornecedoresRepository fornecedorRepository;
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
		

	}
	
	


	
	
	@Test
	@DisplayName("Deve salvar uma conta a pagar SIMPLES, sem parcela e sem impostos")
	public void salvarContaPagarSimples() {
		ContaPagar conta = contaNova();
		ContaPagar contaSalva = contaNova();
		contaSalva.setId(1l);
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		Assertions.assertThat(contaSalva.getId()).isNotNull();
		Mockito.verify(repository, Mockito.times(1)).save(conta);
	}
	
	


	
	
//	@Test
//	@DisplayName("Deve salvar uma conta a pagar com 2 parcelas e sem impostos")
//	public void salvarContaPagar2ParcelasSemImpostos() {
//		
//		List<ContaPagar> contasASalvar = new ArrayList<>();
//		List<ContaPagar> contasSalvas = new ArrayList<>();
//
//		ContaPagar conta = contaNova();
//		conta.setTotalParcela(2);
//		contasASalvar.add(conta);
//		
//		
//		for(int i = 0; i < conta.getTotalParcela(); i++) {
//			ContaPagar c = contaNova();
//			Long id = (long) (i+ 1);
//			c.setId(id);
//			c.setValor(CalculosComValores.setarValorTotalPositivo(conta.getValor(), BigDecimal.ZERO, BigDecimal.ZERO, conta.getTotalParcela()));
//			contasSalvas.add(c);
//		}
//		
//		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
//				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.empty());
//		
//		Mockito.when(repository.save(contasASalvar)).thenReturn(contasSalvas);
//		
//		service.salvar(conta);
//		
//		Assertions.assertThat(contasSalvas.size()).isNotNull();
//		Assertions.assertThat(contasSalvas.size()).isNotZero();
//		Assertions.assertThat(contasSalvas.size()).isEqualTo(2);
//		
//		for (int i = 0; i < conta.getTotalParcela(); i++) {
//			Assertions.assertThat(contasSalvas.get(i).getDataEmissao()).isBefore(contasSalvas.get(i).getVencimento());
//			Assertions.assertThat(contasSalvas.get(i).getNotaFiscal()).isEqualTo(contaNova().getNotaFiscal());
//			Assertions.assertThat(contasSalvas.get(i).getHistorico()).isEqualTo(contaNova().getHistorico());
//			Assertions.assertThat(contasSalvas.get(i).getFornecedor()).isEqualTo(contaNova().getFornecedor());
//			Assertions.assertThat(contasSalvas.get(i).getPlanoContaSecundaria()).isEqualTo(contaNova().getPlanoContaSecundaria());
//			Assertions.assertThat(contasSalvas.get(i).getReterINSS()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getReterCOFINS()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getReterIR()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getIssPorcentagem()).isZero();			
//			Assertions.assertThat(contasSalvas.get(i).getValor()).isEqualTo(BigDecimal.valueOf(500));
//		}
//		
//		Mockito.verify(repository, Mockito.times(1)).save(contasASalvar);
//	}	
//
//	
//	
//	@Test
//	@DisplayName("Deve salvar uma conta a pagar com 10 parcelas e sem impostos")
//	public void salvarContaPagar10ParcelasSemImpostos() {
//		
//		List<ContaPagar> contasASalvar = new ArrayList<>();
//		List<ContaPagar> contasSalvas = new ArrayList<>();
//
//		ContaPagar conta = contaNova();
//		conta.setTotalParcela(10);
//		contasASalvar.add(conta);
//		
//		
//		for(int i = 0; i < conta.getTotalParcela(); i++) {
//			ContaPagar c = contaNova();
//			Long id = (long) (i + 1);
//			c.setId(id);
//			c.setValor(CalculosComValores.setarValorTotalPositivo(conta.getValor(), BigDecimal.ZERO, BigDecimal.ZERO, conta.getTotalParcela()));
//			contasSalvas.add(c);
//		}
//		
//		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
//				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.empty());
//		
//		Mockito.when(repository.save(contasASalvar)).thenReturn(contasSalvas);
//		
//		service.salvar(conta);
//		
//		Assertions.assertThat(contasSalvas.size()).isNotNull();
//		Assertions.assertThat(contasSalvas.size()).isNotZero();
//		Assertions.assertThat(contasSalvas.size()).isEqualTo(10);
//		
//		for (int i = 0; i < conta.getTotalParcela(); i++) {
//			Assertions.assertThat(contasSalvas.get(i).getDataEmissao()).isBefore(contasSalvas.get(i).getVencimento());
//			Assertions.assertThat(contasSalvas.get(i).getNotaFiscal()).isEqualTo(contaNova().getNotaFiscal());
//			Assertions.assertThat(contasSalvas.get(i).getHistorico()).isEqualTo(contaNova().getHistorico());
//			Assertions.assertThat(contasSalvas.get(i).getFornecedor()).isEqualTo(contaNova().getFornecedor());
//			Assertions.assertThat(contasSalvas.get(i).getPlanoContaSecundaria()).isEqualTo(contaNova().getPlanoContaSecundaria());
//			Assertions.assertThat(contasSalvas.get(i).getReterINSS()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getReterCOFINS()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getReterIR()).isFalse();
//			Assertions.assertThat(contasSalvas.get(i).getIssPorcentagem()).isZero();			
//			Assertions.assertThat(contasSalvas.get(i).getValor()).isEqualTo(BigDecimal.valueOf(100));
//		}
//		
//		Mockito.verify(repository, Mockito.times(1)).save(contasASalvar);
//	}	
//	
//	
//	
//	
//	@Test
//	@DisplayName("Deve retornar erro quando a houver uma conta a pagar salva com mesmos dados")
//	public void erroSalvarContaPagarDuplicada() {
//		
//		ContaPagar conta = contaNova();
//		ContaPagar contaSalva = contaNova();
//		contaSalva.setId(1l);
//		
//		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
//				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.of(contaSalva));
//		
//		Throwable exception = Assertions.catchThrowable(() -> service.testeRegistroJaCadastrado((conta)));
//		
//		Assertions.assertThat(exception)
//			.isInstanceOf(DocumentoEFornecedorJaCadastradoException.class)
//			.hasMessage("Já existe uma conta cadastrada com esta nota fiscal para esse fornecedor!");
//	}
//	
	




	private ContaPagar contaNova() {
		ContaPagar cp = new ContaPagar();
		cp.setDataEmissao(LocalDate.of(2020, 3, 20));
		cp.setVencimento(LocalDate.of(2020, 3, 25));
		cp.setValor(new BigDecimal(1000));
		cp.setNotaFiscal("0001");
		cp.setTotalParcela(1);
		cp.setPlanoContaSecundaria(pcs);
		cp.setFornecedor(novoFornecedor());
		cp.setEmpresa(e);
		cp.setHistorico("TESTANDO A CONTA A PAGAR");
		cp.setReterINSS(false);
		cp.setReterCOFINS(false);
		cp.setReterIR(false);
		cp.setIssPorcentagem(BigDecimal.ZERO);
		cp.setDocumento("123");
		return cp;
	}
	
	private Fornecedor novoFornecedor() {
		Fornecedor fornecedor = new Fornecedor();
		fornecedor.setNome("FORNECEDOR TESTE LTDA");
		fornecedor.setFantasia("FORNECEDOR TESTE");
		fornecedor.setSigla("FORTESTE");
		fornecedor.setClifor(true);
		fornecedor.setEndereco(end);
		Mockito.when(fornecedorRepository.save(Mockito.any(Fornecedor.class))).thenReturn(fornecedor);
		return fornecedor;
	}	
	
}