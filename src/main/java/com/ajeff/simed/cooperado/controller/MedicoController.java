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

import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.filter.MedicoFilter;
import com.ajeff.simed.cooperado.service.MedicoService;
import com.ajeff.simed.financeiro.model.enums.Genero;
import com.ajeff.simed.financeiro.model.enums.TipoConta;
import com.ajeff.simed.financeiro.service.exception.CpfCnpjInvalidoException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.Pessoa;
import com.ajeff.simed.geral.model.enuns.EstadoCivil;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.EstadoService;
import com.ajeff.simed.geral.service.PessoaService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/cooperado/medico")
public class MedicoController {

	@Autowired
	private MedicoService service;
	@Autowired
	private PessoaService pessoaService;
	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private EstadoService estadoService;

	
	@GetMapping("/novo")
	public ModelAndView novo(Medico medico) {
		ModelAndView mv = new ModelAndView("Cooperado/medico/CadastroMedico");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		mv.addObject("contas", TipoConta.values());
		mv.addObject("generos", Genero.values());
		mv.addObject("estadoCivil", EstadoCivil.values());
		mv.addObject("agencias", agenciaService.findAllOrderByAgencia());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Medico medico, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(medico);
		}
		
		try {
			service.salvar(medico);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(medico);
		} catch (CpfCnpjInvalidoException e) {
			result.rejectValue("documento1", e.getMessage(), e.getMessage());
			return novo(medico);
		}
		attributes.addFlashAttribute("mensagem", "Médico(a) " + medico.getNome() + " cadastrado(a) com sucesso");
		return new ModelAndView("redirect:/cooperado/medico/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(MedicoFilter medicoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/medico/PesquisarMedicos");

		PageWrapper<Medico> paginaWrapper = new PageWrapper<>(service.filtrar(medicoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	

	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
		try {
			pessoaService.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}	
	

//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, Pessoa medico) {
//		medico = pessoaService.buscarComCidadeEstado(id);
//		ModelAndView mv = novo(medico);
//		mv.addObject(medico);
//		return mv;
//	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id) {
		Pessoa medico = new Medico();
		ModelAndView mv = new ModelAndView("Cooperado/medico/DetalheMedico");
		medico = pessoaService.findOne(id);
		mv.addObject(medico);
		return mv;
	}
	
	
}
