package com.ajeff.simed.financeiro.storage;

import org.springframework.web.multipart.MultipartFile;

public interface UploadStorage {
	
	public String salvarTemporariamente(MultipartFile[] files);

	public byte[] exibirUpload(String nome);

	public void excluirArquivo(String upload);

	public void moverArquivoParaLocal(String upload);
}
