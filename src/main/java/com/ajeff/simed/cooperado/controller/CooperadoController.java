package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.EstadoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/cooperado/cooperado")
public class CooperadoController {

	@Autowired
	private CooperadoService service;
	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private EstadoService estadoService;

	
	@GetMapping("/novo")
	public ModelAndView novo(Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/CadastroCooperado");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		mv.addObject("agencias", agenciaService.findAllOrderByAgencia());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Cooperado cooperado, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cooperado);
		}
		
		try {
			service.salvar(cooperado);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(cooperado);
		}
		attributes.addFlashAttribute("mensagem", "Cooperado " + cooperado.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/cooperado/cooperado/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CooperadoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/PesquisarCooperados");

		PageWrapper<Cooperado> paginaWrapper = new PageWrapper<>(service.filtrar(cooperadoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable Long id, Cooperado cooperado) {
		cooperado = service.buscarComCidadeEstado(id);
		ModelAndView mv = novo(cooperado);
		mv.addObject(cooperado);
		return mv;
	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/DetalheCooperado");
		cooperado = service.findOne(id);
		mv.addObject(cooperado);
		return mv;
	}
	
	
}
