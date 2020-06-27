package com.ajeff.simed.cooperado.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.cooperado.model.Candidato;
import com.ajeff.simed.cooperado.service.CandidatoService;
import com.ajeff.simed.cooperado.service.DiretoriaService;

@Controller
@RequestMapping("/cooperado/diretoria/candidato")
public class CandidatoController {
	
	@Autowired
	private CandidatoService service;
	@Autowired
	private DiretoriaService diretoriaService;


	
	@GetMapping(value = "/novo/{id}")
	public ModelAndView novo(@PathVariable("id") Long id, Candidato candidato){
		ModelAndView mv = new ModelAndView("Cooperado/candidato/CadastroCandidatos");
		mv.addObject(diretoriaService.findOne(id));
		return mv;
	}

	
//	@PostMapping(value = {"/nova", "{\\d+}"})
//	public ModelAndView cadastrar(@Valid ContaEmpresa contaEmpresa, @Valid @RequestParam("idEmpresa") Long id, BindingResult result, RedirectAttributes attributes) {
//		
//		Empresa empresa = empresaService.findOne(id);
//		
//		if (result.hasErrors()) {
//			return nova(id, contaEmpresa);
//		}
//		
//		try {
//			contaEmpresa.setEmpresa(empresa);
//			service.salvar(contaEmpresa);
//		} catch (RegistroJaCadastradoException e) {
//			result.rejectValue("conta", e.getMessage(), e.getMessage());
//			return nova(id, contaEmpresa);
//		}		
//		attributes.addFlashAttribute("mensagem", "Conta bancária nº " + contaEmpresa.getConta() + " salva com sucesso!");
//		return new ModelAndView("redirect:/contaEmpresa/nova/" + empresa.getId());
//	}
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
