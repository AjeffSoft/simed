package com.ajeff.simed.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.junit.Before;
import org.junit.Test;

import com.ajeff.simed.exceptions.ValorInformadoNegativoException;

public class CalculosImpostosTest {
	
	@Before
	public void setUp() {
	}

	//====================== TESTES PARA CALCULOS DE ISS =============================
	@Test
	public void testCalculoISS() {
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(1000), new BigDecimal(5));
		assertEquals(new BigDecimal(50).setScale(3, RoundingMode.HALF_UP), iss);
	}
	
	@Test
	public void testCalculoISSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal("-1000"), new BigDecimal("5"));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSTaxaNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal("1000"), new BigDecimal("-5"));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSAmbosValorTaxaNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal("-1000"), new BigDecimal("-5"));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSValorZerado(){
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal("0"), new BigDecimal("5"));
		assertEquals(BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP), iss);
	}
	
	@Test
	public void testCalculoISSTaxaZerado(){
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal("1000"), new BigDecimal("0"));
		assertEquals(BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP), iss);
	}
	
	

	//====================== TESTES PARA CALCULOS DE INSS =============================
	
	@Test
	public void testCalculoINSS() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(1523.44));
		assertEquals(new BigDecimal(167.5784).setScale(3, RoundingMode.HALF_UP), inss);
	}
	
	@Test
	public void testCalculoINSSValorAcimaTetoMaximo() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(5839.46).setScale(3, RoundingMode.HALF_UP));
		assertEquals(BigDecimal.ZERO, inss);
	}	
	
	@Test
	public void testCalculoINSSValorIgualTetoMaximo() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(5839.45).setScale(3, RoundingMode.HALF_UP));
		assertEquals(new BigDecimal(642.34).setScale(3, RoundingMode.HALF_UP), inss);
	}
	
	@Test
	public void testCalculoINSSValorZerado() {
		BigDecimal inss = CalculosImpostos.calculoINSS(BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP));
		assertEquals(BigDecimal.ZERO.setScale(3, RoundingMode.HALF_UP), inss);
	}		
	
	@Test
	public void testCalculoINSSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoINSS(new BigDecimal(-1000)).setScale(3, RoundingMode.HALF_UP);
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("O valor principal esta negativo", e.getMessage());
		}
	}
	
	
	//====================== TESTES PARA CALCULOS DE INSS =============================
	
	@Test
	public void testCalculoPCCS() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(1000));
		assertEquals(new BigDecimal(46.50).setScale(3, RoundingMode.HALF_UP), pccs);
	}
	
	@Test
	public void testCalculoPCCSMenorPisoMinimo() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(214));
		assertEquals(BigDecimal.ZERO, pccs);
	}
	
	@Test
	public void testCalculoPCCSIgualPisoMinimo() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(215.1).setScale(3, RoundingMode.HALF_UP));
		assertEquals(new BigDecimal(10.002).setScale(3, RoundingMode.HALF_UP), pccs);
	}
	
	@Test
	public void testCalculoPCCSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(-1000)).setScale(3, RoundingMode.HALF_UP);
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("O valor principal esta negativo", e.getMessage());
		}
	}	
}
