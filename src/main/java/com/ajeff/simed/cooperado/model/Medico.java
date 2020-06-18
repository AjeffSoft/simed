package com.ajeff.simed.cooperado.model;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.Valid;

import com.ajeff.simed.geral.model.InformacaoPessoal;
import com.ajeff.simed.geral.model.OutrosDocumentos;
import com.ajeff.simed.geral.model.Pessoa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@DiscriminatorValue("MEDICO")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Medico extends Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Valid
	@Embedded
	private OutrosDocumentos documento;
	
	@Embedded
	private InformacaoPessoal informacaoPessoal;
	
	private String anotacao;

}
