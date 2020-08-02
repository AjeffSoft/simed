package com.ajeff.simed.financeiro.service.imposto;



import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculoImpostoPCCSTest {
	
	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("Deverá calcular valor PCCS")
	public void calculoValorPCCS() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquota = new BigDecimal(4.65);
		BigDecimal result = CalculoImpostoPCCS.calculo(valor, aliquota);
		Assertions.assertEquals(new BigDecimal(46.50).setScale(2, RoundingMode.HALF_UP), result);
	}

	@Test
	@DisplayName("Deverá retornar zerado quando o valor informado for zero PCCS")
	public void calculoValorZeradoPCCS() {
		BigDecimal valor = new BigDecimal(00);
		BigDecimal aliquota = new BigDecimal(4.65);
		BigDecimal result = CalculoImpostoPCCS.calculo(valor, aliquota);
		Assertions.assertEquals(new BigDecimal(0), result);
	}

}
