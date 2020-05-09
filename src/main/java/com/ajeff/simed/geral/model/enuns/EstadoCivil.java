package com.ajeff.simed.geral.model.enuns;

public enum EstadoCivil {
	
	SOLTEIRO ("Solteiro(a)"),
	CASADO ("Casado(a)"),
	UNIAO_ESTAVEL ("União Estável"),
	SEPARADO ("Separado(a)"),
	VIUVO ("Viúvo(a)"),
	OUTRO ("Outro tipo");	
	
	
	private String descricao;
	
	
	EstadoCivil (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

}
