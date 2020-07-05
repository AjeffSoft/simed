package com.ajeff.simed.financeiro.storage;

import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.ajeff.simed.financeiro.dto.UploadContaPagarDTO;

public class UploadStorageRunnable implements Runnable {
	
	private UploadStorage uploadStorage;
	private MultipartFile[] files;
	private DeferredResult<UploadContaPagarDTO> result;

	public UploadStorageRunnable(MultipartFile[] files, DeferredResult<UploadContaPagarDTO> result, UploadStorage uploadStorage) {
		this.files = files;
		this.result = result;
		this.uploadStorage = uploadStorage;
	}

	@Override
	public void run() {
		String novoNome = this.uploadStorage.salvarUploadTemporariamente(files);
		String nome = novoNome;
		String contentType = files[0].getContentType();
		result.setResult(new UploadContaPagarDTO(nome, contentType));
	}

}
