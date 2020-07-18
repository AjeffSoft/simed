package com.ajeff.simed.financeiro.storage;

import org.springframework.web.multipart.MultipartFile;

public interface UploadStorage {
	
	String salvarUploadTemporariamente(MultipartFile[] upload);

	byte[] recuperarUploadTemporaria(String nome);

	void salvar(String upload);

}
