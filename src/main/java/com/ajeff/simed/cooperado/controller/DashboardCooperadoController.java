package com.ajeff.simed.cooperado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
public class DashboardCooperadoController {
	
	@Autowired
	private UsuarioService usuarioService;
	

	@GetMapping("/cooperado")
	public ModelAndView cooperado(@AuthenticationPrincipal UsuarioSistema usuarioSistema) {	
		ModelAndView mv = new ModelAndView("Cooperado/Dashboard");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		return mv;
	}
	
}
