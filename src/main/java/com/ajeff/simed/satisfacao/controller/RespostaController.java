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
import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.repository.filter.RespostaFilter;
import com.ajeff.simed.satisfacao.service.RespostaService;

@Controller
@RequestMapping("/satisfacao/resposta")
public class RespostaController {

	
	@Autowired
	private RespostaService service;

	
	@GetMapping("/novo")
	public ModelAndView novo(Resposta resposta) {
		ModelAndView mv = new ModelAndView("Satisfacao/resposta/CadastroResposta");
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Resposta resposta, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(resposta);
		}
		
		try {
			service.salvar(resposta);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(resposta);
		}
		attributes.addFlashAttribute("mensagem", "Resposta " + resposta.getNome() + " salva com sucesso");
		return new ModelAndView("redirect:/satisfacao/resposta/novo");
	}

	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(RespostaFilter respostaFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Satisfacao/resposta/PesquisarRespostas");
		PageWrapper<Resposta> paginaWrapper = new PageWrapper<>(service.filtrar(respostaFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, Resposta resposta) {
		ModelAndView mv = novo(resposta);
		resposta = service.findOne(id);
		mv.addObject(resposta);
		return mv;
	}
	
}
