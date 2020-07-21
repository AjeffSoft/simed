package com.ajeff.simed.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

import com.ajeff.simed.exceptions.DataNaoInformadaException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.util.model.Feriado;

public class DatasUtils {
	
	
	
	public static Boolean emissaoMenorOuIgualVencimento(LocalDate emissao, LocalDate vencimento) {
		if (emissao == null || vencimento == null) {
			throw new DataNaoInformadaException("A data de emissão e/ou vencimento não foi informada!");
		}
		if (emissao.isAfter(vencimento)) {
			throw new VencimentoMenorEmissaoException("A data de vencimento não pode ser menor que a data de emissão!");
		}	
		return emissao.isEqual(vencimento) || emissao.isBefore(vencimento);
	}
	
	
	public static LocalDate setarParaUltimoDiaMes(LocalDate data) {
		if (data == null) {
			throw new DataNaoInformadaException("A data não foi informada!");
		}
		return data.minusMonths(0).with(TemporalAdjusters.lastDayOfMonth());
	}	
	

	public static Boolean dataFinalSemana(LocalDate data) {
		if (data == null) {
			throw new DataNaoInformadaException("A data não foi informada!");
		}
		DayOfWeek day = data.getDayOfWeek();
		return (day.getValue() == 6 || day.getValue() == 7)? true: false;
	}	
	
	
	
	public static boolean diaUtil(LocalDate data) {
		if (data == null) {
			throw new DataNaoInformadaException("A data não foi informada!");
		}
		
		if(dataFinalSemana(data)) {
			return false;
		}
		
		if(diaFeriado(data)) {
			return false;
		}
		
		return true;
	}
	
	
	public static boolean diaFeriado(LocalDate data) {
		if (data == null) {
			throw new DataNaoInformadaException("A data não foi informada!");
		}
		
		Feriado feriado = new Feriado();
		Integer dia = data.getDayOfMonth();
		Integer mes = data.getMonthValue();
		
		long count = feriado.getFeriados().stream()
			.filter(d -> d.getDayOfMonth() == dia && d.getMonth().getValue() == mes).count();
		
		return count > 0 ? true : false;
	}
	
	
	public static LocalDate somarDiasNoInicioMesRetornandoDataUtil(LocalDate data, Integer dias) {
		LocalDate result = data;
		
		if (data == null) {
			throw new DataNaoInformadaException("A data não foi informada!");
		}
		
		if (dias != null) {
			result = data.with(TemporalAdjusters.firstDayOfMonth()).plusDays(dias -1);
		}

		do {
			if(!diaUtil(result)) {
				result = result.minusDays(1);
			}
		}while(diaUtil(result));
		
		return result;
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
	
	
	public static Date convertLocalDateInDate(LocalDate dataOrigin) {
		if(dataOrigin.equals(null)) {
			throw new DataNaoInformadaException("Não foi informado a data de origem para conversão");
		}else {
			ZoneId zone = ZoneId.systemDefault();
			return Date.from(dataOrigin.atStartOfDay(zone).toInstant());			
		}

	}

}
