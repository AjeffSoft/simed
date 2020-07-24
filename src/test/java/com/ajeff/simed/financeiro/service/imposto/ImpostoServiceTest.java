package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
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
	
	
	@Test
	@DisplayName("Deve retornar a aliquota do imposto PCCS")
	public void deveRetornarAliquotaPCCS() {
		TabelaIRPJ tabela = tabelaPJ();
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.aliquotaPCCS();
		
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(4.65));
	}

	
	@Test
	@DisplayName("Deve retornar a aliquota do imposto IRPJ")
	public void deveRetornarAliquotaIRPJ() {
		TabelaIRPJ tabela = tabelaPJ();
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.aliquotaIRPJ();
		
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1.5));
	}

	
	@Test
	@DisplayName("Deve retornar a aliquota do imposto IRPF")
	public void deveRetornarAliquotaIRPF() {
		BigDecimal valor = BigDecimal.valueOf(3000);
		TabelaIRPF tabela = tabelaPF();
		Mockito.when(tabelaPFRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.aliquotaIRPF(valor);
		
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(7.5));
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