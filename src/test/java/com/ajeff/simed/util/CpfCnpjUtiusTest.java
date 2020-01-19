package com.ajeff.simed.util;



import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CpfCnpjUtiusTest {
	
	@Test
	public void deveRetornarCPFValido() {
		Boolean result = CpfCnpjUtils.isValid("97996599300");
		assertTrue(result);
	}
	
	
	@Test
	public void deveRetornarCPFInValido() {
		Boolean result = CpfCnpjUtils.isValid("11111111111");
		assertFalse(result);
	}
	
	@Test
	public void deveRetornarCNPJValido() {
		Boolean result = CpfCnpjUtils.isValid("11685526000179");
		assertTrue(result);
	}
	
	
	@Test
	public void deveRetornarCNPJInValido() {
		Boolean result = CpfCnpjUtils.isValid("11111111000179");
		assertFalse(result);
	}	


}
