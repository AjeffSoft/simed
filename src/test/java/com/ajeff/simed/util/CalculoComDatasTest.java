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


	/*
	 * diferencaEntreDuasDatas
	 * 
	 */		
	@Test
	@DisplayName("Deve retornar a quantidade de dias entre duas datas")
	public void retornarDiasDuasDatas() {
		LocalDate dataInicio = LocalDate.of(2020, 4, 10);
		LocalDate dataFinal = LocalDate.of(2020, 4, 20);
		
		Long dias = CalculosComDatas.diferencaEntreDuasDatas(dataInicio, dataFinal);
		
		org.assertj.core.api.Assertions.assertThat(dias).isNotNull();
		org.assertj.core.api.Assertions.assertThat(dias).isNotNegative();
		org.assertj.core.api.Assertions.assertThat(dias).isNotZero();	
		org.assertj.core.api.Assertions.assertThat(dias).isEqualTo(10);		
	}
	
	@Test
	@DisplayName("Deve retornar a quantidade de dias zerada pois a data final é menor que a data inicial")
	public void retornarZeradoDuasDatas() {
		LocalDate dataInicio = LocalDate.of(2020, 4, 20);
		LocalDate dataFinal = LocalDate.of(2020, 4, 10);
		
		Long dias = CalculosComDatas.diferencaEntreDuasDatas(dataInicio, dataFinal);
		
		org.assertj.core.api.Assertions.assertThat(dias).isNotNull();
		org.assertj.core.api.Assertions.assertThat(dias).isNotNegative();
		org.assertj.core.api.Assertions.assertThat(dias).isZero();	
	}
	
	@Test
	@DisplayName("Deve retornar erro, pois não foi informado a data inicial")
	public void retornarErroDataInicial() {
		LocalDate dataFinal = LocalDate.of(2020, 4, 10);
		
		Assertions.assertThrows(DataNaoInformadaException.class, 
				() -> CalculosComDatas.diferencaEntreDuasDatas(null, dataFinal));
	}	

	
	@Test
	@DisplayName("Deve retornar erro, pois não foi informado a data final")
	public void retornarErroDataFinal() {
		LocalDate dataInicial = LocalDate.of(2020, 4, 10);
		
		Assertions.assertThrows(DataNaoInformadaException.class, 
				() -> CalculosComDatas.diferencaEntreDuasDatas(dataInicial, null));
	}
	
	
	
	
	/*
	 * setarParaUltimoDiaMes
	 * 
	 */	
	@Test
	@DisplayName("Deve retornar a data com o ultimo dia do mês da data informada")
	public void ultimoDiaMesTest() {
		LocalDate data = LocalDate.of(2020, 4, 18);
		
		LocalDate result = CalculosComDatas.setarParaUltimoDiaMes(data);
		
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isAfter(data);
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 30));
	}


	
	/*
	 * setarDataSomandoDiasNoInicioMes
	 * 
	 */	
	@Test
	@DisplayName("Deve retornar a data somando nenhum dia no inicio do mês")
	public void dataSomandoZeroDiasInicioMesTest() {
		LocalDate data = LocalDate.of(2020, 4, 18);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 0, false, false);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 1));
	}
	
	@Test
	@DisplayName("Deve retornar a data somando 10 dias no inicio do mês e retornando final de semana")
	public void dataSomando10DiasInicioMesTest() {
		LocalDate data = LocalDate.of(2020, 4, 18);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, false, false);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 11));
	}

	@Test
	@DisplayName("Deve retornar a data somando 40 dias no inicio do mês")
	public void dataSomando40DiasInicioMesTest() {
		LocalDate data = LocalDate.of(2020, 4, 18);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 40, false, false);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 5, 11));
	}

	@Test
	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no SABADO e indo para segunda")
	public void dataSomando10CaindoSabadoTest() {
		LocalDate data = LocalDate.of(2020, 4, 10);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, false);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 13));
	}
	
	@Test
	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no SABADO e antecipando para sexta")
	public void dataSomando10CaindoSabadoAntecipandoTest() {
		LocalDate data = LocalDate.of(2020, 4, 10);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, true);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 10));
	}
	
	@Test
	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no DOMINGO e indo para segunda")
	public void dataSomando10CaindoDomingoTest() {
		LocalDate data = LocalDate.of(2020, 4, 11);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, false);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 13));
	}
	
	@Test
	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no DOMINGO e antecipando para sexta")
	public void dataSomando10CaindoDomingoAntecipandoTest() {
		LocalDate data = LocalDate.of(2020, 4, 11);
		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, true);
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 10));
	}
	


	/*
	 * dataDomingo
	 */
	@Test
	public void dataDomingo() {
		LocalDate data = LocalDate.of(2019, 11, 24);
		boolean dia = CalculosComDatas.dataDomingo(data);
		assertTrue(dia);
	}

	
	/*
	 * dataSabado
	 */
	@Test
	public void dataSabado() {
		LocalDate data = LocalDate.of(2019, 11, 23);
		boolean dia = CalculosComDatas.dataSabado(data);
		assertTrue(dia);
	}
	
	
	/*
	 * numeroDiaSemana - Retorna o numero da semana no mês
	 */
	@Test
	public void dataSemana() {
		LocalDate data = LocalDate.of(2019, 11, 19);
		int dia = CalculosComDatas.numeroDiaSemana(data);
		assertEquals(2, dia);
	}
	
	
	
	/*
	 * dataFinalSemana
	 */
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
	

	/*
	 * emissaoMenorIgualVencimento
	 */
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


	
	/*
	 * setarVencimentoXTotalParcelas
	 */
	@Test
	public void setarVencimentoCincoDiasAposEmissaoEUmaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 1, 5);
		assertEquals(LocalDate.of(2019, 11, 25), vencimento);
	}

	@Test
	public void setarVencimentoCincoDiasAposEmissaoEQuatroParcelas() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 4, 5);
		assertEquals(LocalDate.of(2019, 12, 10), vencimento);
	}
	
	@Test
	public void setarVencimentoCincoDiasAposEmissaoENenhumaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 0, 5);
		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
	}	

	@Test
	public void setarVencimentoNenhumDiaAposEmissaoENenhumaParcela() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 0, 0);
		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
	}	
	
}
