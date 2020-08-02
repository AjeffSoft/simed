package com.ajeff.simed.financeiro.service.imposto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import com.ajeff.simed.financeiro.model.TabelaIRPF;
import com.ajeff.simed.financeiro.model.TabelaIRPJ;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ImpostoServiceTest {
	
	
	@InjectMocks
	private ImpostoService service;
	
	@Mock
	private TabelasIrpjRepository tabelaRepository;
	@Mock
	private TabelasIrpfRepository tabelaPFRepository;
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	
	/*
	 * PIS/COFINS/CSLL
	 */
	@Test
	@DisplayName("Deve retornar o valor retenção do imposto PCCS")
	public void deveRetornarValorPCCS() {
		TabelaIRPJ tabela = tabelaPJ();
		BigDecimal valor = BigDecimal.valueOf(2000);
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.valorPCCSRetido(valor);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(93).setScale(2));
	}
	
	@Test
	@DisplayName("Deve retornar o valor zerado quando o valor vier zerado do imposto PCCS")
	public void deveRetornarValorPCCSZerado() {
		TabelaIRPJ tabela = tabelaPJ();
		BigDecimal valor = BigDecimal.valueOf(0);
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.valorPCCSRetido(valor);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(0));
	}
	
	

	/*
	 * ISS
	 */
	@Test
	@DisplayName("Deve retornar o valor retenção do imposto ISS")
	public void deveRetornarValorISS() {
		BigDecimal issPorcentagem = BigDecimal.valueOf(10);
		BigDecimal valor = BigDecimal.valueOf(2000);
		
		BigDecimal result = service.valorISSRetido(valor, issPorcentagem);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(200).setScale(2));
	}

	@Test
	@DisplayName("Deve retornar zerado quando o valor passado for zerado do imposto ISS")
	public void deveRetornarValorISSZerado() {
		BigDecimal issPorcentagem = BigDecimal.valueOf(10);
		BigDecimal valor = BigDecimal.valueOf(0);
		
		BigDecimal result = service.valorISSRetido(valor, issPorcentagem);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(0));
	}
	
	
	
	/*
	 * INSS
	 */
	@Test
	@DisplayName("Deve retornar o valor retenção do imposto INSS fixo a 11%")
	public void deveRetornarValorINSS() {
		BigDecimal valor = BigDecimal.valueOf(2000);
		
		BigDecimal result = service.valorINSSRetido(valor);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(220).setScale(2));
	}

	@Test
	@DisplayName("Deve retornar o valor retenção do imposto INSS acima do teto máximo")
	public void deveRetornarValorINSSTetoMaximo() {
		BigDecimal valor = BigDecimal.valueOf(10000);
		
		BigDecimal result = service.valorINSSRetido(valor);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(671.11).setScale(2));
	}

	@Test
	@DisplayName("Deve retornar zerado quando o valor passado for zerado do imposto INSS")
	public void deveRetornarValorINSSZerado() {
		BigDecimal valor = BigDecimal.valueOf(0);
		
		BigDecimal result = service.valorINSSRetido(valor);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(0));
	}
	
	
	
	/*
	 * IRPJ - PESSOA JURIDICA
	 */
	@Test
	@DisplayName("Deve retornar o valor do imposto IRPJ")
	public void deveRetornarValorIRPJ() {
		TabelaIRPJ tabela = tabelaPJ();
		BigDecimal valor = BigDecimal.valueOf(1000);
		BigDecimal inss = BigDecimal.valueOf(0);
		BigDecimal dependente = BigDecimal.valueOf(0);
		String tipo = "J";
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.valorIRRFRetido(valor, inss, dependente, tipo);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(15).setScale(2));
	}

	@Test
	@DisplayName("Deve laçar erro quando o valor base for zerado do imposto IRPJ")
	public void deveRetornarValorIRPJZerado() {
		TabelaIRPJ tabela = tabelaPJ();
		BigDecimal valor = BigDecimal.valueOf(0);
		BigDecimal inss = BigDecimal.valueOf(0);
		BigDecimal dependente = BigDecimal.valueOf(0);
		String tipo = "J";
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> service.valorIRRFRetido(valor, inss, dependente, tipo));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}
	
	@Test
	@DisplayName("Deve lancar erro ao não localizar a tabela de aliquotas IRPJ")
	public void deveRetornarErroTabelaAliquotaNaoLocalizado() {
		BigDecimal valor = BigDecimal.valueOf(5000);
		BigDecimal inss = BigDecimal.valueOf(0);
		BigDecimal dependente = BigDecimal.valueOf(0);
		String tipo = "J";
		Mockito.when(tabelaRepository.findOne(Mockito.anyLong())).thenReturn(null);
		
		Throwable result = assertThrows(RegistroNaoCadastradoException.class, ()-> service.valorIRRFRetido(valor, inss, dependente, tipo));
		
		assertThat(result).isInstanceOf(RegistroNaoCadastradoException.class).hasMessage("Não localizado a tabela de aliquotas!");
	}
	
	
	
	

	/*
	 * IRPF - PESSOA FISICA
	 */
	@Test
	@DisplayName("Deve retornar o valor do imposto IRPF simples")
	public void deveRetornarAliquotaIRPF() {
		BigDecimal valor = BigDecimal.valueOf(2000);
		BigDecimal inss = BigDecimal.valueOf(0);
		BigDecimal dependente = BigDecimal.valueOf(0);
		String tipo = "F";
		List<TabelaIRPF> tabelas = new ArrayList<>();
		TabelaIRPF tabela = tabelaPF();
		tabelas.add(tabela);
		Mockito.when(tabelaPFRepository.findAll()).thenReturn(tabelas);
				
		BigDecimal result = service.valorIRRFRetido(valor, inss, dependente, tipo);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(7.20).setScale(2));
	}

	@Test
	@DisplayName("Deve retornar o valor do imposto IRPF com INSS retido")
	public void deveRetornarAliquotaIRPFRetencaoINSS() {
		BigDecimal valor = BigDecimal.valueOf(2500);
		BigDecimal inss = BigDecimal.valueOf(275);
		BigDecimal dependente = BigDecimal.valueOf(0);
		String tipo = "F";
		List<TabelaIRPF> tabelas = new ArrayList<>();
		TabelaIRPF tabela = tabelaPF();
		tabelas.add(tabela);
		Mockito.when(tabelaPFRepository.findAll()).thenReturn(tabelas);
		
		BigDecimal result = service.valorIRRFRetido(valor, inss, dependente, tipo);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(24.08).setScale(2));
	}

	@Test
	@DisplayName("Deve retornar o valor do imposto IRPF com INSS retido e um dependente")
	public void deveRetornarValorIRPFRetencaoINSSUmDependente() {
		BigDecimal valor = BigDecimal.valueOf(2800);
		BigDecimal inss = BigDecimal.valueOf(308);
		BigDecimal dependente = BigDecimal.valueOf(189.90);
		String tipo = "F";
		List<TabelaIRPF> tabelas = new ArrayList<>();
		TabelaIRPF tabela = tabelaPF();
		tabelas.add(tabela);
		Mockito.when(tabelaPFRepository.findAll()).thenReturn(tabelas);
		
		BigDecimal result = service.valorIRRFRetido(valor, inss, dependente, tipo);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(29.86).setScale(2));
	}
	
	@Test
	@DisplayName("Deve lançar erro quando o valor base for zerado para imposto IRPF")
	public void deveLancarErroQuandoValorForZeradoIRPF() {
		BigDecimal valor = BigDecimal.valueOf(0);
		BigDecimal inss = BigDecimal.valueOf(308);
		BigDecimal dependente = BigDecimal.valueOf(189.90);
		String tipo = "F";
		List<TabelaIRPF> tabelas = new ArrayList<>();
		TabelaIRPF tabela = tabelaPF();
		tabelas.add(tabela);
		Mockito.when(tabelaPFRepository.findAll()).thenReturn(tabelas);
		
		Throwable result = assertThrows(RegistroNaoCadastradoException.class, () -> service.valorIRRFRetido(valor, inss, dependente, tipo));
		
		assertThat(result).isInstanceOf(RegistroNaoCadastradoException.class).hasMessage("Tabela de aliquotas não encontrada");
	}

	@Test
	@DisplayName("Deve lançar erro quando a tabela do imposto não for localizado para imposto IRPF")
	public void deveLancarErroQuandoTabelaIRPFNaoLocalizado() {
		BigDecimal valor = BigDecimal.valueOf(2000);
		BigDecimal inss = BigDecimal.valueOf(308);
		BigDecimal dependente = BigDecimal.valueOf(189.90);
		String tipo = "F";
		
		Throwable result = assertThrows(RegistroNaoCadastradoException.class, () -> service.valorIRRFRetido(valor, inss, dependente, tipo));
		
		assertThat(result).isInstanceOf(RegistroNaoCadastradoException.class).hasMessage("Tabela de aliquotas não encontrada");
	}
	
	
	
	
	
	
	private TabelaIRPJ tabelaPJ() {
		TabelaIRPJ tabela = new TabelaIRPJ();
		tabela.setId(1l);
		tabela.setAliquotaCOFINS(BigDecimal.valueOf(3));
		tabela.setAliquotaPIS(BigDecimal.valueOf(0.65));
		tabela.setAliquotaCSLL(BigDecimal.valueOf(1));
		tabela.setAliquotaIR(BigDecimal.valueOf(1.5));
		Mockito.when(tabelaRepository.save(Mockito.any(TabelaIRPJ.class))).thenReturn(tabela);
		return tabela;
	}

	private TabelaIRPF tabelaPF() {
		TabelaIRPF tabela = new TabelaIRPF(1l, "1", BigDecimal.valueOf(1903.99), BigDecimal.valueOf(2826.65), 
				BigDecimal.valueOf(7.5), BigDecimal.valueOf(142.80), BigDecimal.valueOf(189.50));	

		Mockito.when(tabelaPFRepository.save(Mockito.any(TabelaIRPF.class))).thenReturn(tabela);
		return tabela;
	}

	
}