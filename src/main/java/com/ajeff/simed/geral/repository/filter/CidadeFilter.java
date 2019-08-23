package com.ajeff.simed.geral.repository.filter;

import com.ajeff.simed.geral.model.Estado;

public class CidadeFilter {

	private String nome;
	private Estado uf;

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Estado getUf() {
		return uf;
	}
	public void setUf(Estado uf) {
		this.uf = uf;
	}
	
}
