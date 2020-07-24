package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import com.ajeff.simed.financeiro.model.TabelaIRPF;

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
	
	
	
//	public BigDecimal aliquotaIRPF(BigDecimal valor) {
//		List<TabelaIRPF> tabelas = tabelaIRPFRepository.findAll();
//
//		Function<TabelaIRPF, BigDecimal> calculo = a -> {
//			return valor.multiply( (a.getAliquota().divide(BigDecimal.valueOf(100))) ).subtract(a.getDeducao());
//		};
//		
//		Predicate<TabelaIRPF> filtro = t -> {
//			return valor.compareTo(t.getValorInicial()) >=0 && valor.compareTo(t.getValorFinal()) <=0; 
//		};
//
//		return tabelas.stream()
//				.filter(i -> filtro.test(i))
//				.map( i -> calculo.apply(i) )
//				.findFirst().get();
//	}	
	

}
