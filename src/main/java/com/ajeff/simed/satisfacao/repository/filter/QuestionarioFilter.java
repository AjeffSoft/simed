package com.ajeff.simed.satisfacao.repository.filter;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.enums.TipoResposta;

public class QuestionarioFilter {

	private Empresa empresa;
	private Pergunta pergunta;
	private TipoResposta tipoResposta;
	
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
	public TipoResposta getTipoResposta() {
		return tipoResposta;
	}
	public void setTipoResposta(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	


}
