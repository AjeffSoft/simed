package com.ajeff.simed.cooperado.repository.filter;

import com.ajeff.simed.cooperado.model.Cooperado;

public class DependenteFilter {
	
	private String nome;
	
	private String cpf;
	
	private Cooperado cooperado;

	
	
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

	public Cooperado getCooperado() {
		return cooperado;
	}

	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
	}


}
