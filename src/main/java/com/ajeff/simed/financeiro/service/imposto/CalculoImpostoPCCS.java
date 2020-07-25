package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoPCCS {


	public static BigDecimal calculo(BigDecimal valor, BigDecimal aliquota) {
		BigDecimal result = aliquota.divide(BigDecimal.valueOf(100));
		return valor.multiply(result).setScale(2, RoundingMode.HALF_UP);
	}
	

}
