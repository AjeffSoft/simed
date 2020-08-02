package com.ajeff.simed.financeiro.service.contaPagar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

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
		Mockito.when(impostoService.valorISSRetido(Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(50));
		Mockito.when(repository.save(Mockito.any(ContaPagar.class))).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor().setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(50));
	}	
	
	
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de INSS")
	public void salvarNovaContaPagarRetencaoInss() {
		ContaPagar conta = contaNova();
		conta.setReterINSS(true);
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		imposto.setNome("INSS");
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(890));
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "INSS")).thenReturn(imposto);
		Mockito.when(impostoService.valorINSSRetido(Mockito.any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(110));
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor().setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(110));
	}
	
	@Test
	@DisplayName("Não deve haver impostos INSS pois o fornecedor é PJ")
	public void retornarNullTipoFornecedorInvalidoParaRetencaoInss() {
		ContaPagar conta = contaNova();
		conta.setReterINSS(true);
		conta.getFornecedor().setTipo("J");

		service.salvar(conta);

		//FIXME Corrigir para cada teste ter sua instancia de impostoService
		//está com times(1) porque o método acima chama esses métodos
		Mockito.verify(impostoService, Mockito.times(1)).novoImposto(conta, "INSS");
		Mockito.verify(impostoService, Mockito.times(1)).valorINSSRetido(conta.getValor());
	}	
	


	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de PCCS")
	public void salvarNovaContaPagarRetencaoPCCS() {
		ContaPagar conta = contaNova();
		conta.setReterCOFINS(true);
		conta.getFornecedor().setTipo("J");
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		imposto.setNome("PCCS");
		contaSalva.setId(1l);
		contaSalva.setValor(BigDecimal.valueOf(985));
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "PCCS")).thenReturn(imposto);
		Mockito.when(impostoService.valorPCCSRetido(Mockito.any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(15));
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor().setScale(2));
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(15));
	}	

	@Test
	@DisplayName("Não deve haver impostos PCCS pois o fornecedor é PF")
	public void retornarNullTipoFornecedorInvalidoParaRetencaoPCCS() {
		ContaPagar conta = contaNova();
		conta.setReterCOFINS(true);
		conta.getFornecedor().setTipo("F");

		service.salvar(conta);

		//FIXME Corrigir para cada teste ter sua instancia de impostoService
		//está com times(1) porque o método acima chama esses métodos
		Mockito.verify(impostoService, Mockito.times(1)).novoImposto(conta, "PCCS");
		Mockito.verify(impostoService, Mockito.times(1)).valorPCCSRetido(conta.getValor());
	}	
	
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRRF")
	public void salvarNovaContaPagarRetencaoIRPJ() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.getFornecedor().setTipo("J");
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");
		contaSalva.setId(1l);
		contaSalva.setValor(BigDecimal.valueOf(990));
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		Mockito.when(impostoService.valorIRRFRetido(Mockito.any(BigDecimal.class), 
				Mockito.any(BigDecimal.class), Mockito.any(BigDecimal.class), Mockito.anyString())).thenReturn(BigDecimal.valueOf(10));
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor().setScale(2));
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(10));
	}	
	
	
	@Test
	@DisplayName("Deve lançar um erro quando tentar salvar uma conta já existente")
	public void salvarContaPagarExistente() {
		ContaPagar conta = contaNova();
		ContaPagar conta1 = contaNova();
		conta1.setId(1l);
		Mockito.when(repository.findByNotaFiscalAndFornecedor(Mockito.anyString(), Mockito.any(Fornecedor.class))).thenReturn(Optional.of(conta1));
		
		Throwable result = assertThrows(DocumentoEFornecedorJaCadastradoException.class, () -> service.salvar(conta));
		
		assertThat(result).isInstanceOf(DocumentoEFornecedorJaCadastradoException.class)
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
		fornecedor.setTipo("F");
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