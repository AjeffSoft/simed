package com.ajeff.simed.satisfacao.model.enums;

public enum GrauPergunta {
	
	BAIXO ("Grau Baixo"),
	MEDIO ("Grau Médio"),
	ALTO ("Grau Alto"),
	NULO ("Nulo");

	
	private String descricao;
	
	
	GrauPergunta (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	

	
	

}
