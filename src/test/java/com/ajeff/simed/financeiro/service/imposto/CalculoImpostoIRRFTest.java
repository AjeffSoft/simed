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
	@DisplayName("Deverá calcular valor IRPF")
	public void calculoIRPFComDeducao() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquota = new BigDecimal(7.50);
		BigDecimal result = CalculoImpostoIRRF.calculo(valor, aliquota);
		Assertions.assertEquals(new BigDecimal(75).setScale(2, RoundingMode.HALF_UP), result);
	}


	@Test
	@DisplayName("Deverá calcular valor IRPJ")
	public void calculoIRPJ() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquota = new BigDecimal(1.50);
		BigDecimal result = CalculoImpostoIRRF.calculo(valor, aliquota);
		Assertions.assertEquals(new BigDecimal(15).setScale(2, RoundingMode.HALF_UP), result);
	}

	@Test
	@DisplayName("Deverá retornar erro quando não informado o valor para calculo")
	public void deveRetornarErroValorNulo() {
		BigDecimal valor = null;
		BigDecimal aliquota = new BigDecimal(1.50);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando o valor informado for zerado para calculo")
	public void deveRetornarErroValorZerado() {
		BigDecimal valor = BigDecimal.ZERO;
		BigDecimal aliquota = new BigDecimal(1.50);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota ));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando o valor informado for menor que zero para calculo")
	public void deveRetornarErroValorNegativo() {
		BigDecimal valor = new BigDecimal(-150);
		BigDecimal aliquota = new BigDecimal(1.50);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota ));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando a aliquota informada for negativo")
	public void deveRetornarErroAliquotaZerado() {
		BigDecimal valor = new BigDecimal(1000.50);
		BigDecimal aliquota = BigDecimal.ZERO;
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}

	@Test
	@DisplayName("Deverá retornar erro quando a aliquota informada for negativo")
	public void deveRetornarErroAliquotaNegativo() {
		BigDecimal valor = new BigDecimal(1000.50);
		BigDecimal aliquota = new BigDecimal(-1);
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}
	
	@Test
	@DisplayName("Deverá retornar erro quando não informado a aliquota para calculo")
	public void deveRetornarErroAliquotaNula() {
		BigDecimal valor = new BigDecimal(1000.50);
		BigDecimal aliquota = null;
		
		Throwable result = assertThrows(ValorInformadoInvalidoException.class, () -> CalculoImpostoIRRF.calculo(valor, aliquota));
		
		assertThat(result).isInstanceOf(ValorInformadoInvalidoException.class).hasMessage("O valor base ou aliquota do imposto inválido!");
	}
	
	
	@Test
	@DisplayName("Deverá retornar o valor de um dependente")
	public void valorUmDependente() {
		BigDecimal result = CalculoImpostoIRRF.descontoDependente(1, true);
		Assertions.assertEquals(new BigDecimal(189.59).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de dois dependentes")
	public void valorDoisDependentes() {
		BigDecimal result = CalculoImpostoIRRF.descontoDependente(2, true);
		Assertions.assertEquals(new BigDecimal(379.18).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de zero dependentes")
	public void valorZeroDependentes() {
		BigDecimal result = CalculoImpostoIRRF.descontoDependente(0, true);
		Assertions.assertEquals(new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP), result);
	}	

	@Test
	@DisplayName("Deverá retornar o valor de zero quando o dependente for nulo")
	public void valorZeroDependentesNulo() {
		BigDecimal result = CalculoImpostoIRRF.descontoDependente(2, false);
		Assertions.assertEquals(new BigDecimal(0.00), result);
	}	
}
