package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoINSS {
	
	private static final BigDecimal TETO_INSS = new BigDecimal(6101.06);
	private static final BigDecimal ALIQUOTA_INSS = new BigDecimal(0.11);	
	


	public static BigDecimal calculo(BigDecimal valor) {
		BigDecimal result = BigDecimal.ZERO;
		if(valor.compareTo(TETO_INSS) == 1) {
			result = valorMaximoRetencao();
		}else {
			result = valor.multiply(ALIQUOTA_INSS).setScale(2, RoundingMode.HALF_UP);
		}
		return result;
	}
	
	
	private static BigDecimal valorMaximoRetencao() {
		return TETO_INSS.multiply(ALIQUOTA_INSS).setScale(2, BigDecimal.ROUND_DOWN);
	}
	

}
