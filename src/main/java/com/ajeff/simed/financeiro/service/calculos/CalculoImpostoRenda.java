package com.ajeff.simed.financeiro.service.calculos;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculoImpostoRenda {
	
	/*
	 * Metodo que retorna o valor da retenção do IRRF (PESSOA FISICA), caso valor seja negativo, será retornado zero
	 */
	public static BigDecimal calculoIRRF (BigDecimal valor, BigDecimal aliquota, BigDecimal deducao) {
		BigDecimal imposto = valor.multiply(aliquota).divide(new BigDecimal(100)).subtract(deducao).setScale(2, RoundingMode.HALF_UP);
		return imposto.compareTo(BigDecimal.ZERO) == 1 ? imposto : BigDecimal.ZERO;
	}
	
	
	/*
	 * Metodo que retorna o valor da retenção do IRPJ (PESSOA JURIDICA)
	 */
	public static BigDecimal calculoIRPJ (BigDecimal valor, BigDecimal aliquotaIR) {
		BigDecimal imposto = valor.multiply(aliquotaIR).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		return imposto.compareTo(BigDecimal.ZERO) == 1 ? imposto : BigDecimal.ZERO;
	}	

	
	/*
	 * Metodo que retorna o valor da retenção do PIS/COFINS/CSLL (PESSOA JURIDICA)
	 */
	public static BigDecimal calculoPCCS (BigDecimal valor, BigDecimal aliquotaPIS, BigDecimal aliquotaCOFINS, BigDecimal aliquotaCSLL) {
		BigDecimal somaAliquotas = new BigDecimal(0);
		somaAliquotas = somaAliquotas.add(aliquotaPIS).add(aliquotaCOFINS).add(aliquotaCSLL);
		BigDecimal imposto = valor.multiply(somaAliquotas).divide(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP);
		return imposto.compareTo(BigDecimal.ZERO) == 1 ? imposto : BigDecimal.ZERO;
	}		
	
	
	/*
	 * Retorna o total do desconto definido pela quantidade de dependentes
	 */
	public static BigDecimal calculoTotalDescontoDependente(Integer dependente, BigDecimal depDesconto){
		if(dependente.equals(null)) {
			throw new NullPointerException("O fornecedor possui o campo dependente nulo. Favor preencher");
		}else {
			return depDesconto.multiply(new BigDecimal(dependente).setScale(2, RoundingMode.HALF_UP));

		}
	}


}
