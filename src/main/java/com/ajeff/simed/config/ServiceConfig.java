package com.ajeff.simed.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.financeiro.service.FornecedorService;
import com.ajeff.simed.geral.service.UsuarioService;
import com.ajeff.simed.satisfacao.service.PerguntaService;

@Configuration
@ComponentScan(basePackageClasses = {UsuarioService.class, 
									FornecedorService.class, 
									PerguntaService.class,
									CooperadoService.class})
public class ServiceConfig {
	
}