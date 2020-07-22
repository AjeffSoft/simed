package com.ajeff.simed.financeiro.service.imposto;

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
public class ImpostoServiceTest {
	
	
	@InjectMocks
	private ImpostoService service;
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
//	@Test
//	@DisplayName("Deve salvar uma nova conta a pagar SIMPLES, sem parcela e sem impostos")
//	public void salvarNovaContaPagarSimples() {
//		ContaPagar conta = contaNova();
//		ContaPagar contaSalva = contaNova();
//		contaSalva.setId(1l);
//		contaSalva.setStatus(StatusContaPagar.ABERTO);
//		Mockito.when(repository.save(Mockito.any(ContaPagar.class))).thenReturn(contaSalva);
//		Mockito.doNothing().when(publisher).publishEvent((new ContaPagarSalvaEvent(contaSalva)));
//		
//		service.salvar(conta);
//		
//		assertThat(contaSalva.getId()).isNotNull();
//		assertThat(contaSalva.getStatus()).isEqualTo(StatusContaPagar.ABERTO);
//		assertThat(contaSalva.getValor()).isEqualTo(BigDecimal.valueOf(1000));
//		Mockito.verify(repository, Mockito.times(1)).save(conta);
//	}
	

//	private ContaPagar contaNova() {
//		ContaPagar cp = new ContaPagar();
//		cp.setDataEmissao(LocalDate.of(2020, 3, 20));
//		cp.setVencimento(LocalDate.of(2020, 3, 25));
//		cp.setValor(new BigDecimal(1000));
//		cp.setNotaFiscal("0001");
//		cp.setTotalParcela(1);
//		cp.setPlanoContaSecundaria(pcs);
//		cp.setFornecedor(novoFornecedor());
//		cp.setEmpresa(empresa);
//		cp.setHistorico("TESTANDO A CONTA A PAGAR");
//		cp.setReterINSS(false);
//		cp.setReterCOFINS(false);
//		cp.setReterIR(false);
//		cp.setIssPorcentagem(BigDecimal.ZERO);
//		cp.setDocumento("123");
//		return cp;
//	}
	
//	private Fornecedor novoFornecedor() {
//		Fornecedor fornecedor = new Fornecedor();
//		fornecedor.setNome("FORNECEDOR TESTE LTDA");
//		fornecedor.setFantasia("FORNECEDOR TESTE");
//		fornecedor.setSigla("FORTESTE");
//		fornecedor.setClifor(true);
//		fornecedor.setEndereco(endereco);
//		Mockito.when(fornecedorRepository.save(Mockito.any(Fornecedor.class))).thenReturn(fornecedor);
//		return fornecedor;
//	}	

//	private Imposto novoImposto() {
//		Imposto imp = new Imposto();
//		imp.setApuracao(LocalDate.of(2020, 7, 15));
//		imp.setJuros(BigDecimal.ZERO);
//		imp.setMulta(BigDecimal.ZERO);
//		imp.setValorNF(BigDecimal.valueOf(1000));
//		imp.setNumeroNF("123");
//		imp.setStatus(StatusContaPagar.ABERTO);
//		imp.setEmissaoNF(LocalDate.of(2020, 7, 15));
//		imp.setNome("ISS");
//		imp.setValor(BigDecimal.valueOf(50));
//		imp.setTotal(imp.getValor());
//		imp.setVencimento(LocalDate.of(2020, 7, 15));
//		return imp;
//	}	
	
}