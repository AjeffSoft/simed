package com.ajeff.simed.financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParcelamentoDTO {

	private Integer parcela;
	private LocalDate vencimento;
	private BigDecimal valor;
	
	
	@Override
	public String toString() {
		return "Parcela: "+this.parcela + ", Vencimento: "+ this.vencimento + ", Valor R$ " + this.valor;
	}
}
