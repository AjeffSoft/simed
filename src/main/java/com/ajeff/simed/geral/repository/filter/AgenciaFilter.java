package com.ajeff.simed.geral.repository.filter;

import com.ajeff.simed.geral.model.Banco;

public class AgenciaFilter {

	private String agencia;
	private Banco banco;
	
	public String getAgencia() {
		return agencia;
	}
	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}

}
