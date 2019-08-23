package com.ajeff.simed.financeiro.repository.filter;

import com.ajeff.simed.geral.model.Empresa;

public class MovimentacaoFilter {

	private Boolean fechado;
	private Empresa empresa;
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Boolean getFechado() {
		return fechado;
	}
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}	
}
