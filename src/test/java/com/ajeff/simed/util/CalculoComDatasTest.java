package com.ajeff.simed.util;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

public class CalculoComDatasTest {

	
	@Test
	public void dataDomingo() {
		LocalDate data = LocalDate.of(2019, 11, 24);
		int dia = CalculosComDatas.numeroDiaSemana(data);
		assertEquals(7, dia);
	}
	
	@Test
	public void dataSabado() {
		LocalDate data = LocalDate.of(2019, 11, 23);
		int dia = CalculosComDatas.numeroDiaSemana(data);
		assertEquals(6, dia);
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
	public void dataEmissaoMenorDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = LocalDate.of(2019, 11, 20);
		assertFalse(CalculosComDatas.emissaoMaiorVancimento(emissao, vencimento));
	}

	@Test
	public void dataEmissaoMaiorDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		assertTrue(CalculosComDatas.emissaoMaiorVancimento(emissao, vencimento));
	}

	@Test
	public void dataEmissaoIgualDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		assertFalse(CalculosComDatas.emissaoMaiorVancimento(emissao, vencimento));
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
