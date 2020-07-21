package com.ajeff.simed.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ajeff.simed.exceptions.DataNaoInformadaException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;

public class DatasUtilsTest {

	
	
//	@Test
//	@DisplayName("Deve retornar data do dia útil somando dias informados a partir inicio mês")
//	public void deveRetornarDataDiaUtil() {
//		LocalDate data = LocalDate.of(2020, 7, 21);
//		
//		LocalDate result = DatasUtils.somarDiasNoInicioMesRetornandoDataUtil(data, 20);
//
//		assertThat(result).isEqualTo(LocalDate.of(2020, 7, 20));
//	}		

	@Test
	@DisplayName("Deve retornar data do dia útil somando dias informados a partir inicio mês caindo em um final semana")
	public void deveRetornarDataDiaUtilParandoFeriadoEAntecipando() {
		LocalDate data = LocalDate.of(2020, 7, 5);
		
		LocalDate result = DatasUtils.somarDiasNoInicioMesRetornandoDataUtil(data, 20);
		
		assertThat(result).isEqualTo(LocalDate.of(2020, 7, 30));
	}		
	

	@Test
	@DisplayName("Deve retornar TRUE se a data informada for util")
	public void deveRetornarTrueSeDiaUtil() {
		LocalDate data = LocalDate.of(2020, 7, 21);
		
		boolean result = DatasUtils.diaUtil(data);

		assertThat(result).isTrue();
	}		

	@Test
	@DisplayName("Deve retornar FALSE, pois a data é um final de semana")
	public void deveRetornarFalsePoisDataFinalSemanaNaoUtil() {
		LocalDate data = LocalDate.of(2020, 7, 25);
		
		boolean result = DatasUtils.diaUtil(data);
		
		assertThat(result).isFalse();
	}		

	@Test
	@DisplayName("Deve retornar FALSE, pois a data é um feriado")
	public void deveRetornarFalsePoisDataFeriadoNaoUtil() {
		LocalDate data = LocalDate.of(2020, 10, 12);
		
		boolean result = DatasUtils.diaUtil(data);
		
		assertThat(result).isFalse();
	}
	
	
	@Test
	@DisplayName("Deve lançar erro quando a data do dia útil não for informada ")
	public void deveLancarErroQuandoNaoInformadoDataDiaUtil() {
		LocalDate data = null;
		
		Throwable result = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.diaUtil(data));

		assertThat(result).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data não foi informada!");		
	}		
	
	
	
	@Test
	@DisplayName("Deve retornar TRUE se a data informada for feriado")
	public void deveRetornarTrueSeFeriado() {
		LocalDate data = LocalDate.of(2019, 10, 12);
		
		boolean result = DatasUtils.diaFeriado(data);

		assertThat(result).isTrue();
	}	

	
	@Test
	@DisplayName("Deve retornar FALSE se a data informada não for feriado")
	public void deveRetornarFalseSeNaoFeriado() {
		LocalDate data = LocalDate.of(2019, 1, 2);
		
		boolean result = DatasUtils.diaFeriado(data);
		
		assertThat(result).isFalse();
	}
	
	
	@Test
	@DisplayName("Deve lançar erro quando a data do feriado não for informada ")
	public void deveLancarErroQuandoNaoInformadoDataFeriado() {
		LocalDate data = null;
		
		Throwable result = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.diaFeriado(data));

		assertThat(result).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data não foi informada!");		
	}		
	
	
	@Test
	@DisplayName("Deve retornar TRUE se a data informada for final de semana")
	public void deveRetornarTrueSeFinalSemana() {
		LocalDate data = LocalDate.of(2019, 11, 17);
		Boolean dia = DatasUtils.dataFinalSemana(data);
		assertThat(dia).isTrue();
	}
	
	
	@Test
	@DisplayName("Deve retornar FALSE se a data informada não for final de semana")
	public void deveRetornarFalseSeNaoForFinalSemana() {
		LocalDate data = LocalDate.of(2019, 11, 19);
		Boolean dia = DatasUtils.dataFinalSemana(data);
		assertThat(dia).isFalse();
	}	

	@Test
	@DisplayName("Deve lançar erro quando a data não for informada")
	public void deveLancarErroQuandoNaoInformadoDataFinalSemana() {
		LocalDate data = null;
		
		Throwable result = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.dataFinalSemana(data));

		assertThat(result).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data não foi informada!");		
	}	
	
	
	
	@Test
	@DisplayName("Deve retornar a data com no ultimo dia do mês da data informada")
	public void ultimoDiaMesTest() {
		LocalDate data = LocalDate.of(2020, 4, 18);
		
		LocalDate result = DatasUtils.setarParaUltimoDiaMes(data);
		
		assertThat(result).isNotNull();
		assertThat(result).isAfter(data);
		assertThat(result).isEqualTo(LocalDate.of(2020, 4, 30));
	}	
	

	@Test
	@DisplayName("Deve lançar erro quando a data não for informada para calculo do último dia do mês")
	public void naoInformadoDataParaUltimoDiaMesTest() {
		LocalDate data = null;
		
		Throwable resul = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.setarParaUltimoDiaMes(data));
		
		assertThat(resul).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data não foi informada!");
	}	
	
	
	@Test
	@DisplayName("Deve retornar VERDADEIRO se a data de emissão for menor que a data do vencimento")
	public void dataEmissaoMenorDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 19);
		LocalDate vencimento = LocalDate.of(2019, 11, 20);
		assertThat(DatasUtils.emissaoMenorOuIgualVencimento(emissao, vencimento)).isTrue();
	}

	
	@Test
	@DisplayName("Deve retornar VERDADEIRO se a data de emissão for igual que a data do vencimento")
	public void dataEmissaoIgualDataVencimento() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = LocalDate.of(2019, 11, 20);
		assertThat(DatasUtils.emissaoMenorOuIgualVencimento(emissao, vencimento)).isTrue();
	}
	
	
	@Test
	@DisplayName("Deve lançar erro quando a data de emissão não é informada")
	public void dataEmissaoNaoInformada() {
		LocalDate emissao = null;
		LocalDate vencimento = LocalDate.of(2019, 11, 20);
		
		Throwable result = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.emissaoMenorOuIgualVencimento(emissao, vencimento));
		
		assertThat(result).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data de emissão e/ou vencimento não foi informada!");		
	}
	

	@Test
	@DisplayName("Deve lançar erro quando a data de vencimento não é informada")
	public void dataVencimentoNaoInformada() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = null;
		
		Throwable result = assertThrows(DataNaoInformadaException.class, () -> DatasUtils.emissaoMenorOuIgualVencimento(emissao, vencimento));
		
		assertThat(result).isInstanceOf(DataNaoInformadaException.class).hasMessage("A data de emissão e/ou vencimento não foi informada!");		
	}

	
	@Test
	@DisplayName("Deve lançar erro quando a data de vencimento for MENOR que a data de emissão")
	public void dataVencimentoMenorEmissao() {
		LocalDate emissao = LocalDate.of(2019, 11, 20);
		LocalDate vencimento = LocalDate.of(2019, 11, 19);
		
		Throwable result = assertThrows(VencimentoMenorEmissaoException.class, () -> DatasUtils.emissaoMenorOuIgualVencimento(emissao, vencimento));
		
		assertThat(result).isInstanceOf(VencimentoMenorEmissaoException.class).hasMessage("A data de vencimento não pode ser menor que a data de emissão!");		
	}

	
	
//
//	/*
//	 * diferencaEntreDuasDatas
//	 * 
//	 */		
//	@Test
//	@DisplayName("Deve retornar a quantidade de dias entre duas datas")
//	public void retornarDiasDuasDatas() {
//		LocalDate dataInicio = LocalDate.of(2020, 4, 10);
//		LocalDate dataFinal = LocalDate.of(2020, 4, 20);
//		
//		Long dias = CalculosComDatas.diferencaEntreDuasDatas(dataInicio, dataFinal);
//		
//		org.assertj.core.api.Assertions.assertThat(dias).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(dias).isNotNegative();
//		org.assertj.core.api.Assertions.assertThat(dias).isNotZero();	
//		org.assertj.core.api.Assertions.assertThat(dias).isEqualTo(10);		
//	}
//	
//	@Test
//	@DisplayName("Deve retornar a quantidade de dias zerada pois a data final é menor que a data inicial")
//	public void retornarZeradoDuasDatas() {
//		LocalDate dataInicio = LocalDate.of(2020, 4, 20);
//		LocalDate dataFinal = LocalDate.of(2020, 4, 10);
//		
//		Long dias = CalculosComDatas.diferencaEntreDuasDatas(dataInicio, dataFinal);
//		
//		org.assertj.core.api.Assertions.assertThat(dias).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(dias).isNotNegative();
//		org.assertj.core.api.Assertions.assertThat(dias).isZero();	
//	}
//	
//	@Test
//	@DisplayName("Deve retornar erro, pois não foi informado a data inicial")
//	public void retornarErroDataInicial() {
//		LocalDate dataFinal = LocalDate.of(2020, 4, 10);
//		
//		Assertions.assertThrows(DataNaoInformadaException.class, 
//				() -> CalculosComDatas.diferencaEntreDuasDatas(null, dataFinal));
//	}	
//
//	
//	@Test
//	@DisplayName("Deve retornar erro, pois não foi informado a data final")
//	public void retornarErroDataFinal() {
//		LocalDate dataInicial = LocalDate.of(2020, 4, 10);
//		
//		Assertions.assertThrows(DataNaoInformadaException.class, 
//				() -> CalculosComDatas.diferencaEntreDuasDatas(dataInicial, null));
//	}
//	
//	
//	
//	
//	/*
//	 * setarParaUltimoDiaMes
//	 * 
//	 */	

//
//
//	
//	/*
//	 * setarDataSomandoDiasNoInicioMes
//	 * 
//	 */	
//	@Test
//	@DisplayName("Deve retornar a data somando nenhum dia no inicio do mês")
//	public void dataSomandoZeroDiasInicioMesTest() {
//		LocalDate data = LocalDate.of(2020, 4, 18);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 0, false, false);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 1));
//	}
//	
//	@Test
//	@DisplayName("Deve retornar a data somando 10 dias no inicio do mês e retornando final de semana")
//	public void dataSomando10DiasInicioMesTest() {
//		LocalDate data = LocalDate.of(2020, 4, 18);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, false, false);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 11));
//	}
//
//	@Test
//	@DisplayName("Deve retornar a data somando 40 dias no inicio do mês")
//	public void dataSomando40DiasInicioMesTest() {
//		LocalDate data = LocalDate.of(2020, 4, 18);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 40, false, false);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 5, 11));
//	}
//
//	@Test
//	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no SABADO e indo para segunda")
//	public void dataSomando10CaindoSabadoTest() {
//		LocalDate data = LocalDate.of(2020, 4, 10);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, false);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 13));
//	}
//	
//	@Test
//	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no SABADO e antecipando para sexta")
//	public void dataSomando10CaindoSabadoAntecipandoTest() {
//		LocalDate data = LocalDate.of(2020, 4, 10);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, true);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 10));
//	}
//	
//	@Test
//	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no DOMINGO e indo para segunda")
//	public void dataSomando10CaindoDomingoTest() {
//		LocalDate data = LocalDate.of(2020, 4, 11);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, false);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 13));
//	}
//	
//	@Test
//	@DisplayName("Deve ir para o primeiro dia do mês, somar 10 dias caindo no DOMINGO e antecipando para sexta")
//	public void dataSomando10CaindoDomingoAntecipandoTest() {
//		LocalDate data = LocalDate.of(2020, 4, 11);
//		LocalDate result = CalculosComDatas.setarDataSomandoDiasNoInicioMes(data, 10, true, true);
//		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
//		org.assertj.core.api.Assertions.assertThat(result).isEqualTo(LocalDate.of(2020, 4, 10));
//	}
//	
//
//
//	/*
//	 * dataDomingo
//	 */
//	@Test
//	public void dataDomingo() {
//		LocalDate data = LocalDate.of(2019, 11, 24);
//		boolean dia = CalculosComDatas.dataDomingo(data);
//		assertTrue(dia);
//	}
//
//	
//	/*
//	 * dataSabado
//	 */
//	@Test
//	public void dataSabado() {
//		LocalDate data = LocalDate.of(2019, 11, 23);
//		boolean dia = CalculosComDatas.dataSabado(data);
//		assertTrue(dia);
//	}
//	
//	
//	/*
//	 * numeroDiaSemana - Retorna o numero da semana no mês
//	 */
//	@Test
//	public void dataSemana() {
//		LocalDate data = LocalDate.of(2019, 11, 19);
//		int dia = CalculosComDatas.numeroDiaSemana(data);
//		assertEquals(2, dia);
//	}
//	
//	
//	
//	/*
//	 * dataFinalSemana
//	 */

//	
//
//	@Test
//	@DisplayName("Deve dar ERRO quando a data de emissão é maior que a data de vencimento")
//	public void dataEmissaoMaiorDataVencimento() {
//		LocalDate emissao = LocalDate.of(2019, 11, 20);
//		LocalDate vencimento = LocalDate.of(2019, 11, 19);
//		Assertions.assertThrows(VencimentoMenorEmissaoException.class, 
//				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
//	}
//	
//	@Test
//	@DisplayName("Deve dar ERRO quando a data de emissão e a data de vencimento não é informada")
//	public void dataEmissaoEVencimentoNulo() {
//		LocalDate emissao = null;
//		LocalDate vencimento = null;
//		Assertions.assertThrows(DataNaoInformadaException.class, 
//				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
//	}	
//	
//	@Test
//	@DisplayName("Deve dar ERRO quando a data de emissão não for informada")
//	public void dataEmissaoNula() {
//		LocalDate emissao = null;
//		LocalDate vencimento = LocalDate.of(2019, 11, 19);
//		Assertions.assertThrows(DataNaoInformadaException.class, 
//				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
//	}	
//	
//	@Test
//	@DisplayName("Deve dar ERRO quando a data de emissão não for informada")
//	public void dataVencimentoNula() {
//		LocalDate emissao = LocalDate.of(2019, 11, 19);
//		LocalDate vencimento = null;
//		Assertions.assertThrows(DataNaoInformadaException.class, 
//				() -> CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
//	}	
//
//	@Test
//	@DisplayName("Deve retornar VERDADEIRO quando a data de emissão for igual a data de vencimento")
//	public void dataEmissaoIgualDataVencimento() {
//		LocalDate emissao = LocalDate.of(2019, 11, 19);
//		LocalDate vencimento = LocalDate.of(2019, 11, 19);
//		assertTrue(CalculosComDatas.emissaoMenorIgualVencimento(emissao, vencimento));
//	}
//
//
//	
//	/*
//	 * setarVencimentoXTotalParcelas
//	 */
//	@Test
//	public void setarVencimentoCincoDiasAposEmissaoEUmaParcela() {
//		LocalDate emissao = LocalDate.of(2019, 11, 20);
//		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 1, 5);
//		assertEquals(LocalDate.of(2019, 11, 25), vencimento);
//	}
//
//	@Test
//	public void setarVencimentoCincoDiasAposEmissaoEQuatroParcelas() {
//		LocalDate emissao = LocalDate.of(2019, 11, 20);
//		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 4, 5);
//		assertEquals(LocalDate.of(2019, 12, 10), vencimento);
//	}
//	
//	@Test
//	public void setarVencimentoCincoDiasAposEmissaoENenhumaParcela() {
//		LocalDate emissao = LocalDate.of(2019, 11, 20);
//		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 0, 5);
//		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
//	}	
//
//	@Test
//	public void setarVencimentoNenhumDiaAposEmissaoENenhumaParcela() {
//		LocalDate emissao = LocalDate.of(2019, 11, 20);
//		LocalDate vencimento = CalculosComDatas.setarVencimentoXTotalParcelas(emissao, 0, 0);
//		assertEquals(LocalDate.of(2019, 11, 20), vencimento);
//	}	
	
}
