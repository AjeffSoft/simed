package com.ajeff.simed.financeiro.service.imposto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;

public class CalculoImpostoIRRFTest {
	
	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("Deverá calcular valor IRPF com deducao")
	public void calculoIRPFComDeducao() {
		BigDecimal valor = new BigDecimal(4000.00);
		BigDecimal aliquota = new BigDecimal(7.50);
		BigDecimal deducao = new BigDecimal(200.00);
		BigDecimal result = CalculoImpostoIRRF.calculo(valor, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(100).setScale(2, RoundingMode.HALF_UP), result);
	}

	@Test
	@DisplayName("Deverá retornar ZERO quando calcular valor IRPF apos deducao o valor ficar negativo")
	public void deveRetonarZeradoCalculoIRPFComValorNegativo() {
		BigDecimal valor = new BigDecimal(300.00);
		BigDecimal aliquota = new BigDecimal(7.50);
		BigDecimal deducao = new BigDecimal(200.00);
		BigDecimal result = CalculoImpostoIRRF.calculo(valor, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(0), result);
	}

	@Test
	@DisplayName("Deverá calcular valor IRPJ")
	public void calculoIRPJ() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquota = new BigDecimal(1.50);
		BigDecimal deducao = new BigDecimal(0.00);
		BigDecimal result = CalculoImpostoIRRF.calculo(valor, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(15).setScale(2, RoundingMode.HALF_UP), result);
	}

	@Test
	@DisplayName("Deverá retornar erro quando não informado o valor para calculo")
	public void deveRetornarErroValorNulo() {
		BigDecimal valor = null;
		BigDecimal aliquota = new BigDecimal(1.50);
		BigDecimal deducao = new BigDecimal(0.00);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota, deducao));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando o valor informado for zerado para calculo")
	public void deveRetornarErroValorZerado() {
		BigDecimal valor = BigDecimal.ZERO;
		BigDecimal aliquota = new BigDecimal(1.50);
		BigDecimal deducao = new BigDecimal(0.00);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota, deducao));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando a aliquota informada for zerada para calculo")
	public void deveRetornarErroAliquotaZerado() {
		BigDecimal valor = new BigDecimal(1000.50);
		BigDecimal aliquota = BigDecimal.ZERO;
		BigDecimal deducao = new BigDecimal(0.00);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota, deducao));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}
	
	@Test
	@DisplayName("Deverá retornar erro quando não informado a aliquota para calculo")
	public void deveRetornarErroAliquotaNula() {
		BigDecimal valor = new BigDecimal(1000.50);
		BigDecimal aliquota = null;
		BigDecimal deducao = new BigDecimal(0.00);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota, deducao));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}
	
	
	@Test
	@DisplayName("Deverá retornar o valor de um dependente")
	public void valorUmDependente() {
		BigDecimal result = CalculoImpostoIRRF.descontDependente(1, true);
		Assertions.assertEquals(new BigDecimal(189.59).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de dois dependentes")
	public void valorDoisDependentes() {
		BigDecimal result = CalculoImpostoIRRF.descontDependente(2, true);
		Assertions.assertEquals(new BigDecimal(379.18).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de zero dependentes")
	public void valorZeroDependentes() {
		BigDecimal result = CalculoImpostoIRRF.descontDependente(0, true);
		Assertions.assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de zero quando o dependente for nulo")
	public void valorZeroDependentesNulo() {
		BigDecimal result = CalculoImpostoIRRF.descontDependente(2, false);
		Assertions.assertEquals(new BigDecimal(0.00), result);
	}	
}
