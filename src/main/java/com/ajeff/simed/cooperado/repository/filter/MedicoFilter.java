package com.ajeff.simed.cooperado.repository.filter;

import com.ajeff.simed.geral.model.Cidade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicoFilter {
	
	private String nome;
	
	private String documento1;
	
	private String sigla;
	
	private Cidade cidade;

}
