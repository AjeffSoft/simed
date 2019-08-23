package com.ajeff.simed.financeiro.repository.filter;

import com.ajeff.simed.financeiro.model.PlanoConta;

public class PlanoContaSecundariaFilter {
	
	private String nome;
	private String tipo;
	private PlanoConta planoConta;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public PlanoConta getPlanoConta() {
		return planoConta;
	}
	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

}
