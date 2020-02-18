package com.ajeff.simed.satisfacao.controller;

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

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.filter.PerguntaFilter;
import com.ajeff.simed.satisfacao.service.PerguntaService;

@Controller
@RequestMapping("/satisfacao/pergunta")
public class PerguntaController {

	
	@Autowired
	private PerguntaService service;

	
	@GetMapping("/novo")
	public ModelAndView novo(Pergunta pergunta) {
		ModelAndView mv = new ModelAndView("Satisfacao/pergunta/CadastroPergunta");
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Pergunta pergunta, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(pergunta);
		}
		
		try {
			service.salvar(pergunta);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(pergunta);
		}
		attributes.addFlashAttribute("mensagem", "Pergunta " + pergunta.getNome() + " salva com sucesso");
		return new ModelAndView("redirect:/satisfacao/pergunta/novo");
	}

	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PerguntaFilter perguntaFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Satisfacao/pergunta/PesquisarPerguntas");
		PageWrapper<Pergunta> paginaWrapper = new PageWrapper<>(service.filtrar(perguntaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long id){

		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}

	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar (@PathVariable Long id, Pergunta pergunta) {
		ModelAndView mv = novo(pergunta);
		pergunta = service.findOne(id);
		mv.addObject(pergunta);
		return mv;
	}
	
}
