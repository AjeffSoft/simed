package com.ajeff.simed.geral.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrosController {

	@GetMapping("/404")
	public String paginaNaoEncontrada() {
		return "404";
	}
	
	@GetMapping("/500")
	public String erroServidor() {
		return "500";
	}
	
	@GetMapping("/ListaVaziaErro")
	public String listaVaziaErro() {
		return "ListaVaziaErro";
	}
	
	
}