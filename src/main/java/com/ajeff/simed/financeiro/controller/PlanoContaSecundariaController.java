package com.ajeff.simed.financeiro.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.service.PlanoContaSecundariaService;
import com.ajeff.simed.financeiro.service.PlanoContaService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/financeiro/planoContaSecundaria")
public class PlanoContaSecundariaController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PlanoContaSecundariaController.class);
	
	@Autowired
	private PlanoContaSecundariaService service;
	@Autowired
	private PlanoContaService planoContaService;

				
	@GetMapping(value = "/nova/{idPlanoConta}")
	public ModelAndView nova(@PathVariable("idPlanoConta") Long idPlanoConta, @ModelAttribute("planoContaSecundaria") PlanoContaSecundaria planoContaSecundaria){
		ModelAndView mv = new ModelAndView("/Financeiro/planoConta/CadastroContaSecundaria");
		mv.addObject("idPlanoConta", idPlanoConta);
		mv.addObject("planoConta", planoContaService.findOne(idPlanoConta));
		mv.addObject("contasSecundarias", service.findByPlanoContaId(idPlanoConta));
		return mv;
	}
	

	@PostMapping(value = {"/nova", "{\\d+}"})
	public ModelAndView cadastrar(@Valid PlanoContaSecundaria planoContaSecundaria, @RequestParam("idPlanoConta") Long id, BindingResult result, RedirectAttributes attributes) {
		PlanoConta planoConta = planoContaService.findOne(id);
		if(result.hasErrors()) {
			return nova(id, planoContaSecundaria);
		}
		try {
			planoContaSecundaria.setPlanoConta(planoConta);
			service.salvar(planoContaSecundaria);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(id, planoContaSecundaria);
		}
		attributes.addFlashAttribute("mensagem", "Conta secundária " + planoContaSecundaria.getNome() + " salva com sucesso");
		return new ModelAndView("redirect:/financeiro/planoContaSecundaria/nova/" + planoConta.getId());
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
	public ModelAndView alterar (@PathVariable Long id, PlanoContaSecundaria planoContaSecundaria) {
		ModelAndView mv = nova(id, planoContaSecundaria);
		planoContaSecundaria = service.findOne(id);
		mv.addObject(planoContaSecundaria);
		mv.addObject("idPlanoConta", planoContaSecundaria.getPlanoConta().getId());	
		return mv;
	}
	
	
	
	//simed/financeiro/planoContaSecundaria?planoConta=1
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<PlanoContaSecundaria> pesquisarPorCodigoPlanoConta(@RequestParam(name = "planoConta", defaultValue = "-1") Long idPlanoConta){
		return service.findByPlanoContaId(idPlanoConta);
	}
	
}
