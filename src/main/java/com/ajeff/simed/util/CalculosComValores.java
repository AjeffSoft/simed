package com.ajeff.simed.util;

import java.math.BigDecimal;

public class CalculosComValores {
	
	
	
	public static BigDecimal setarValorTotalPositivo(BigDecimal base, BigDecimal desconto, BigDecimal acrescimo, Integer parcela) {
		BigDecimal result = BigDecimal.ZERO;
		if (parcela.equals(null)) {
			throw new NullPointerException("O valor da parcela não pode ser nula!");
		}
		
		if (parcela == 0) {
			result =  base.subtract(desconto).add(acrescimo);
		}else {
			
			result = (base.add(acrescimo).subtract(desconto)).divide(new BigDecimal (parcela));
		}
		return result.compareTo(BigDecimal.ZERO) == 1 ? result : BigDecimal.ZERO;
	}
}
