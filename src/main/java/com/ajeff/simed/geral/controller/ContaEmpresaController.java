package com.ajeff.simed.geral.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/contaEmpresa")
public class ContaEmpresaController {
	
	@Autowired
	private ContaEmpresaService service;
	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private EmpresaService empresaService;

	
	@GetMapping(value = "/nova/{idEmpresa}")
	public ModelAndView nova(@PathVariable("idEmpresa") Long idEmpresa, @ModelAttribute("contaEmpresa") ContaEmpresa contaEmpresa){
		ModelAndView mv = new ModelAndView("contaEmpresa/CadastroContaEmpresa");
		mv.addObject("agencias", agenciaService.findAllOrderByAgencia());
		mv.addObject("idEmpresa", idEmpresa);
		mv.addObject("empresa", empresaService.findOne(idEmpresa));
		mv.addObject("contas", service.findByEmpresaId(idEmpresa));
		return mv;
	}

	
	@PostMapping(value = {"/nova", "{\\d+}"})
	public ModelAndView cadastrar(@Valid ContaEmpresa contaEmpresa, @Valid @RequestParam("idEmpresa") Long id, BindingResult result, RedirectAttributes attributes) {
		
		Empresa empresa = empresaService.findOne(id);
		
		if (result.hasErrors()) {
			return nova(id, contaEmpresa);
		}
		
		try {
			contaEmpresa.setEmpresa(empresa);
			service.salvar(contaEmpresa);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("conta", e.getMessage(), e.getMessage());
			return nova(id, contaEmpresa);
		}		
		attributes.addFlashAttribute("mensagem", "Conta bancária nº " + contaEmpresa.getConta() + " salva com sucesso!");
		return new ModelAndView("redirect:/contaEmpresa/nova/" + empresa.getId());
	}
	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Long id){
		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable Long id, ContaEmpresa contaEmpresa){
		contaEmpresa = service.findOne(id);
		ModelAndView mv = nova(id, contaEmpresa);
		mv.addObject(contaEmpresa);
		mv.addObject("idEmpresa", contaEmpresa.getEmpresa().getId());	
		return mv;
	}
	

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ContaEmpresa> pesquisarPorCodigoEmpresa(@RequestParam(name = "empresa", defaultValue = "-1") Long codigoEmpresa){
		return service.findByEmpresaId(codigoEmpresa);
	}
	
}
