package com.ajeff.simed.financeiro.controller;

import java.util.HashMap;
import java.util.Map;

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

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.repository.PlanosContaSecundariaRepository;
import com.ajeff.simed.financeiro.repository.filter.PlanoContaFilter;
import com.ajeff.simed.financeiro.service.PlanoContaService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/financeiro/planoConta")
public class PlanoContaController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PlanoContaController.class);
	
	@Autowired
	private PlanoContaService service;
	@Autowired
	private PlanosContaSecundariaRepository planosContaSecundariaRepository;

				
	@GetMapping("/novo")
	public ModelAndView novo(PlanoConta planoConta) {
		ModelAndView mv = new ModelAndView("Financeiro/planoConta/CadastroPlanoConta");
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid PlanoConta planoConta, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return novo(planoConta);
		}
		
		try {
			service.salvar(planoConta);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(planoConta);
		}
		attributes.addFlashAttribute("mensagem", "Plano de conta " + planoConta.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/financeiro/planoConta/novo");
	}

	
	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvarModal(@RequestBody @Valid PlanoConta planoConta, BindingResult result){
		if (result.hasErrors()){
			return ResponseEntity.badRequest().body(result.getFieldError().getDefaultMessage());
		}
		try {
			planoConta = service.salvar(planoConta);
		} catch (RegistroJaCadastradoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok(planoConta);
	}	

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PlanoContaFilter planoContaFilter, BindingResult result, @PageableDefault(size=20) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/planoConta/PesquisarPlanosConta");
		PageWrapper<PlanoConta> paginaWrapper = new PageWrapper<>(service.filtrar(planoContaFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, PlanoConta planoConta) {
		ModelAndView mv = novo(planoConta);
		planoConta = service.findOne(id);
		mv.addObject(planoConta);
		return mv;
	}
	
	@GetMapping("/historico/{id}")
	public ModelAndView imprimirHistorico(@PathVariable Long id) {

		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		map.put("id_plano", id);
		return new ModelAndView("rel_PLANODECONTA_Historico", map);
	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, PlanoConta planoConta) {
		ModelAndView mv = new ModelAndView("Financeiro/planoConta/DetalhePlanoConta");
		planoConta = service.findOne(id);
		mv.addObject("secundarias", planosContaSecundariaRepository.findByPlanoContaId(planoConta.getId()));
		mv.addObject(planoConta);
		return mv;
	}	
	
	
}
