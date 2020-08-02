package com.ajeff.simed.financeiro.service.contaPagar;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

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
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
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
		Mockito.when(repository.save(Mockito.any(ContaPagar.class))).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(50).setScale(2));
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
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(110).setScale(2));
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
		Mockito.when(impostoService.aliquotaPCCS()).thenReturn(BigDecimal.valueOf(1.5));
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(15).setScale(2));
	}	

	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPJ")
	public void salvarNovaContaPagarRetencaoIRPJ() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.getFornecedor().setTipo("J");
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(985));
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "J")).thenReturn(BigDecimal.valueOf(1.5));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(0));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(15).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
	}	

	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPF")
	public void salvarNovaContaPagarRetencaoIRPF() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.getFornecedor().setTipo("F");
		ContaPagar contaSalva = contaNova();
		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(995));
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "F")).thenReturn(BigDecimal.valueOf(1));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(5));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(5).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(1);
	}	

	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPJ, ISS e PCCS")
	public void salvarNovaContaPagarRetencaoIRPJISSPCCS() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.setReterCOFINS(true);
		conta.setIssPorcentagem(BigDecimal.valueOf(5));
		conta.setValor(BigDecimal.valueOf(2500));
		conta.getFornecedor().setTipo("J");
		ContaPagar contaSalva = contaNova();

		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");

		Imposto impostoCofins = novoImposto();
		impostoCofins.setNome("PCCS");

		Imposto impostoIss = novoImposto();
		impostoIss.setNome("ISS");
		
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(2221.25));
		contaSalva.getImpostos().add(imposto);
		contaSalva.getImpostos().add(impostoCofins);
		contaSalva.getImpostos().add(impostoIss);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		Mockito.when(impostoService.novoImposto(conta, "PCCS")).thenReturn(impostoCofins);
		Mockito.when(impostoService.novoImposto(conta, "ISS")).thenReturn(impostoIss);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "J")).thenReturn(BigDecimal.valueOf(1.5));
		Mockito.when(impostoService.aliquotaPCCS()).thenReturn(BigDecimal.valueOf(4.65));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(0));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		BigDecimal vlImpostos = contaSalva.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
		
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(vlImpostos).isEqualTo(BigDecimal.valueOf(278.75).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(3);
	}
	
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPJ, INSS - sendo o INSS ignorado pois só PF")
	public void salvarNovaContaPagarRetencaoIRPJINSS() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.setReterINSS(true);
		conta.getFornecedor().setTipo("J");
		ContaPagar contaSalva = contaNova();

		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");

		Imposto impostoInss = novoImposto();
		impostoInss.setNome("INSS");
		
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(985));
		contaSalva.getImpostos().add(imposto);
		contaSalva.getImpostos().add(impostoInss);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		Mockito.when(impostoService.novoImposto(conta, "INSS")).thenReturn(impostoInss);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "J")).thenReturn(BigDecimal.valueOf(1.5));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(0));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
	
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(contaSalva.getImpostos().get(0).getValor()).isEqualTo(BigDecimal.valueOf(15).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(2);
	}
	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPF e ISS")
	public void salvarNovaContaPagarRetencaoIRPFISS() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.setIssPorcentagem(BigDecimal.valueOf(5));
		conta.setValor(BigDecimal.valueOf(2500));
		conta.getFornecedor().setTipo("F");
		ContaPagar contaSalva = contaNova();

		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");

		Imposto impostoIss = novoImposto();
		impostoIss.setNome("ISS");
		
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(2287.50));
		contaSalva.getImpostos().add(imposto);
		contaSalva.getImpostos().add(impostoIss);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		Mockito.when(impostoService.novoImposto(conta, "ISS")).thenReturn(impostoIss);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "F")).thenReturn(BigDecimal.valueOf(7.5));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(100));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
		BigDecimal vlImpostos = contaSalva.getImpostos().stream().map(i -> i.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
	
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
		assertThat(vlImpostos).isEqualTo(BigDecimal.valueOf(212.50).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(2);
	}


	
	@Test
	@DisplayName("Deve salvar uma nova conta a pagar com retenção de IRPF um dependente")
	public void salvarNovaContaPagarRetencaoIRPFComUmDependente() {
		ContaPagar conta = contaNova();
		conta.setReterIR(true);
		conta.setValor(BigDecimal.valueOf(2500));
		conta.getFornecedor().setTipo("F");
		conta.getFornecedor().setDependente(1);
		ContaPagar contaSalva = contaNova();

		Imposto imposto = novoImposto();
		imposto.setNome("IRRF");

		
		contaSalva.setId(1l);
		contaSalva.setStatus(StatusContaPagar.ABERTO);
		contaSalva.setValor(BigDecimal.valueOf(2287.50));
		contaSalva.getImpostos().add(imposto);
		Mockito.when(impostoService.novoImposto(conta, "IRRF")).thenReturn(imposto);
		
		Mockito.when(impostoService.aliquotaIRRF(conta.getValor(), "F")).thenReturn(BigDecimal.valueOf(7.5));
		Mockito.when(impostoService.deducaoIRPF(conta.getValor())).thenReturn(BigDecimal.valueOf(140));
		
		Mockito.when(repository.save(conta)).thenReturn(contaSalva);
		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
		
		service.salvar(conta);
		
	
		assertThat(contaSalva.getId()).isNotNull();
		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
		assertThat(contaSalva.getValor().setScale(2)).isEqualTo(conta.getValor());
//		assertThat(vlImpostos).isEqualTo(BigDecimal.valueOf(212.50).setScale(2));
		assertThat(contaSalva.getImpostos()).isNotEmpty();
		assertThat(contaSalva.getImpostos().size()).isEqualTo(2);
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