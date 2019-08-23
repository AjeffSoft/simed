package com.ajeff.simed.geral.controller;

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
import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.repository.filter.AgenciaFilter;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.BancoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/financeiro/agencia")
public class AgenciaController {

	@Autowired
	private BancoService bancoService;
	@Autowired
	private AgenciaService service;
	
	@GetMapping("/nova")
	public ModelAndView nova(Agencia agencia) {
		ModelAndView mv = new ModelAndView("Financeiro/agencia/CadastroAgencia");
		mv.addObject("bancos", bancoService.findByNomeOrderByNomeAsc());
		return mv;
	}
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView salvar(@Valid Agencia agencia, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return nova(agencia);
		}
		
		try {
			service.salvar(agencia);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("agencia", e.getMessage(), e.getMessage());
			return nova(agencia);
		}
		attributes.addFlashAttribute("mensagem", "Agencia " + agencia.getAgencia() + " salva com sucesso");
		return new ModelAndView("redirect:/financeiro/agencia/nova");
	}
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(AgenciaFilter agenciaFilter, BindingResult result, @PageableDefault(size=20) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/agencia/PesquisarAgencias");

		mv.addObject("bancos", bancoService.findByNomeOrderByNomeAsc());
		
		PageWrapper<Agencia> paginaWrapper = new PageWrapper<>(service.filtrar(agenciaFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, Agencia agencia) {
		ModelAndView mv = nova(agencia);
		agencia = service.findOne(id);
		mv.addObject(agencia);
		return mv;
	}	
	
}
