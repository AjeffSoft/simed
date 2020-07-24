package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;

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
import com.ajeff.simed.financeiro.model.TabelaIRPJ;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class ImpostoServiceTest {
	
	
	@InjectMocks
	private ImpostoService service;
	
	@Mock
	private TabelasIrpjRepository tabelaRepository;
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	@DisplayName("Deve retornar a aliquota do imposto PCCS")
	public void deveRetornarAliquotaPCCS() {
		TabelaIRPJ tabela = tabela();
		Mockito.when(tabelaRepository.findOne(1l)).thenReturn(tabela);
		
		BigDecimal result = service.aliquotaPCCS();
		
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(4.65));
		
	}
	
	
	private TabelaIRPJ tabela() {
		TabelaIRPJ tabela = new TabelaIRPJ();
		tabela.setId(1l);
		tabela.setAliquotaCOFINS(BigDecimal.valueOf(3));
		tabela.setAliquotaPIS(BigDecimal.valueOf(0.65));
		tabela.setAliquotaCSLL(BigDecimal.valueOf(1));
		Mockito.when(tabelaRepository.save(Mockito.any(TabelaIRPJ.class))).thenReturn(tabela);
		return tabela;
	}

	
}