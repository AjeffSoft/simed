package com.ajeff.simed.util.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Feriado{
	
	private LocalDate anoNovo = LocalDate.of(2020, 1, 1);
	private LocalDate diaCriancas = LocalDate.of(2020, 10, 12);

	private List<LocalDate> datasFixas = new ArrayList<>();
	
	public Feriado() {
		setDatasFixas(anoNovo);
		setDatasFixas(diaCriancas);
	}

	public List<LocalDate> getFeriados() {
		return datasFixas;
	}

	private void setDatasFixas(LocalDate data) {
		this.datasFixas.add(data);
	}
	
	
	
	
	

}
