package com.ajeff.simed.financeiro.service.contaPagar;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
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
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.financeiro.repository.ContasPagarRepository;
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.imposto.ImpostoService;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ContaPagarServiceTest {
	
	
	
	@InjectMocks
	private ContaPagarService service;
	@Mock
	private ApplicationEventPublisher publisher;
	
	@Mock
	private ImpostoService impostoService;
	@Mock
	private ContasPagarRepository repository;
	@Mock
	private FornecedoresRepository fornecedorRepository;
	@Mock
	private PlanoContaSecundaria pcs;
	@Mock
	private Empresa empresa;
	@Mock
	private Endereco endereco;	

	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar SIMPLES, sem parcela e sem impostos")
	public void salvarNovaContaPagarSimples() {
		ContaPagar conta = contaNova();
		ContaPagar contaSalva = contaNova();
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		Mockito.when(repository.save(Mockito.any(ContaPagar.class))).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor()).isEqualTo(BigDecimal.valueOf(1000));
		Mockito.verify(repository, Mockito.times(1)).save(conta);
	}
	
	
	@Test
	@DisplayName("Deve lançar erro DUPLICIDADE ao salvar uma conta a pagar SIMPLES, sem parcela e sem impostos")
	public void erroDuplicidadesalvarContaPagarSimples() {
		ContaPagar contaDuplicada = contaNova();
		ContaPagar conta = contaNova();
		conta.setId(1l);
		Mockito.when(repository.findByNotaFiscalAndFornecedor(Mockito.anyString(), Mockito.any(Fornecedor.class)))
			.thenReturn(Optional.of(contaDuplicada));
		
		Throwable result = catchThrowable(() -> service.salvar(conta));
		
		assertThat(result).isInstanceOf(DocumentoEFornecedorJaCadastradoException.class)
			.hasMessage("Já existe uma conta cadastrada com esta nota fiscal para esse fornecedor!");
		Mockito.verify(repository, Mockito.never()).save(conta);
	}
	
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de ISS")
	public void salvarNovaContaPagarRetencaoIss() {
		ContaPagar conta = contaNova();
		conta.setIssPorcentagem(BigDecimal.valueOf(5));
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(950));
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "ISS")).thenReturn(imposto);
		Mockito.when(repository.save(Mockito.any(ContaPagar.class))).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor()).isEqualTo(BigDecimal.valueOf(950));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		Mockito.verify(impostoService, Mockito.times(1)).novoImposto(conta, "ISS");
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
		cp.setEmpresa(empresa);
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
		fornecedor.setEndereco(endereco);
		Mockito.when(fornecedorRepository.save(Mockito.any(Fornecedor.class))).thenReturn(fornecedor);
		return fornecedor;
	}	

	private Imposto novoImposto() {
		Imposto imp = new Imposto();
		imp.setApuracao(LocalDate.of(2020, 7, 15));
		imp.setJuros(BigDecimal.ZERO);
		imp.setMulta(BigDecimal.ZERO);
		imp.setValorNF(BigDecimal.valueOf(1000));
		imp.setNumeroNF("123");
		imp.setStatus(StatusContaPagar.ABERTO);
		imp.setEmissaoNF(LocalDate.of(2020, 7, 15));
		imp.setNome("ISS");
		imp.setValor(BigDecimal.valueOf(50));
		imp.setTotal(imp.getValor());
		imp.setVencimento(LocalDate.of(2020, 7, 15));
		return imp;
	}	
	
}