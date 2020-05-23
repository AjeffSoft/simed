package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.model.enums.TipoDescooperar;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;
import com.ajeff.simed.cooperado.service.CancelamentoService;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;

@Controller
@RequestMapping("/cooperado/cooperado/cancelamento")
public class CancelamentoController {

	@Autowired
	private CancelamentoService service;
	
	
	
	@GetMapping("/descooperar/{id}")
	public ModelAndView descooperar(@PathVariable Long id, Cooperado cooperado) {
		cooperado = service.findOne(id);
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/CadastroCancelamento");
		mv.addObject(cooperado);
		mv.addObject("tipos", TipoDescooperar.values());
		return mv;
	}
	
	

	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Cooperado cooperado, BindingResult result) {

		if (result.hasErrors()) {
			return descooperar(cooperado.getId(), cooperado);
		}
		
		try {
			service.descooperar(cooperado);
			
		} catch (VencimentoMenorEmissaoException e) {
			result.rejectValue("dataCancelamento", e.getMessage(), e.getMessage());
			return descooperar(cooperado.getId(), cooperado);
		}
		return new ModelAndView("redirect:/cooperado/cooperado/pesquisar");
	}
	

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CancelamentoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/PesquisarCancelamentos");
		PageWrapper<Cooperado> paginaWrapper = new PageWrapper<>(service.filtrar(cooperadoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable long id, Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/DetalheDescooperado");
		cooperado = service.findOne(id);
		mv.addObject(cooperado);
		return mv;
	}
	

}
