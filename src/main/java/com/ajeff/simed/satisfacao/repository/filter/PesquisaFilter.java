package com.ajeff.simed.satisfacao.repository.filter;

import java.time.LocalDateTime;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Usuario;

public class PesquisaFilter {
	
	private Empresa empresa;
	private Usuario usuario;
	private Boolean fechado;
	private LocalDateTime dataInicio;
	private LocalDateTime dataFim;
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Boolean getFechado() {
		return fechado;
	}
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}
	public LocalDateTime getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDateTime dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDateTime getDataFim() {
		return dataFim;
	}
	public void setDataFim(LocalDateTime dataFim) {
		this.dataFim = dataFim;
	}

	

}
