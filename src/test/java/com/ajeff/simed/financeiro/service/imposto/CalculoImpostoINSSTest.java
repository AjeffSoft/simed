package com.ajeff.simed.financeiro.service.imposto;



import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculoImpostoINSSTest {
	
	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("Deverá calcular valor INSS abaixo do Teto máximo")
	public void calculoINSSAbaixoTeto() {
		BigDecimal valor = new BigDecimal(4000.00);
		BigDecimal inss = CalculoImpostoINSS.calculo(valor);
		Assertions.assertEquals(new BigDecimal(440).setScale(2, RoundingMode.HALF_UP), inss);
	}

	@Test
	@DisplayName("Deverá calcular valor INSS acima do Teto máximo")
	public void calculoINSSAcimaTeto() {
		BigDecimal valor = new BigDecimal(7000.00);
		BigDecimal inss = CalculoImpostoINSS.calculo(valor);
		Assertions.assertEquals(new BigDecimal(671.11).setScale(2, BigDecimal.ROUND_DOWN), inss);
	}
	
}
