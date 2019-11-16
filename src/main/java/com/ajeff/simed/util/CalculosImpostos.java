package com.ajeff.simed.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.ajeff.simed.exceptions.ValorInformadoNegativoException;
import com.ajeff.simed.financeiro.model.ContaPagar;

public class CalculosImpostos {
	
	private static final BigDecimal TETO_MAXIMO_INSS = new BigDecimal("642.34");
	private static final BigDecimal TAXA_DESCONTO_INSS = new BigDecimal("11");
	private static final BigDecimal TAXA_DESCONTO_PCCS = new BigDecimal("4.65");
	private static final BigDecimal PISO_MINIMO_PCCS = new BigDecimal("10.0");
	
	
	
	public static BigDecimal calculoISS(BigDecimal valor, BigDecimal taxa) {
		if (valor.compareTo(BigDecimal.ZERO) == -1 || taxa.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("Um dos valores informados é inválido");
		}else {
			return valor.multiply(taxa).divide(new BigDecimal(100), MathContext.DECIMAL32).setScale(3, RoundingMode.HALF_UP);
		}
	}
	
	
	public static BigDecimal calculoINSS(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("O valor principal esta negativo");
		}
		BigDecimal inss = (valor.multiply(TAXA_DESCONTO_INSS).divide(new BigDecimal(100))).setScale(3, RoundingMode.HALF_UP);
		return ((inss.compareTo(TETO_MAXIMO_INSS) == -1) || (inss.compareTo(TETO_MAXIMO_INSS) == 0)? inss : BigDecimal.ZERO);
	}
	
	public static BigDecimal calculoPCCS(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("O valor principal esta negativo");
		}
		BigDecimal pccs = (valor.multiply(TAXA_DESCONTO_PCCS).divide(new BigDecimal(100))).setScale(3, RoundingMode.HALF_UP);
		return ((pccs.compareTo(PISO_MINIMO_PCCS) == 1) || (pccs.compareTo(PISO_MINIMO_PCCS) == 0)? pccs : BigDecimal.ZERO);
		
	}
}
