package com.ajeff.simed.geral.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.ajeff.simed.geral.model.Permissao;
import com.ajeff.simed.geral.repository.filter.PermissaoFilter;
import com.ajeff.simed.geral.service.PermissaoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/permissao")
public class PermissaoController {
	
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PermissaoController.class);

	@Autowired
	private PermissaoService service;
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Permissao permissao) {
		ModelAndView mv = new ModelAndView("/Geral/permissao/CadastroPermissao");
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Permissao permissao, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(permissao);
		}
		
		try {
			service.salvar(permissao);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(permissao);
		}
		attributes.addFlashAttribute("mensagem", "Permissão " + permissao.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/permissao/novo");
	}

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PermissaoFilter permissaoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Geral/permissao/PesquisarPermissoes");
		PageWrapper<Permissao> paginaWrapper = new PageWrapper<>(service.filtrar(permissaoFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, Permissao permissao) {
		ModelAndView mv = novo(permissao);
		permissao = service.findOne(id);
		mv.addObject(permissao);
		return mv;
	}
	
}
