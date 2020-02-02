package com.ajeff.simed.financeiro.service.calculos;



import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculoImpostoRendaTest {
	
	@BeforeEach
	public void setUp() {
	}

	@Test
	@DisplayName("Deverá retornar o valor do IRRF retido sem descontos de dependentes e INSS")
	public void calcularIRRFSemDescontos() {
		BigDecimal valor = new BigDecimal(3000.00);
		BigDecimal aliquota = new BigDecimal(7.5);
		BigDecimal deducao = new BigDecimal(189.90);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRRF(valor, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(35.10).setScale(2, RoundingMode.HALF_UP), imposto);
	}
	
	@Test
	@DisplayName("Deverá retornar o valor do IRRF zerado, pois o valor da dedução é maior que o imposto")
	public void calcularIRRFSemDescontosComDeducaoMaiorValor() {
		BigDecimal valor = new BigDecimal(2000.00);
		BigDecimal aliquota = new BigDecimal(7.5);
		BigDecimal deducao = new BigDecimal(189.90);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRRF(valor, aliquota, deducao);
		Assertions.assertEquals(BigDecimal.ZERO, imposto);
	}	
	
	
	@Test
	@DisplayName("Deverá retornar o valor do IRRF retido com o desconto de um dependente")
	public void calcularIRRFDescontandoDependente() {
		BigDecimal valor = new BigDecimal(3000.00);
		BigDecimal aliquota = new BigDecimal(7.5);
		BigDecimal deducao = new BigDecimal(189.90);
		BigDecimal valorDependente = CalculoImpostoRenda.calculoTotalDescontoDependente(1, deducao);
		BigDecimal valorBase = valor.subtract(valorDependente).setScale(2, RoundingMode.HALF_UP);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRRF(valorBase, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(20.86).setScale(2, RoundingMode.HALF_UP), imposto);
	}
	
		
	@Test
	@DisplayName("Deverá retornar o valor do IRRF retido com o desconto do INSS")
	public void calcularIRRFDescontandoINSS() {
		BigDecimal valor = new BigDecimal(3000.00);
		BigDecimal aliquota = new BigDecimal(7.5);
		BigDecimal deducao = new BigDecimal(189.90);
		BigDecimal inss = CalculoINSS.calculoINSS(valor, new BigDecimal(6000), new BigDecimal(11));
		BigDecimal valorBase = valor.subtract(inss).setScale(2, RoundingMode.HALF_UP);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRRF(valorBase, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(10.35).setScale(2, RoundingMode.HALF_UP), imposto);
	}	
	
	@Test
	@DisplayName("Deverá retornar o valor do IRRF retido com o desconto do INSS e um dependente")
	public void calcularIRRFDescontandoINSSEDependente() {
		BigDecimal valor = new BigDecimal(5000.00);
		BigDecimal aliquota = new BigDecimal(7.5);
		BigDecimal deducao = new BigDecimal(189.90);
		BigDecimal inss = CalculoINSS.calculoINSS(valor, new BigDecimal(6000), new BigDecimal(11));
		BigDecimal valorDependente = CalculoImpostoRenda.calculoTotalDescontoDependente(1, deducao);
		BigDecimal valorBase = valor.subtract(inss).subtract(valorDependente).setScale(2, RoundingMode.HALF_UP);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRRF(valorBase, aliquota, deducao);
		Assertions.assertEquals(new BigDecimal(129.61).setScale(2, RoundingMode.HALF_UP), imposto);
	}
	
	@Test
	@DisplayName("Deverá retornar o valor do IRPJ retido")
	public void calcularIRPJ() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquotaIR = new BigDecimal(1.5);
		BigDecimal imposto = CalculoImpostoRenda.calculoIRPJ(valor, aliquotaIR);
		Assertions.assertEquals(new BigDecimal(15).setScale(2, RoundingMode.HALF_UP), imposto);
	}
	
	
	@Test
	@DisplayName("Deverá retornar o valor do PIS/COFINS/CSLL retido")
	public void calcularPCCS() {
		BigDecimal valor = new BigDecimal(1000.00);
		BigDecimal aliquotaPIS = new BigDecimal(0.65);
		BigDecimal aliquotaCOFINS = new BigDecimal(3);
		BigDecimal aliquotaCSLL = new BigDecimal(1);
		BigDecimal imposto = CalculoImpostoRenda.calculoPCCS(valor, aliquotaPIS, aliquotaCOFINS, aliquotaCSLL);
		Assertions.assertEquals(new BigDecimal(46.50).setScale(2, RoundingMode.HALF_UP), imposto);
	}
	


}
