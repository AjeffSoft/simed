package com.ajeff.simed.financeiro.service.contaPagar;

import java.util.List;

import com.ajeff.simed.financeiro.model.ContaPagar;

public class ContaPagarSalvaEvent {
	
	private List<ContaPagar> contas;

	public ContaPagarSalvaEvent(List<ContaPagar> contas) {
		this.contas = contas;
	}

	public List<ContaPagar> getContaPagar() {
		return contas;
	}
	
	public boolean isUpload() {
		return contas.stream().filter(c -> c.getUpload() != null).count() > 0 ? true : false;
	}

}
