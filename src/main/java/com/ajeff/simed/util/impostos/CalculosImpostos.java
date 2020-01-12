package com.ajeff.simed.util.impostos;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.ajeff.simed.exceptions.ValorInformadoNegativoException;

public class CalculosImpostos {
	
	/*
	 * Constantes para calculos dos impostos
	 */
	private static final BigDecimal TETO_MAXIMO_INSS = new BigDecimal(642.34);
	private static final BigDecimal TAXA_DESCONTO_INSS = new BigDecimal(11);
	private static final BigDecimal TAXA_DESCONTO_PCCS = new BigDecimal(4.65);
	private static final BigDecimal TAXA_DESCONTO_IRPJ = new BigDecimal(1.5);
	private static final BigDecimal PISO_MINIMO_DARF = new BigDecimal(10);
	
	private static final BigDecimal INICIO_PRIMEIRA_FAIXA_IRPF = new BigDecimal(1903.99);
	private static final BigDecimal FINAL_PRIMEIRA_FAIXA_IRPF = new BigDecimal(2826.65);
	private static final BigDecimal INICIO_SEGUNDA_FAIXA_IRPF = new BigDecimal(2826.66);
	private static final BigDecimal FINAL_SEGUNDA_FAIXA_IRPF = new BigDecimal(3751.05);
	private static final BigDecimal INICIO_TERCEIRA_FAIXA_IRPF = new BigDecimal(3751.06);
	private static final BigDecimal FINAL_TERCEIRA_FAIXA_IRPF = new BigDecimal(4664.68);

	private static final BigDecimal DEDUCAO_IRPF_DEPENDENTE = new BigDecimal(189.50);

	
	/*
	 * Método para calculo de Imposto renda (PF) com classe auxiliar CalculosImpotoRendaPF contendo os calculos
	 * para as quatro faixas de retenção
	 */
	public static BigDecimal calculoIRFisica (BigDecimal valor, Integer dependente, Boolean retencaoINSS) {
		if(valor.compareTo(INICIO_PRIMEIRA_FAIXA_IRPF) == -1) {
			return BigDecimal.ZERO;
		}else {
			
			BigDecimal irpf = new BigDecimal(0);
			valor = descontoDeRetencaoINSS(valor, retencaoINSS);
			valor = descontoDeRetencaoDependentes(valor, dependente);
			
			if((valor.compareTo(INICIO_PRIMEIRA_FAIXA_IRPF) == 1) && (valor.compareTo(FINAL_PRIMEIRA_FAIXA_IRPF) == -1)) {
				irpf =  CalculosImpostoRendaPF.calculoImpostoRendaFaixa1(valor).setScale(2, RoundingMode.HALF_UP);
			}else if((valor.compareTo(INICIO_SEGUNDA_FAIXA_IRPF) == 1) && (valor.compareTo(FINAL_SEGUNDA_FAIXA_IRPF) == -1)) {
				irpf =  CalculosImpostoRendaPF.calculoImpostoRendaFaixa2(valor).setScale(2, RoundingMode.HALF_UP);
			}else if((valor.compareTo(INICIO_TERCEIRA_FAIXA_IRPF) == 1) && (valor.compareTo(FINAL_TERCEIRA_FAIXA_IRPF) == -1)) {
				irpf =  CalculosImpostoRendaPF.calculoImpostoRendaFaixa3(valor).setScale(2, RoundingMode.HALF_UP);
			}else {
				irpf =  CalculosImpostoRendaPF.calculoImpostoRendaFaixa4(valor).setScale(2, RoundingMode.HALF_UP);
			}

			return (irpf.compareTo(BigDecimal.ZERO) == 1 ? irpf : BigDecimal.ZERO);
		}
	}
	
	
	/*
	 * Método estatico para calculo do ISS com a taxa informada pelo usuário
	 * 
	 */
	public static BigDecimal calculoISS(BigDecimal valor, BigDecimal taxa) {
		if (valor.compareTo(BigDecimal.ZERO) == -1 || taxa.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("Um dos valores informados é inválido");
		}else {
			return valor.multiply(taxa).divide(new BigDecimal(100), MathContext.DECIMAL32).setScale(2, RoundingMode.HALF_UP);
		}
	}
	
	
	/*
	 * Método estatico para calculo de INSS com taxa fixa de 11% com o teto limite de 642,34
	 */
	public static BigDecimal calculoINSS(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("O valor principal esta negativo");
		}
		BigDecimal inss = (valor.multiply(TAXA_DESCONTO_INSS).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
		return ((inss.compareTo(TETO_MAXIMO_INSS) == -1)? inss : BigDecimal.ZERO);
	}
	
	
	/*
	 * Método estatico para calculo dos impostos PIS/COFINS/CSLL usando a taxa total de 4,65%
	 * PIS: 0,65%, COFINS: 3%, CSLL: 1%
	 */
	public static BigDecimal calculoPCCS(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("O valor principal esta negativo");
		}
		BigDecimal pccs = (valor.multiply(TAXA_DESCONTO_PCCS).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
		return ((pccs.compareTo(PISO_MINIMO_DARF) == 1) || (pccs.compareTo(PISO_MINIMO_DARF) == 0)? pccs : BigDecimal.ZERO);
	}
	
	
	/*
	 * Método estatico para calculo de IMPOSTO RENDA (PJ) com taxa total de 1,5%
	 */
	public static BigDecimal calculoIRJuridico(BigDecimal valor) {
		if(valor.compareTo(BigDecimal.ZERO) == -1) {
			throw new ValorInformadoNegativoException("O valor principal esta negativo");
		}		
		BigDecimal ir = (valor.multiply(TAXA_DESCONTO_IRPJ).divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP);
		return ((ir.compareTo(PISO_MINIMO_DARF) == 1) || (ir.compareTo(PISO_MINIMO_DARF) == 0)? ir : BigDecimal.ZERO);
	}
	
	
	private static BigDecimal descontoDeRetencaoINSS(BigDecimal valor, Boolean retencaoINSS) {
		if(retencaoINSS) {
			valor = valor.subtract(calculoINSS(valor));
		}
		return valor;
	}
	
	private static BigDecimal descontoDeRetencaoDependentes(BigDecimal valor, Integer dependente) {
		if(dependente > 0 || dependente != null) {
			BigDecimal dep = new BigDecimal(dependente);
			BigDecimal descontoDependente = DEDUCAO_IRPF_DEPENDENTE.multiply(dep);
			valor = valor.subtract(descontoDependente);
		}
		return valor;
	}	


}
