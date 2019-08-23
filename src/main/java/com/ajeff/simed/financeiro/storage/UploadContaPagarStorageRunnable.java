package com.ajeff.simed.financeiro.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.ajeff.simed.financeiro.dto.UploadContaPagarDTO;

public class UploadContaPagarStorageRunnable implements Runnable {

	private MultipartFile[] files;
	private DeferredResult<UploadContaPagarDTO> resultado;
	private UploadStorage uploadStorage;
	
	public UploadContaPagarStorageRunnable(MultipartFile[] files, DeferredResult<UploadContaPagarDTO> resultado, UploadStorage uploadStorage) {
		this.files = files;
		this.resultado = resultado;
		this.uploadStorage = uploadStorage;
	}


	@Override
	public void run() {
		String nomeUpload = this.uploadStorage.salvarTemporariamente(files);
		String contentType = files[0].getContentType();
		resultado.setResult(new UploadContaPagarDTO(nomeUpload, contentType));
	}

}
