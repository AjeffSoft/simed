package com.ajeff.simed.cooperado.repository.filter;

import com.ajeff.simed.geral.model.Cidade;

public class CooperadoFilter {
	
	private String nome;
	
	private String cpf;
	
	private String crm;
	
	private Cidade cidade;

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCrm() {
		return crm;
	}

	public void setCrm(String crm) {
		this.crm = crm;
	}


}
