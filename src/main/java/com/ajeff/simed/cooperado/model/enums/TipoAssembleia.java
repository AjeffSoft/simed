package com.ajeff.simed.cooperado.model.enums;

public enum TipoAssembleia {
	

	EXTRAORDINARIA("Extraordinária"),
	ORDINARIA("Ordinária");
	
	private String descricao;
	
	private TipoAssembleia(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
