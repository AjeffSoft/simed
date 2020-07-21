package com.ajeff.simed.financeiro.model.enums;

public enum StatusContaPagar {
	
	ABERTO ("Aberto"),
	AUTORIZADO ("Autorizado"),
	EMITIDO ("Emitido"),
	PAGO ("Pago");
	
	private String descricao;
	
	
	StatusContaPagar (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	

	
	

}
