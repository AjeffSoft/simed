package com.ajeff.simed.financeiro.model.enums;

public enum TipoConta {
	
	CORRENTE ("Conta Corrente"),
	POUPANÇA ("Conta Poupança"),
	SALARIO ("Conta Salário"),
	APLICACAO ("Conta Aplicação"),
	OUTRA ("Outro Tipo");
	
	private String descricao;
	
	
	TipoConta (String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	

	
	

}
