package com.ajeff.simed.cooperado.repository.filter;

import java.time.LocalDate;

import com.ajeff.simed.cooperado.model.Cooperado;

public class CancelamentoFilter {

	private Cooperado cooperado;
	private String registro;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	
	public Cooperado getCooperado() {
		return cooperado;
	}
	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}
	public String getRegistro() {
		return registro;
	}
	public void setRegistro(String registro) {
		this.registro = registro;
	}
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
	
}
