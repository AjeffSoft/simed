package com.ajeff.simed.geral.repository.filter;

import com.ajeff.simed.geral.model.Cidade;

public class PessoaFilter {
	
	private String nome;
	
	private String documento1;
	
	private String fantasia;
	
	private Cidade cidade;

	
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento1() {
		return documento1;
	}

	public void setDocumento1(String documento1) {
		this.documento1 = documento1;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getFantasia() {
		return fantasia;
	}

	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}

}
