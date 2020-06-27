package com.ajeff.simed.cooperado.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Candidato;
import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.model.enums.TipoCargoDiretor;
import com.ajeff.simed.cooperado.model.enums.TipoDiretor;
import com.ajeff.simed.cooperado.service.CandidatoService;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.cooperado.service.DiretoriaService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/cooperado/diretoria/candidato")
public class CandidatoController {
	
	@Autowired
	private CandidatoService service;
	@Autowired
	private DiretoriaService diretoriaService;
	@Autowired
	private CooperadoService cooperadoService;
	

	
	@GetMapping(value = "/novo/{idDiretoria}")
	public ModelAndView novo(@PathVariable("idDiretoria") Long id, Candidato candidato){
		ModelAndView mv = new ModelAndView("Cooperado/candidato/CadastroCandidatos");
		mv.addObject(diretoriaService.findOne(id));
		mv.addObject("idDiretoria", id);
		mv.addObject("tipos", TipoDiretor.values());
		mv.addObject("cargos", TipoCargoDiretor.values());
		mv.addObject("cooperados", cooperadoService.findAllAtivoTrueOrderByMedicoNome());
		mv.addObject("candidatos", service.findAll());
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d+}"})
	public ModelAndView cadastrar(@Valid Candidato candidato, @Valid @RequestParam("idDiretoria") Long id, BindingResult result, RedirectAttributes attributes) {
		
		Diretoria diretoria = diretoriaService.findOne(id);
		
		if (result.hasErrors()) {
			return novo(id, candidato);
		}
		
		try {
			candidato.setDiretoria(diretoria);
			service.salvar(candidato);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("conta", e.getMessage(), e.getMessage());
			return novo(id, candidato);
		}		
		attributes.addFlashAttribute("mensagem", "Salvo com sucesso!");
		return new ModelAndView("redirect:/cooperado/diretoria/candidato/novo/" + id);
	}
//	
//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Long id){
//		try {
//			service.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
//	
//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, ContaEmpresa contaEmpresa){
//		contaEmpresa = service.findOne(id);
//		ModelAndView mv = nova(id, contaEmpresa);
//		mv.addObject(contaEmpresa);
//		mv.addObject("idEmpresa", contaEmpresa.getEmpresa().getId());	
//		return mv;
//	}
//	
//
//	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//	public @ResponseBody List<ContaEmpresa> pesquisarPorCodigoEmpresa(@RequestParam(name = "empresa", defaultValue = "-1") Long codigoEmpresa){
//		return service.findByEmpresaIdAtivo(codigoEmpresa);
//	}
	
}
