package com.ajeff.simed.financeiro.service.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ajeff.simed.financeiro.storage.UploadStorage;

@Component
public class ContaPagarListener {

//	@Autowired
//	private UploadStorage uploadStorage;
//	
//	@EventListener
//	public void contaPagarSalva(ContaPagarSalvoEvent event) {
//		
//		if(event.temArquivoUpload()) {
//			uploadStorage.moverArquivoParaLocal(event.getContaPagar().getUpload());
//		}
//		
//	}
}
