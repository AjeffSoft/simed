package com.ajeff.simed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ajeff.simed.cooperado.service.MedicoService;
import com.ajeff.simed.financeiro.service.FornecedorService;
import com.ajeff.simed.financeiro.storage.UploadStorage;
import com.ajeff.simed.financeiro.storage.local.UploadStorageLocal;
import com.ajeff.simed.geral.service.UsuarioService;
import com.ajeff.simed.satisfacao.service.PerguntaService;

@Configuration
@ComponentScan(basePackageClasses = {UsuarioService.class, 
									FornecedorService.class, 
									PerguntaService.class,
									MedicoService.class})
public class ServiceConfig {
	
	@Bean
	public UploadStorage uploadStorage() {
		return new UploadStorageLocal();
	}
	
}