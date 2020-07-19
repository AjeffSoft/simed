package com.ajeff.simed.financeiro.service.contaPagar;

import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.ContaPagar;

public class ContaPagarSalvaEvent {
	
	private ContaPagar conta;

	public ContaPagarSalvaEvent(ContaPagar conta) {
		this.conta = conta;
	}

	public ContaPagar getContaPagar() {
		return conta;
	}
	
	public boolean isUpload() {
		return !StringUtils.isEmpty(conta.getUpload());
	}

}
