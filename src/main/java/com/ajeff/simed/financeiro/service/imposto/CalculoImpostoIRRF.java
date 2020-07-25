package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;

public class CalculoImpostoIRRF {



	public static BigDecimal calculo(BigDecimal valor, BigDecimal aliquota, BigDecimal deducao) {
		if( valorIsNotValido(valor) || aliquotaIsNotValido(aliquota)) {
			throw new ValorInformadoInvalidoException("O valor base ou aliquota do imposto inválido!");
		}
		BigDecimal result = valor.multiply( ( aliquota.divide(BigDecimal.valueOf(100)) ));
		result = result.subtract(deducao).setScale(2, RoundingMode.HALF_UP);
		return result.compareTo(BigDecimal.ZERO) ==1 ? result : BigDecimal.ZERO;
	}
	
	
	
	private static boolean valorIsNotValido(BigDecimal valor) {
		return valor == null || valor.compareTo(BigDecimal.ZERO) == 0 
				|| valor.compareTo(BigDecimal.ZERO) == -1 ? true : false;
	}

	private static boolean aliquotaIsNotValido(BigDecimal aliquota) {
		return aliquota == null || aliquota.compareTo(BigDecimal.ZERO) == 0 
				|| aliquota.compareTo(BigDecimal.ZERO) == -1 ? true : false;
	}

}
