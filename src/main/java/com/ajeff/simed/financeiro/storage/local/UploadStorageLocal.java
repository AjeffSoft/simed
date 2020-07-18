package com.ajeff.simed.financeiro.storage.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.ajeff.simed.financeiro.storage.UploadStorage;

public class UploadStorageLocal implements UploadStorage {


	private Path local;
	private Path localTemporario;
	
	public UploadStorageLocal() {
		this.local = FileSystems.getDefault().getPath(System.getenv("UPLOADSIMED"), "ARQUIVO");
		criarPastas();
	}

	private void criarPastas() {
		try {
			Files.createDirectories(this.local);
			this.localTemporario = FileSystems.getDefault().getPath(this.local.toString(), "temp");
			Files.createDirectories(this.localTemporario);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao criar as pastas para salvar uploads! ", e);
		}
	}

	@SuppressWarnings("null")
	@Override
	public String salvarUploadTemporariamente(MultipartFile[] files) {
		String novoNome = null;
		if(files != null || files.length > 0) {
			MultipartFile arquivo = files[0];
			novoNome = renomearArquivo(arquivo.getOriginalFilename());
			try {
				arquivo.transferTo(new File (this.localTemporario.toAbsolutePath().toString() + FileSystems.getDefault().getSeparator() + novoNome));
			} catch (IOException e) {
				throw new RuntimeException("Erro salvando upload na pasta temporária!!");
			}
			
		}
		return novoNome;
	}
	
	
	private String renomearArquivo(String nomeOriginal) {
		String novoNome = UUID.randomUUID().toString() + "__" + nomeOriginal;
		return novoNome;
	}
	

	@Override
	public byte[] recuperarUploadTemporaria(String nome) {
		try {
			return Files.readAllBytes(this.localTemporario.resolve(nome));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao ler o arquivo upload. ", e);
		}
	}

	@Override
	public void salvar(String upload) {
		try {
			Files.move(this.localTemporario.resolve(upload), this.local.resolve(upload));
		} catch (IOException e) {
			throw new RuntimeException("Erro ao mover o upload para o destino final " + e);
		}
	}
	
	
}
