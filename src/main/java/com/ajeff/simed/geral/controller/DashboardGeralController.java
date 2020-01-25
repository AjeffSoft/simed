package com.ajeff.simed.geral.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
public class DashboardGeralController {
	
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/")
	public ModelAndView index(@AuthenticationPrincipal UsuarioSistema usuarioSistema) {	
		ModelAndView mv = new ModelAndView("Index");
//		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		return mv;
	}
	
	@GetMapping("/geral")
	public ModelAndView geral(@AuthenticationPrincipal UsuarioSistema usuarioSistema) {	
		ModelAndView mv = new ModelAndView("Geral/DashboardGeral");
//		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		return mv;
	}
	
	
}
