package com.ajeff.simed.financeiro.service.calculos;

import java.math.BigDecimal;

public class CalculosImpostoRendaPF {
	
	private static final BigDecimal ALIQUOTA_PRIMEIRA_FAIXA_IRPF = new BigDecimal(7.5);
	private static final BigDecimal DEDUCAO_PRIMEIRA_FAIXA_IRPF = new BigDecimal(142.80);
	private static final BigDecimal ALIQUOTA_SEGUNDA_FAIXA_IRPF = new BigDecimal(15);
	private static final BigDecimal DEDUCAO_SEGUNDA_FAIXA_IRPF = new BigDecimal(354.80);
	private static final BigDecimal ALIQUOTA_TERCEIRA_FAIXA_IRPF = new BigDecimal(22.5);
	private static final BigDecimal DEDUCAO_TERCEIRA_FAIXA_IRPF = new BigDecimal(636.13);
	private static final BigDecimal ALIQUOTA_QUARTA_FAIXA_IRPF = new BigDecimal(27.5);
	private static final BigDecimal DEDUCAO_QUARTA_FAIXA_IRPF = new BigDecimal(869.36);


	public static BigDecimal calculoImpostoRendaFaixa1(BigDecimal valorParaDesconto) {
		return (valorParaDesconto.multiply(ALIQUOTA_PRIMEIRA_FAIXA_IRPF)).divide(new BigDecimal(100)).subtract(DEDUCAO_PRIMEIRA_FAIXA_IRPF);
	}
	
	public static BigDecimal calculoImpostoRendaFaixa2(BigDecimal valorParaDesconto) {
		return (valorParaDesconto.multiply(ALIQUOTA_SEGUNDA_FAIXA_IRPF)).divide(new BigDecimal(100)).subtract(DEDUCAO_SEGUNDA_FAIXA_IRPF);
	}
	
	public static BigDecimal calculoImpostoRendaFaixa3(BigDecimal valorParaDesconto) {
		return (valorParaDesconto.multiply(ALIQUOTA_TERCEIRA_FAIXA_IRPF)).divide(new BigDecimal(100)).subtract(DEDUCAO_TERCEIRA_FAIXA_IRPF);
	}	

	public static BigDecimal calculoImpostoRendaFaixa4(BigDecimal valorParaDesconto) {
		return (valorParaDesconto.multiply(ALIQUOTA_QUARTA_FAIXA_IRPF)).divide(new BigDecimal(100)).subtract(DEDUCAO_QUARTA_FAIXA_IRPF);
	}	
	
}
