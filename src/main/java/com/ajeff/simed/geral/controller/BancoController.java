package com.ajeff.simed.geral.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.filter.BancoFilter;
import com.ajeff.simed.geral.service.BancoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/financeiro/banco")
public class BancoController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(BancoController.class);
	
	@Autowired
	private BancoService service;

	
	@GetMapping("/novo")
	public ModelAndView novo(Banco banco) {
		ModelAndView mv = new ModelAndView("Financeiro/banco/CadastroBanco");
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Banco banco, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(banco);
		}
		
		try {
			service.salvar(banco);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(banco);
		}
		attributes.addFlashAttribute("mensagem", "Banco " + banco.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/financeiro/banco/novo");
	}

	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvarModal(@RequestBody @Valid Banco banco, BindingResult result){
		if (result.hasErrors()){
			return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
		}
		try {
			banco = service.salvar(banco);
		} catch (RegistroJaCadastradoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(banco);
	}	

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(BancoFilter bancoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/banco/PesquisarBancos");
		PageWrapper<Banco> paginaWrapper = new PageWrapper<>(service.filtrar(bancoFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, Banco banco) {
		ModelAndView mv = novo(banco);
		banco = service.findOne(id);
		mv.addObject(banco);
		return mv;
	}
	
}
