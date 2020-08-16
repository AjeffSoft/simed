package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoPCCS {


	public static BigDecimal calculo(BigDecimal valor, BigDecimal aliquota, String tipo) {
		BigDecimal result = BigDecimal.ZERO;
				
		if(tipo.equals("F")) {
			return result;
		}else {
			result = valor.multiply( aliquota.divide(BigDecimal.valueOf(100)) ).setScale(2, RoundingMode.HALF_UP);
			return result.compareTo(BigDecimal.ZERO) == 1 ? result : BigDecimal.ZERO;
		}
	}
	

}
