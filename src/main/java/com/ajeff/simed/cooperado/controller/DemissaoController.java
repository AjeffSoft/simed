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

import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.model.enums.TipoDemissaoCooperado;
import com.ajeff.simed.cooperado.repository.filter.DemissaoFilter;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.cooperado.service.DemissaoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.DataAtualPosteriorDataReferenciaException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/cooperado/demissao")
public class DemissaoController {

	@Autowired
	private DemissaoService service;
	@Autowired
	private CooperadoService admissaoService;

	
	@GetMapping("/novo")
	public ModelAndView novo(DemissaoCooperado demissao) {
		ModelAndView mv = new ModelAndView("Cooperado/demissao/CadastroDemissao");
		mv.addObject("admissoes", admissaoService.findByAdmissaoCooperadoAtivoTrue());
		mv.addObject("tiposDemissao", TipoDemissaoCooperado.values());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid DemissaoCooperado demissao, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(demissao);
		}
		try {
			service.salvar(demissao);
		} catch (DataAtualPosteriorDataReferenciaException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(demissao);
		}
		attributes.addFlashAttribute("mensagem", "Demissao de: " + demissao.getAdmissao().getCooperado().getNome() + " realizado com sucesso");
		return new ModelAndView("redirect:/cooperado/demissao/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(DemissaoFilter demissaoFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/demissao/PesquisarDemissoes");
		mv.addObject("admissoes", admissaoService.findByAdmissaoCooperadoAtivoFalse());

		PageWrapper<DemissaoCooperado> paginaWrapper = new PageWrapper<>(service.filtrar(demissaoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
}
