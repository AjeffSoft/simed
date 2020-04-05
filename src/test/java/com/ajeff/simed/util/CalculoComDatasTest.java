package com.ajeff.simed.util;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ajeff.simed.exceptions.DataNaoInformadaException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;

public class CalculoComDatasTest {

	
	@Test
	public void dataDomingo() {
		LocalDate data = LocalDate.of(2019, 11, 24);
		boolean dia = CalculosComDatas.dataDomingo(data);
		assertTrue(dia);
	}
	
	@Test
	public void dataSabado() {
		LocalDate data = LocalDate.of(2019, 11, 23);
		boolean dia = CalculosComDatas.dataSabado(data);
		assertTrue(dia);
	}
	
	@Test
	public void dataSemana() {
		LocalDate data = LocalDate.of(2019, 11, 19);
		int dia = CalculosComDatas.numeroDiaSemana(data);
		assertEquals(2, dia);
	}
	
	@Test
	public void deveRetornarTrueSeFinalSemana() {
		LocalDate data = LocalDate.of(2019, 11, 17);
		Boolean dia = CalculosComDatas.dataFinalSemana(data);
		assertTrue(dia);
	}
	
	@Test
	public void deveRetornarFalseSeNaoForFinalSemana() {
		LocalDate data = LocalDate.of(2019, 11, 19);
		Boolean dia = CalculosComDatas.dataFinalSemana(data);
		assertFalse(dia);
	}
	

	
	
	
	@Test
	@DisplayName("Deve retornar VERDADEIRO se a data de emissão for menor que a data do vencimento")
	public void dataEmissaoMenorDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = LocalDate.of(2019, 11, 20);
		assertTrue(CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}

	@Test
	@DisplayName("Deve dar ERRO quando a data de emissão é maior que a data de vencimento")
	public void dataEmissaoMaiorDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		Assertions.assertThrows(VencimentoMenorEmissaoException.class, 
				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}
	
	@Test
	@DisplayName("Deve dar ERRO quando a data de emissão e a data de vencimento não é informada")
	public void dataEmissaoEVencimentoNulo() {
		LocalDate emissao = null;
		LocalDate vencimento = null;
		Assertions.assertThrows(DataNaoInformadaException.class, 
				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}	
	
	@Test
	@DisplayName("Deve dar ERRO quando a data de emissão não for informada")
	public void dataEmissaoNula() {
		LocalDate emissao = null;
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		Assertions.assertThrows(DataNaoInformadaException.class, 
				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}	
	
	@Test
	@DisplayName("Deve dar ERRO quando a data de emissão não for informada")
	public void dataVencimentoNula() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = null;
		Assertions.assertThrows(DataNaoInformadaException.class, 
				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}	

	@Test
	@DisplayName("Deve retornar VERDADEIRO quando a data de emissão for igual a data de vencimento")
	public void dataEmissaoIgualDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		assertTrue(CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
	}
	
	

	@Test
	public void setarVencimentoCincoDiasAposEmissaoEUmaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimento(emissao, 1, 5);
		assertEquals(LocalDate.of(2019, 11, 25), vencimento);
	}

	@Test
	public void setarVencimentoCincoDiasAposEmissaoEQuatroParcelas() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimento(emissao, 4, 5);
		assertEquals(LocalDate.of(2019, 12, 10), vencimento);
	}
	
	@Test
	public void setarVencimentoCincoDiasAposEmissaoENenhumaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimento(emissao, 0, 5);
		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
	}	

	@Test
	public void setarVencimentoNenhumDiaAposEmissaoENenhumaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimento(emissao, 0, 0);
		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
	}	
	
}
