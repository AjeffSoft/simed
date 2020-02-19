package com.ajeff.simed.satisfacao.repository.filter;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Pergunta;

public class QuestionarioFilter {

	private Empresa empresa;
	private Pergunta pergunta;
	private String tipoResposta;
	
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Pergunta getPergunta() {
		return pergunta;
	}
	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}
	public String getTipoResposta() {
		return tipoResposta;
	}
	public void setTipoResposta(String tipoResposta) {
		this.tipoResposta = tipoResposta;
	}
	


}
