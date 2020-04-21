package com.ajeff.simed.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

import com.ajeff.simed.exceptions.DataNaoInformadaException;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;

public class CalculosComDatas {
	
	
	public static LocalDate setarDataSomandoDiasNoInicioMes(LocalDate data, Integer dias, Boolean diaUtil, Boolean antecipar) {
		LocalDate result = LocalDate.MIN;
		if (data != null && dias != null) {
			result = data.with(TemporalAdjusters.firstDayOfMonth());
			result = result.plusDays(dias -1);

			if (diaUtil) {
				if(dataFinalSemana(result)) {
					if(antecipar) {
						if (dataDomingo(result)) {
							result = result.minusDays(2); 
						}else {
							result = result.minusDays(1);
						}
					}else {
						if (dataDomingo(result)) {
							result = result.plusDays(1);
						}else {
							result = result.plusDays(2);
						}
					}
				}
			}
		}
			return result;
	}
	
	
	
	public static LocalDate setarParaUltimoDiaMes(LocalDate data) {
		return data.minusMonths(0).with(TemporalAdjusters.lastDayOfMonth());
	}
	
	
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
	
	
	public static LocalDate setarVencimentoXTotalParcelas(LocalDate emissao, Integer parcela, Integer dias) {
		if (emissao == null) {
			throw new DataNaoInformadaException("Data de emissão não informada!");
		}
		return emissao.plusDays(dias * parcela);
	}
	
	
	public static Long diferencaEntreDuasDatas(LocalDate dataInicio, LocalDate dataFinal) {
		Long dias = 0L;
		if(dataInicio == null || dataFinal == null) {
			throw new DataNaoInformadaException("Data não informada!");
		}
		dias = ChronoUnit.DAYS.between(dataInicio, dataFinal);
		return dias >= 0 ? dias : 0L;
	}

}
