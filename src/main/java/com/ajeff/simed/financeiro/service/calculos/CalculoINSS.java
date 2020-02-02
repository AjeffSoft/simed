package com.ajeff.simed.financeiro.service.calculos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoINSS {
	

	/*
	 * Calculo e retorno do imposto INSS com o valor acima e abaixo do teto
	 * Quando acima: retorna um valor fixo (teto * aliquota /100)
	 */
	
	public static BigDecimal calculoINSS(BigDecimal valor, BigDecimal tetoINSS, BigDecimal aliquota) {		
		if(valor.compareTo(tetoINSS) == 1) {
			return tetoINSS.multiply(aliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		}else {
			return valor.multiply(aliquota).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		}
	}
	
	
	/*
	 * Método que retorna o valor já descontado o imposto INSS
	 */
	public static BigDecimal descontarINSSNoValor(BigDecimal valor, BigDecimal tetoINSS, BigDecimal aliquota) {		
		BigDecimal inss = calculoINSS(valor, tetoINSS, aliquota);
		return valor.subtract(inss); 
	}	

}
