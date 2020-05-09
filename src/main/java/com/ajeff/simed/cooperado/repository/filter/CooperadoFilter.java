package com.ajeff.simed.cooperado.repository.filter;

import java.time.LocalDate;

import com.ajeff.simed.cooperado.model.Medico;

public class CooperadoFilter {

	private Medico medicoCooperado;
	private String registro;
	private LocalDate dataInicio;
	private LocalDate dataFim;
	
	public Medico getMedicoCooperado() {
		return medicoCooperado;
	}
	public void setMedicoCooperado(Medico medicoCooperado) {
		this.medicoCooperado = medicoCooperado;
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
