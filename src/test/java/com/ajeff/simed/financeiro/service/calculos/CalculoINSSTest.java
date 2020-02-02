package com.ajeff.simed.financeiro.service.calculos;



import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculoINSSTest {
	
	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("Deverá calcular valor INSS abaixo do Teto máximo")
	public void calculoINSSAbaixoTeto() {
		BigDecimal valor = new BigDecimal(4000.00);
		BigDecimal tetoINSS = new BigDecimal(6000.00);
		BigDecimal aliquota = new BigDecimal(11);
		BigDecimal inss = CalculoINSS.calculoINSS(valor, tetoINSS, aliquota);
		Assertions.assertEquals(new BigDecimal(440).setScale(2, RoundingMode.HALF_UP), inss);
	}

	@Test
	@DisplayName("Deverá calcular valor INSS acima do Teto máximo")
	public void calculoINSSAcimaTeto() {
		BigDecimal valor = new BigDecimal(7000.00);
		BigDecimal tetoINSS = new BigDecimal(6000.00);
		BigDecimal aliquota = new BigDecimal(11);
		BigDecimal inss = CalculoINSS.calculoINSS(valor, tetoINSS, aliquota);
		Assertions.assertEquals(new BigDecimal(660).setScale(2, RoundingMode.HALF_UP), inss);
	}
	
	@Test
	@DisplayName("Deverá calcular valor INSS igual ao Teto máximo")
	public void calculoINSSIgualTeto() {
		BigDecimal valor = new BigDecimal(6000.00);
		BigDecimal tetoINSS = new BigDecimal(6000.00);
		BigDecimal aliquota = new BigDecimal(11);
		BigDecimal inss = CalculoINSS.calculoINSS(valor, tetoINSS, aliquota);
		Assertions.assertEquals(new BigDecimal(660).setScale(2, RoundingMode.HALF_UP), inss);
	}	
	
	@Test
	@DisplayName("Deverá retornar o valor liquido já descontado o INSS. Valor abaixo do teto")
	public void calculoINSSAbaixoTetoRetornaLiquido() {
		BigDecimal valor = new BigDecimal(4000.00);
		BigDecimal tetoINSS = new BigDecimal(6000.00);
		BigDecimal aliquota = new BigDecimal(11);
		BigDecimal liquido = CalculoINSS.descontarINSSNoValor(valor, tetoINSS, aliquota);
		Assertions.assertEquals(new BigDecimal(3560).setScale(2, RoundingMode.HALF_UP), liquido);
	}		
	
	@Test
	@DisplayName("Deverá retornar o valor liquido já descontado o INSS. Valor acima do teto")
	public void calculoINSSAcimaTetoRetornaLiquido() {
		BigDecimal valor = new BigDecimal(7000.00);
		BigDecimal tetoINSS = new BigDecimal(6000.00);
		BigDecimal aliquota = new BigDecimal(11);
		BigDecimal liquido = CalculoINSS.descontarINSSNoValor(valor, tetoINSS, aliquota);
		Assertions.assertEquals(new BigDecimal(6340).setScale(2, RoundingMode.HALF_UP), liquido);
	}
}
