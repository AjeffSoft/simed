package com.ajeff.simed.geral.repository.filter;

import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.model.Cidade;

public class AgenciaFilter {

	private String agencia;
	private Banco banco;
	private Cidade cidade;
	
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
	public Cidade getCidade() {
		return cidade;
	}
	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}
}
