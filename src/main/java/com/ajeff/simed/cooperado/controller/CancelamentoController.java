package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.model.enums.TipoDescooperar;
import com.ajeff.simed.cooperado.model.enums.TipoRecebimentoProducao;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;
import com.ajeff.simed.cooperado.service.CancelamentoService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.DataAtualPosteriorDataReferenciaException;

@Controller
@RequestMapping("/cooperado/cooperado/cancelamento")
public class CancelamentoController {

	@Autowired
	private CancelamentoService service;

	
	
	@GetMapping("/descooperar/{id}")
	public ModelAndView descooperar(@PathVariable Long id, Cooperado cooperado) {
		cooperado = service.findOne(id);
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/CadastroCancelamento");
		mv.addObject(cooperado);
		mv.addObject("tipos", TipoDescooperar.values());
		return mv;
	}
	
	

	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Cooperado cooperado) {
		
		try {
//			cooperado = service.findOne(id);
			service.descooperar(cooperado);
			
		} catch (DataAtualPosteriorDataReferenciaException e) {
			e.printStackTrace();
		}
		return new ModelAndView("redirect:/cooperado/cooperado/pesquisar");
	}
	

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CancelamentoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/PesquisarCancelamentos");
		mv.addObject("medicos", service.findByAtivoFalseOrderByRegistro());
		PageWrapper<Cooperado> paginaWrapper = new PageWrapper<>(service.filtrar(cooperadoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
//		try {
//			service.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
	
	
//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, Cooperado cooperado) {
//		cooperado = service.findOne(id);
//		ModelAndView mv = novo(cooperado);
//		mv.addObject(cooperado);
//		return mv;
//	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable long id, Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/DetalheCooperado");
		cooperado = service.findOne(id);
		mv.addObject(cooperado);
		return mv;
	}
	

}
