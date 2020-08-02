package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;

public class CalculoImpostoIRRF {
	
	private static final BigDecimal DESCONTO_DEPENDENTE = BigDecimal.valueOf(189.59);



	public static BigDecimal calculo(BigDecimal valor, BigDecimal aliquota) {
		if( valorIsNotValido(valor) || aliquotaIsNotValido(aliquota)) {
			throw new ValorInformadoInvalidoException("O valor base ou aliquota do imposto inválido!");
		}
		BigDecimal result = valor.multiply( ( aliquota.divide(BigDecimal.valueOf(100)) )).setScale(2, RoundingMode.HALF_UP);
		return result;
	}	
	
	
	private static boolean valorIsNotValido(BigDecimal valor) {
		return valor == null || valor.compareTo(BigDecimal.ZERO) == 0 
				|| valor.compareTo(BigDecimal.ZERO) == -1 ? true : false;
	}

	private static boolean aliquotaIsNotValido(BigDecimal aliquota) {
		return aliquota == null || aliquota.compareTo(BigDecimal.ZERO) == 0 
				|| aliquota.compareTo(BigDecimal.ZERO) == -1 ? true : false;
	}



	public static BigDecimal descontoDependente(Integer dependente, Boolean temDependente) {
		if(temDependente) {
			BigDecimal depend = BigDecimal.valueOf(dependente);
			return DESCONTO_DEPENDENTE.multiply(depend);
		}else {
			return BigDecimal.ZERO;
		}
	}

}
