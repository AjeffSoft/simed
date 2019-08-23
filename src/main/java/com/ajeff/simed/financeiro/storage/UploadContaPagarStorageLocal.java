package com.ajeff.simed.financeiro.storage;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class UploadContaPagarStorageLocal implements UploadStorage{

	private static final Logger LOG = LoggerFactory.getLogger(UploadContaPagarStorageLocal.class);

	private Path local;
	private Path localTemporario;
	
	//CRIAR VARIAVEL AMBIENTE COM CAMINHO DA PASTA E INFORMAR O NOME DA VARIAVEL NO MÉTODO EX: SIMED
	public UploadContaPagarStorageLocal() {
		this(FileSystems.getDefault().getPath(System.getenv("SIMED"), "upload"));
		criarPastas();
	}
	
	public UploadContaPagarStorageLocal(Path path) {
		this.local = path;
		criarPastas();
	}

	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = FileSystems.getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
			
			if(LOG.isDebugEnabled()) {
				LOG.debug("Pastas criadas para salvar arquivos de upload");
				LOG.debug("Pasta Padrão: " + this.local.toAbsolutePath());
				LOG.debug("Pasta Temporária: " + this.localTemporario.toAbsolutePath());
			}
			
		} catch (IOException e) {
			throw new RuntimeException("Erro ao criar pasta para salvar os uploads", e);
		}
	}

	@Override
	public String salvarTemporariamente(MultipartFile[] files) {

		String novoNome = null;
		
		if(files != null && files.length > 0 ) {
			MultipartFile arquivo = files[0];
			
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			
			try {
				arquivo.transferTo(new File(this.localTemporario.toAbsolutePath().toString() + FileSystems.getDefault().getSeparator() + novoNome));
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando o arquivo de upload na pasta temporária", e);
			}
		}
		return novoNome;
	}

	@Override
	public byte[] exibirUpload(String nome) {
		try {
			System.out.println(this.local.resolve(nome));
			return Files.readAllBytes(this.local.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro lendo arquivo de upload na pasta local!!", e);
		}
	}	
	

	@Override
	public void excluirArquivo(String upload) {
		try {
			Files.deleteIfExists(this.local.resolve(upload));
		} catch (IOException e) {
			LOG.warn(String.format("Erro ao apagar a foto: '%s'. Mensagem: %s", upload, e.getMessage()));
		}
	}	
	
	private String renomearArquivo(String nomeOriginal) {
		String novoNome = UUID.randomUUID().toString() + "_" + nomeOriginal;
		
		if (LOG.isDebugEnabled()) {
			LOG.debug(String.format("Nome original: %s, novo nome: %s", nomeOriginal, novoNome));
		}
		
		return novoNome;
		
	}

	@Override
	public void moverArquivoParaLocal(String upload) {
		try {
			Files.move(this.localTemporario.resolve(upload), this.local.resolve(upload));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao mover o arquivo de upload da pasta temp para local definitivo" + e.getMessage());
		}
	}



}
