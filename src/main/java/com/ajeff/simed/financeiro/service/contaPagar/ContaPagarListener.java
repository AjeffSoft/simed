package com.ajeff.simed.financeiro.service.contaPagar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ajeff.simed.financeiro.storage.UploadStorage;

@Component
public class ContaPagarListener {
	
	@Autowired
	private UploadStorage storage;

	@EventListener(condition = "#event.upload")
	public void contaPagarSalvo(ContaPagarSalvaEvent event) {
		storage.salvar(event.getContaPagar().get(0).getUpload());
	}
	
}
