package com.ajeff.simed.financeiro.model.enums;

public enum GrauPergunta {
	
	BAIXO ("Grau Baixo"),
	MEDIO ("Grau Médio"),
	ALTO ("Grau Alto");
	
	private String descricao;
	
	
	GrauPergunta (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	

	
	

}
