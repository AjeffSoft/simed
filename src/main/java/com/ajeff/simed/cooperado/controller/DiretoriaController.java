package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.repository.filter.DiretoriaFilter;
import com.ajeff.simed.cooperado.service.DiretoriaService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;

@Controller
@RequestMapping("/cooperado/diretoria")
public class DiretoriaController {

	@Autowired
	private DiretoriaService service;



	
	@GetMapping("/nova")
	public ModelAndView novo(Diretoria diretoria) {
		return new ModelAndView("Cooperado/diretoria/CadastroDiretoria");
	}
	
	
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView salvar(@Valid Diretoria diretoria, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(diretoria);
		}
		
		try {
			service.salvar(diretoria);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("chapa", e.getMessage(), e.getMessage());
			return novo(diretoria);
		}
		attributes.addFlashAttribute("mensagem", "Diretoria cadastrada com sucesso");
		return new ModelAndView("redirect:/cooperado/diretoria/pesquisar");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(DiretoriaFilter diretoriaFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/diretoria/PesquisarDiretorias");
		PageWrapper<Diretoria> paginaWrapper = new PageWrapper<>(service.filtrar(diretoriaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	

	
//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
//		try {
//			pessoaService.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}	
//	
//
//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, Medico medico) {
//		medico = (Medico) pessoaService.buscarComCidadeEstado(id);
//		ModelAndView mv = novo(medico);
//		mv.addObject(medico);
//		return mv;
//	}
//	
//	@GetMapping("/detalhe/{id}")
//	public ModelAndView detalhe(@PathVariable Long id) {
//		Pessoa medico = new Medico();
//		ModelAndView mv = new ModelAndView("Cooperado/medico/DetalheMedico");
//		medico = pessoaService.findOne(id);
//		mv.addObject(medico);
//		return mv;
//	}
//	
	
}
