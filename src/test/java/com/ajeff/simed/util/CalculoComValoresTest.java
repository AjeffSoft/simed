package com.ajeff.simed.util;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CalculoComValoresTest {


	@Test
	@DisplayName("Deve lançar erro quando a parcela for nula")
	public void totalComParcelaNula() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = null;
		
		org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, 
				() -> CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela));
	}	
	
	
	
	/*
	 * TESTES COM PARCELA ZERADA
	 */	
	
	@Test
	@DisplayName("Deve retornar um total com a parcela zerada, sem acrescimo e desconto")
	public void totalComParcelaZerada() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1000));
	}
	
	@Test
	@DisplayName("Deve retornar um total com a parcela zerada, com acrescimo e sem desconto")
	public void totalComParcelaZeradaComAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(500);
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1500));
	}	
	
	
	@Test
	@DisplayName("Deve retornar um total com a parcela zerada, sem acrescimo e com desconto")
	public void totalComParcelaZeradaComDesconto() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.valueOf(500);
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(500));
	}
	
	
	@Test
	@DisplayName("Deve retornar um total com a parcela zerada, com acrescimo e com desconto")
	public void totalComParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(200);
		BigDecimal desconto = BigDecimal.valueOf(500);
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(700));
	}
	
	
	@Test
	@DisplayName("Deve retornar zero quando total negativo com a parcela zerada, com acrescimo e com desconto")
	public void totalNegativoComParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(200);
		BigDecimal desconto = BigDecimal.valueOf(1500);
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotPositive();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.ZERO);
	}
	
	
	@Test
	@DisplayName("Deve retornar um total zerado com a parcela zerada, com acrescimo e com desconto")
	public void totalZeradoComParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(500);
		BigDecimal desconto = BigDecimal.valueOf(1500);
		Integer parcela = 0;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.ZERO);
	}
	
	
	/*
	 * TESTES COM 1 PARCELA
	 */
	
	@Test
	@DisplayName("Deve retornar um total com UMA parcela, sem acrescimo e desconto")
	public void totalComUmaParcelaZerada() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1000));
	}
	
	@Test
	@DisplayName("Deve retornar um total com UMA parcela, com acrescimo e sem desconto")
	public void totalComUmaParcelaZeradaComAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(500);
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1500));
	}	
	
	
	@Test
	@DisplayName("Deve retornar um total com UMA parcela, sem acrescimo e com desconto")
	public void totalComUmaParcelaZeradaComDesconto() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.valueOf(500);
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(500));
	}
	
	
	@Test
	@DisplayName("Deve retornar um total com UMA parcela, com acrescimo e com desconto")
	public void totalComUmaParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(200);
		BigDecimal desconto = BigDecimal.valueOf(500);
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(700));
	}
	
	
	@Test
	@DisplayName("Deve retornar zero quando total negativo com UMA parcela, com acrescimo e com desconto")
	public void totalNegativoComUmaParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(200);
		BigDecimal desconto = BigDecimal.valueOf(1500);
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotPositive();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.ZERO);
	}
	
	
	@Test
	@DisplayName("Deve retornar um total zerado com UMA parcela, com acrescimo e com desconto")
	public void totalZeradoComUmaParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(500);
		BigDecimal desconto = BigDecimal.valueOf(1500);
		Integer parcela = 1;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.ZERO);
	}
	

	/*
	 * TESTES COM 3 PARCELAS
	 */
	
	@Test
	@DisplayName("Deve retornar um total com TRÊS parcelas, sem acrescimo e desconto")
	public void totalComTresParcelaZerada() {
		BigDecimal base = BigDecimal.valueOf(1500);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 3;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(500));
	}
	
	@Test
	@DisplayName("Deve retornar um total com TRÊS parcelas, com acrescimo e sem desconto")
	public void totalComTresParcelaZeradaComAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(2000);
		BigDecimal desconto = BigDecimal.ZERO;
		Integer parcela = 3;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1000));
	}	
	
	
	@Test
	@DisplayName("Deve retornar um total com TRÊS parcelas, sem acrescimo e com desconto")
	public void totalComTresParcelaZeradaComDesconto() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.ZERO;
		BigDecimal desconto = BigDecimal.valueOf(400);
		Integer parcela = 3;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(200));
	}
	
	
	@Test
	@DisplayName("Deve retornar um total com TRÊS parcelas, com acrescimo e com desconto")
	public void totalComTresParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(2100);
		BigDecimal acrescimo = BigDecimal.valueOf(1000);
		BigDecimal desconto = BigDecimal.valueOf(100);
		Integer parcela = 3;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotNegative();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isNotZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.valueOf(1000));
	}
	
	
	@Test
	@DisplayName("Deve retornar zero quando total negativo com TRÊS parcelas, com acrescimo e com desconto")
	public void totalNegativoComTresParcelaZeradaComDescontoEAcrescimo() {
		BigDecimal base = BigDecimal.valueOf(1000);
		BigDecimal acrescimo = BigDecimal.valueOf(200);
		BigDecimal desconto = BigDecimal.valueOf(1500);
		Integer parcela = 3;
		
		BigDecimal result = CalculosComValores.setarValorTotalPositivo(base, desconto, acrescimo, parcela);
		
		Assertions.assertThat(result).isNotPositive();
		Assertions.assertThat(result).isNotNull();
		Assertions.assertThat(result).isZero();
		Assertions.assertThat(result).isEqualTo(BigDecimal.ZERO);
	}

	
}
