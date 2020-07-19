package com.ajeff.simed.financeiro.controller.contaPagar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import com.ajeff.simed.financeiro.dto.UploadContaPagarDTO;
import com.ajeff.simed.financeiro.storage.UploadStorage;
import com.ajeff.simed.financeiro.storage.UploadStorageRunnable;

@RestController
@RequestMapping("/financeiro/contasPagar/upload")
public class UploadContaPagarController {
	
	@Autowired
	private UploadStorage uploadStorage;

	@PostMapping
	public DeferredResult<UploadContaPagarDTO> upload (@RequestParam("files[]") MultipartFile[] files) {
		DeferredResult<UploadContaPagarDTO> result = new DeferredResult<>();
		Thread thread = new Thread(new UploadStorageRunnable(files, result, uploadStorage));
		thread.start();
		return result;
	}
	
	
	@GetMapping("/temp/{nome:.*}")
	public byte[] recuperarUpload(@PathVariable String nome) {
		return uploadStorage.recuperarUploadTemporaria(nome);
	}
	
	@GetMapping("/{nome:.*}")
	public byte[] recuperar(@PathVariable String nome) {
		return uploadStorage.recuperarUpload(nome);
	}	
}
