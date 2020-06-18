package com.ajeff.simed.cooperado.model.enums;

public enum TipoDiretor {
	

	ADMINISTRATIVO("Administrativo"),
	FISCAL("Fiscal"),
	VOGAL("Vogal"),
	TECNICO("Técnico");
	
	private String descricao;
	
	private TipoDiretor(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
