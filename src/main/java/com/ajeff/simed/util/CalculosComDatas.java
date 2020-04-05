package com.ajeff.simed.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import com.ajeff.simed.exceptions.DataNaoInformadaException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;

public class CalculosComDatas {
	
	
	
	public static Boolean dataFinalSemana(LocalDate data) {
		DayOfWeek day = data.getDayOfWeek();
		return (day.getValue() == 6 || day.getValue() == 7)? true: false;
	}
	

	public static Boolean dataSabado(LocalDate data) {
		DayOfWeek day = data.getDayOfWeek();
		return (day.getValue() == 6)? true: false;
	}

	public static Boolean dataDomingo(LocalDate data) {
		DayOfWeek day = data.getDayOfWeek();
		return (day.getValue() == 7 )? true: false;
	}
	
	
	public static LocalDate sumDays(LocalDate data, Integer dias) {
		data.with(TemporalAdjusters.firstDayOfMonth());
		return data.plusDays(dias);
	}
	
	public static int numeroDiaSemana(LocalDate data) {
		return data.getDayOfWeek().getValue();
	}
	
	
	public static Boolean emissaoMenorIgualVencimento(LocalDate emissao, LocalDate vencimento) {
		if (emissao == null || vencimento == null) {
			throw new DataNaoInformadaException("A data de emissão e/ou vencimento não foi informada!");
		}
		if (emissao.isAfter(vencimento)) {
			throw new VencimentoMenorEmissaoException("A data de vencimento não pode ser menor que a data de emissão");
		}	
		return emissao.isEqual(vencimento) || emissao.isBefore(vencimento);
	}
	
	
	public static LocalDate setarVencimento(LocalDate emissao, Integer parcela, Integer dias) {
		if (emissao == null) {
			throw new DataNaoInformadaException("Data de emissão não informada!");
		}
		return emissao.plusDays(dias * parcela);
	}

}
