package com.ajeff.simed.cooperado.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.Valid;

import com.ajeff.simed.geral.model.InformacaoPessoal;
import com.ajeff.simed.geral.model.OutrosDocumentos;
import com.ajeff.simed.geral.model.Pessoa;

@DiscriminatorValue("MEDICO")
@Entity
public class Medico extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Valid
	@Embedded
	private OutrosDocumentos documento;
	
	@Embedded
	private InformacaoPessoal informacaoPessoal;
	
	private String anotacao;

	public OutrosDocumentos getDocumento() {
		return documento;
	}

	public void setDocumento(OutrosDocumentos documento) {
		this.documento = documento;
	}

	public InformacaoPessoal getInformacaoPessoal() {
		return informacaoPessoal;
	}

	public void setInformacaoPessoal(InformacaoPessoal informacaoPessoal) {
		this.informacaoPessoal = informacaoPessoal;
	}

	public String getAnotacao() {
		return anotacao;
	}

	public void setAnotacao(String anotacao) {
		this.anotacao = anotacao;
	}
	

}
