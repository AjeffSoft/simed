package com.ajeff.simed.cooperado.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.model.enums.TipoAssembleia;
import com.ajeff.simed.cooperado.service.AssembleiaService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/cooperado/assembleia")
public class AssembleiaController {

	@Autowired
	private AssembleiaService service;


	
	@GetMapping("/nova")
	public ModelAndView novo(Assembleia assembleia) {
		ModelAndView mv = new ModelAndView("Cooperado/assembleia/CadastroAssembleia");
		mv.addObject("tipos", TipoAssembleia.values());
		return mv;
	}
	
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView salvar(@Valid Assembleia assembleia, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(assembleia);
		}
		
		try {
			service.salvar(assembleia);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(assembleia);
		}
		attributes.addFlashAttribute("mensagem", "Assembléia cadastrada com sucesso");
		return new ModelAndView("redirect:/cooperado/assembleia/presentes/novo");
	}
	

//	@GetMapping("/pesquisar")
//	public ModelAndView pesquisar(MedicoFilter medicoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
//										HttpServletRequest httpServletRequest) {
//		ModelAndView mv = new ModelAndView("Cooperado/medico/PesquisarMedicos");
//
//		PageWrapper<Medico> paginaWrapper = new PageWrapper<>(service.filtrar(medicoFilter, pageable), httpServletRequest);
//		mv.addObject("pagina", paginaWrapper);
//		return mv;
//	}	
//
//	
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
