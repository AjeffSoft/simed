package com.ajeff.simed.financeiro.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;

@Controller
public class DashboardFinanceiroController {


	@GetMapping("/financeiro")
	public ModelAndView financeiro(@AuthenticationPrincipal UsuarioSistema usuarioSistema) {	
		ModelAndView mv = new ModelAndView("Financeiro/Dashboard");
		return mv;
	}
	
}
