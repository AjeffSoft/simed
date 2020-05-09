package com.ajeff.simed.cooperado.repository.filter;

import com.ajeff.simed.cooperado.model.Medico;

public class DependenteFilter {
	
	private String nome;
	
	private String cpf;
	
	private Medico cooperado;

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Medico getCooperado() {
		return cooperado;
	}

	public void setCooperado(Medico cooperado) {
		this.cooperado = cooperado;
	}


}
