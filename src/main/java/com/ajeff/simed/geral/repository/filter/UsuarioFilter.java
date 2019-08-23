package com.ajeff.simed.geral.repository.filter;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Grupo;

public class UsuarioFilter {

	private String nomeCompleto;
	private String nome;
	private Empresa empresa;
	private Grupo grupo;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Grupo getGrupo() {
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}
	
	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}
	
}