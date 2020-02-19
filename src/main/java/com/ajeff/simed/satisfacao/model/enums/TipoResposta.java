package com.ajeff.simed.satisfacao.model.enums;

public enum TipoResposta {
	NOTA ("Escolha por nota"),
	SIM_NAO("Sim ou Não");
	
	
	private String descricao;
	
	
	TipoResposta (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
