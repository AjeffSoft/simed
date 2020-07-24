package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoPCCS {


	public static BigDecimal calculo(BigDecimal valor, BigDecimal aliquota) {
		return valor.multiply(aliquota).setScale(2, RoundingMode.HALF_UP);
	}
	

}
