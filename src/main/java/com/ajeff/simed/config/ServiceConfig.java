package com.ajeff.simed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ajeff.simed.financeiro.service.FornecedorService;
import com.ajeff.simed.financeiro.storage.UploadContaPagarStorageLocal;
import com.ajeff.simed.financeiro.storage.UploadStorage;
import com.ajeff.simed.geral.service.UsuarioService;

@Configuration
@ComponentScan(basePackageClasses = {UsuarioService.class, FornecedorService.class})
public class ServiceConfig {
	
	@Bean
	public UploadStorage uploadStorage() {
		return new UploadContaPagarStorageLocal();
	}
	

}