package com.ajeff.simed.financeiro.service.event;

import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.ContaPagar;

public class ContaPagarSalvoEvent {

	
	private ContaPagar contaPagar;

	public ContaPagarSalvoEvent(ContaPagar contaPagar) {
		this.contaPagar = contaPagar;
	}

	public ContaPagar getContaPagar() {
		return contaPagar;
	}
	
	
	public boolean temArquivoUpload() {
		return !StringUtils.isEmpty(contaPagar.getUpload());
	}
	
	
}
