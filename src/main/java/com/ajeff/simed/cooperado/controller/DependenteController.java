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

import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.repository.filter.DependenteFilter;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.cooperado.service.DependenteService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/cooperado/dependente")
public class DependenteController {

	@Autowired
	private DependenteService service;
	@Autowired
	private CooperadoService serviceCooperado;

	
	@GetMapping("/novo")
	public ModelAndView novo(Dependente dependente) {
		ModelAndView mv = new ModelAndView("Cooperado/dependente/CadastroDependente");
		mv.addObject("cooperados", serviceCooperado.listarCooperados());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Dependente dependente, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(dependente);
		}
		
		try {
			service.salvar(dependente);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(dependente);
		}
		attributes.addFlashAttribute("mensagem", "Dependente " + dependente.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/cooperado/dependente/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(DependenteFilter dependenteFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/dependente/PesquisarDependentes");
		mv.addObject("cooperados", serviceCooperado.listarCooperados());

		PageWrapper<Dependente> paginaWrapper = new PageWrapper<>(service.filtrar(dependenteFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar(@PathVariable Long id, Dependente dependente) {
		ModelAndView mv = novo(dependente);
		dependente= service.findOne(id);
		
		mv.addObject(dependente);
		return mv;
	}
	
}
