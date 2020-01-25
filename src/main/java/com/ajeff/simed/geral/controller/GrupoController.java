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
import com.ajeff.simed.geral.model.Grupo;
import com.ajeff.simed.geral.repository.filter.GrupoFilter;
import com.ajeff.simed.geral.service.GrupoService;
import com.ajeff.simed.geral.service.PermissaoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/grupo")
public class GrupoController {
	
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(GrupoController.class);

	@Autowired
	private GrupoService service;
	@Autowired
	private PermissaoService permissaoService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Grupo grupo) {
		ModelAndView mv = new ModelAndView("Geral/grupo/CadastroGrupo");
		mv.addObject("permissoes1", permissaoService.buscarTodasPermissoes());
		return mv;
	}


	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Grupo grupo, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(grupo);
		}
		
		try {
			service.salvar(grupo);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(grupo);
		}
		attributes.addFlashAttribute("mensagem", "Grupo " + grupo.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/grupo/novo");
	}

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(GrupoFilter grupoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Geral/grupo/PesquisarGrupos");
		PageWrapper<Grupo> paginaWrapper = new PageWrapper<>(service.filtrar(grupoFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id) {
		Grupo grupo = service.buscarGrupoComPermissoes(id);
		ModelAndView mv = novo(grupo);
		mv.addObject(grupo);
		return mv;
	}
	
}
