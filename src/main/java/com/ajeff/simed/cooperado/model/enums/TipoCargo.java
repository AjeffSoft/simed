package com.ajeff.simed.cooperado.model.enums;

public enum TipoCargo {
	

	PRESIDENTE("Presidente"),
	VICE_PRESIDENTE("Vice Presidente"),
	SUPERINTENDENTE("Superintendente"),
	CONSELHEIRO_VOGAL("Conselheiro Vogal"),
	CONSELHEIRO_TITULAR("Conselheiro Titular"),
	CONSELHEIRO_SUPLENTE("Conselheiro Suplente");
	
	private String descricao;
	
	private TipoCargo(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
	
	

}
