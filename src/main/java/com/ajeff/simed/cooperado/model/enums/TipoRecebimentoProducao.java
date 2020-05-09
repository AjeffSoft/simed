package com.ajeff.simed.cooperado.model.enums;

public enum TipoRecebimentoProducao {

	CREDITO_CONTA("Crédito em conta banco"),
	CHEQUE("Cheque"),
	OUTRO ("Outro tipo");
	
	private String descricao;
	
	private TipoRecebimentoProducao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}


}
