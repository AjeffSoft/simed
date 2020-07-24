package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;

public class CalculoImpostoIRRF {



	public static BigDecimal calculo(BigDecimal valor, String tipo, BigDecimal inss) {
		
		if(isPessoaJuridica(tipo)) {
			//calculo para PJ
			return BigDecimal.TEN;
		}else {
			//calculo para PF
			return BigDecimal.ONE;
		}
	}
	
	
	private static boolean isPessoaJuridica(String tipo) {
		return tipo.equals("J") ? true : false;
	}
	

}
