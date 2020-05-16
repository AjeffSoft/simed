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
import com.ajeff.simed.cooperado.model.enums.TipoRecebimentoProducao;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.PessoaService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/cooperado/cooperado")
public class CooperadoController {

	@Autowired
	private CooperadoService service;
	@Autowired
	private PessoaService serviceMedico;

	
	@GetMapping("/novo")
	public ModelAndView novo(Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/CadastroCooperado");
		
		if(cooperado.isNovo()) {
			mv.addObject("medicos", serviceMedico.findByAtivoFalseOrderByNome());
		}else {
			mv.addObject("medicos", serviceMedico.findByAtivoTrueOrderByNome());
		}
		mv.addObject("tiposRecebimento", TipoRecebimentoProducao.values());
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
			result.rejectValue("registro", e.getMessage(), e.getMessage());
			return novo(cooperado);
		}
		attributes.addFlashAttribute("mensagem", "Cooperado n° " + cooperado.getRegistro() + " salvo com sucesso");
		return new ModelAndView("redirect:/cooperado/cooperado/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CooperadoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/PesquisarCooperados");
		mv.addObject("medicos", serviceMedico.findByAtivoTrueOrderByNome());

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
		cooperado = service.findOne(id);
		ModelAndView mv = novo(cooperado);
		mv.addObject(cooperado);
		return mv;
	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable long id, Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/DetalheCooperado");
		cooperado = service.findOne(id);
		mv.addObject(cooperado);
		return mv;
	}
	

}
