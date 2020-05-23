package com.ajeff.simed.financeiro.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.financeiro.service.contaPagar.ContaPagarService;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;
import com.ajeff.simed.util.CalculosComValores;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ContaPagarServiceTest {
	
	
	
	@InjectMocks
	private ContaPagarService service;
	
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
	@DisplayName("Deve salvar uma conta a pagar sem parcela e sem impostos")
	public void salvarContaPagarSimples() {
		
		List<ContaPagar> contas = new ArrayList<>();
		List<ContaPagar> contasSalvar = new ArrayList<>();

		ContaPagar conta = contaNova();
		ContaPagar contaSalva = contaNova();
		contaSalva.setId(1l);
		contasSalvar.add(contaSalva);
		contas.add(conta);
		
		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(contas)).thenReturn(contasSalvar);
		
		List<ContaPagar> contasSalvas = service.salvar(conta);
		
		Assertions.assertThat(contasSalvas.get(0).getId()).isNotNull();
		Assertions.assertThat(contasSalvas.get(0).getId()).isEqualTo(1l);
		Assertions.assertThat(contasSalvas.size()).isNotNull();
		Assertions.assertThat(contasSalvas.size()).isNotZero();
		Assertions.assertThat(contasSalvas.size()).isEqualTo(1);
		Assertions.assertThat(contasSalvas.get(0).getDataEmissao()).isBefore(contasSalvas.get(0).getVencimento());
		Assertions.assertThat(contasSalvas.get(0).getNotaFiscal()).isEqualTo(contaNova().getNotaFiscal());
		Assertions.assertThat(contasSalvas.get(0).getHistorico()).isEqualTo(contaNova().getHistorico());
		Assertions.assertThat(contasSalvas.get(0).getValor()).isEqualTo(contaNova().getValor());
		Assertions.assertThat(contasSalvas.get(0).getFornecedor()).isEqualTo(contaNova().getFornecedor());
		Assertions.assertThat(contasSalvas.get(0).getPlanoContaSecundaria()).isEqualTo(contaNova().getPlanoContaSecundaria());
		Assertions.assertThat(contasSalvas.get(0).getReterINSS()).isFalse();
		Assertions.assertThat(contasSalvas.get(0).getReterCOFINS()).isFalse();
		Assertions.assertThat(contasSalvas.get(0).getReterIR()).isFalse();
		Assertions.assertThat(contasSalvas.get(0).getIssPorcentagem()).isZero();
		Mockito.verify(repository, Mockito.times(1)).save(contas);
	}

	
	
	@Test
	@DisplayName("Deve salvar uma conta a pagar com 2 parcelas e sem impostos")
	public void salvarContaPagar2ParcelasSemImpostos() {
		
		List<ContaPagar> contasASalvar = new ArrayList<>();
		List<ContaPagar> contasSalvas = new ArrayList<>();

		ContaPagar conta = contaNova();
		conta.setTotalParcela(2);
		contasASalvar.add(conta);
		
		
		for(int i = 0; i < conta.getTotalParcela(); i++) {
			ContaPagar c = contaNova();
			Long id = (long) (i+ 1);
			c.setId(id);
			c.setValor(CalculosComValores.setarValorTotalPositivo(conta.getValor(), BigDecimal.ZERO, BigDecimal.ZERO, conta.getTotalParcela()));
			contasSalvas.add(c);
		}
		
		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(contasASalvar)).thenReturn(contasSalvas);
		
		service.salvar(conta);
		
		Assertions.assertThat(contasSalvas.size()).isNotNull();
		Assertions.assertThat(contasSalvas.size()).isNotZero();
		Assertions.assertThat(contasSalvas.size()).isEqualTo(2);
		
		for (int i = 0; i < conta.getTotalParcela(); i++) {
			Assertions.assertThat(contasSalvas.get(i).getDataEmissao()).isBefore(contasSalvas.get(i).getVencimento());
			Assertions.assertThat(contasSalvas.get(i).getNotaFiscal()).isEqualTo(contaNova().getNotaFiscal());
			Assertions.assertThat(contasSalvas.get(i).getHistorico()).isEqualTo(contaNova().getHistorico());
			Assertions.assertThat(contasSalvas.get(i).getFornecedor()).isEqualTo(contaNova().getFornecedor());
			Assertions.assertThat(contasSalvas.get(i).getPlanoContaSecundaria()).isEqualTo(contaNova().getPlanoContaSecundaria());
			Assertions.assertThat(contasSalvas.get(i).getReterINSS()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getReterCOFINS()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getReterIR()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getIssPorcentagem()).isZero();			
			Assertions.assertThat(contasSalvas.get(i).getValor()).isEqualTo(BigDecimal.valueOf(500));
		}
		
		Mockito.verify(repository, Mockito.times(1)).save(contasASalvar);
	}	

	
	
	@Test
	@DisplayName("Deve salvar uma conta a pagar com 10 parcelas e sem impostos")
	public void salvarContaPagar10ParcelasSemImpostos() {
		
		List<ContaPagar> contasASalvar = new ArrayList<>();
		List<ContaPagar> contasSalvas = new ArrayList<>();

		ContaPagar conta = contaNova();
		conta.setTotalParcela(10);
		contasASalvar.add(conta);
		
		
		for(int i = 0; i < conta.getTotalParcela(); i++) {
			ContaPagar c = contaNova();
			Long id = (long) (i + 1);
			c.setId(id);
			c.setValor(CalculosComValores.setarValorTotalPositivo(conta.getValor(), BigDecimal.ZERO, BigDecimal.ZERO, conta.getTotalParcela()));
			contasSalvas.add(c);
		}
		
		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.empty());
		
		Mockito.when(repository.save(contasASalvar)).thenReturn(contasSalvas);
		
		service.salvar(conta);
		
		Assertions.assertThat(contasSalvas.size()).isNotNull();
		Assertions.assertThat(contasSalvas.size()).isNotZero();
		Assertions.assertThat(contasSalvas.size()).isEqualTo(10);
		
		for (int i = 0; i < conta.getTotalParcela(); i++) {
			Assertions.assertThat(contasSalvas.get(i).getDataEmissao()).isBefore(contasSalvas.get(i).getVencimento());
			Assertions.assertThat(contasSalvas.get(i).getNotaFiscal()).isEqualTo(contaNova().getNotaFiscal());
			Assertions.assertThat(contasSalvas.get(i).getHistorico()).isEqualTo(contaNova().getHistorico());
			Assertions.assertThat(contasSalvas.get(i).getFornecedor()).isEqualTo(contaNova().getFornecedor());
			Assertions.assertThat(contasSalvas.get(i).getPlanoContaSecundaria()).isEqualTo(contaNova().getPlanoContaSecundaria());
			Assertions.assertThat(contasSalvas.get(i).getReterINSS()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getReterCOFINS()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getReterIR()).isFalse();
			Assertions.assertThat(contasSalvas.get(i).getIssPorcentagem()).isZero();			
			Assertions.assertThat(contasSalvas.get(i).getValor()).isEqualTo(BigDecimal.valueOf(100));
		}
		
		Mockito.verify(repository, Mockito.times(1)).save(contasASalvar);
	}	
	
	
	
	
	@Test
	@DisplayName("Deve retornar erro quando a houver uma conta a pagar salva com mesmos dados")
	public void erroSalvarContaPagarDuplicada() {
		
		ContaPagar conta = contaNova();
		ContaPagar contaSalva = contaNova();
		contaSalva.setId(1l);
		
		Mockito.when( repository.findByDocumentoAndFornecedorAndEmpresa(
				conta.getDocumento(), conta.getFornecedor(), conta.getEmpresa())).thenReturn(Optional.of(contaSalva));
		
		Throwable exception = Assertions.catchThrowable(() -> service.testeRegistroJaCadastrado((conta)));
		
		Assertions.assertThat(exception)
			.isInstanceOf(DocumentoEFornecedorJaCadastradoException.class)
			.hasMessage("Já existe uma conta cadastrada com esta nota fiscal para esse fornecedor!");
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
		cp.setEmpresa(e);
		cp.setHistorico("TESTANDO A CONTA A PAGAR");
		cp.setReterINSS(false);
		cp.setReterCOFINS(false);
		cp.setReterIR(false);
		cp.setIssPorcentagem(BigDecimal.ZERO);
		cp.setDocumento("123");
		return cp;
	}
	
}
