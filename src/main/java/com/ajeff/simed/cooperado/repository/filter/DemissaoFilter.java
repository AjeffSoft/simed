package com.ajeff.simed.cooperado.repository.filter;

import java.time.LocalDate;

public class DemissaoFilter {

	private String cooperado;
	private String tipo;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	

	public LocalDate getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDate getDataFim() {
		return dataFim;
	}
	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}
	public String getCooperado() {
		return cooperado;
	}
	public void setCooperado(String cooperado) {
		this.cooperado = cooperado;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
}
