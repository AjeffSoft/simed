package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoISS {
	

	public static BigDecimal calculo(BigDecimal valor, BigDecimal issPorcentagem) {

		if(valor.equals(null)) {
			throw new NullPointerException();
		}

		if(valor.compareTo(BigDecimal.ZERO) == 1) {
			return valor.multiply(issPorcentagem).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		}else {
			return BigDecimal.ZERO;
		}
	}

}
