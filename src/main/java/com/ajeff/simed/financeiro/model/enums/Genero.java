package com.ajeff.simed.financeiro.model.enums;

public enum Genero {
	
	FEMININO ("Feminino"),
	MASCULINO ("Masculino"),
	OUTRA ("Outro Tipo");
	
	private String descricao;
	
	
	Genero (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	

	
	

}
