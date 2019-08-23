package com.ajeff.simed.satisfacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DashboardSatisfacaoController {
	

	
	@GetMapping("/satisfacao")
	public ModelAndView dashboard() {	
		ModelAndView mv = new ModelAndView("Satisfacao/Dashboard");
		return mv;
	}
	
}
