package com.ajeff.simed.cooperado.model.enums;

public enum TipoDescooperar {
	

	DEMISSAO("Demissão"),
	EXCLUSAO("Exclusão"),
	ELIMINACAO("Eliminação");
	
	private String descricao;
	
	private TipoDescooperar(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
