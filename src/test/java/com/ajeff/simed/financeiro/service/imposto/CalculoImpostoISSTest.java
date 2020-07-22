package com.ajeff.simed.financeiro.service.imposto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;


public class CalculoImpostoISSTest {

	
	@Test
	@DisplayName("Deve retornar o valor do ISS com aliquota de 5%")
	public void deveRetornarIssAliquota5() {
		BigDecimal valor = BigDecimal.valueOf(1000);
		BigDecimal issPorcentagem = BigDecimal.valueOf(5);
		
		BigDecimal result = CalculoImpostoISS.calculo(valor, issPorcentagem);
		
		assertThat(result).isEqualTo(BigDecimal.valueOf(50).setScale(2, RoundingMode.HALF_UP));
	}

	@Test
	@DisplayName("Deve retornar o valor do ISS zerado quando o valor for negativo")
	public void deveRetornarIssZeradoComValorNegativo() {
		BigDecimal valor = BigDecimal.valueOf(-1000);
		BigDecimal issPorcentagem = BigDecimal.valueOf(5);
		
		BigDecimal result = CalculoImpostoISS.calculo(valor, issPorcentagem);
		
		assertThat(result).isEqualTo(BigDecimal.ZERO);
	}

	
	@Test
	@DisplayName("Deve retornar o valor do ISS zerado quando o valor for zero")
	public void deveRetornarIssZeradoComValorZerado() {
		BigDecimal valor = BigDecimal.ZERO;
		BigDecimal issPorcentagem = BigDecimal.valueOf(5);
		
		BigDecimal result = CalculoImpostoISS.calculo(valor, issPorcentagem);
		
		assertThat(result).isEqualTo(BigDecimal.ZERO);
	}

	@Test
	@DisplayName("Deve lançar erro quando o valor para cálculo não for informado")
	public void deveLancarErroNaoInformadoValor() {
		BigDecimal valor = null;
		BigDecimal issPorcentagem = BigDecimal.valueOf(5);
		
		Throwable result = assertThrows(NullPointerException.class, () -> CalculoImpostoISS.calculo(valor, issPorcentagem));
		
		assertThat(result).isInstanceOf(NullPointerException.class);
	}
}
