package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;
import com.ajeff.simed.cooperado.service.CancelamentoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;

@Controller
@RequestMapping("/cooperado/cooperado/cancelamento")
public class CancelamentoController {

	@Autowired
	private CancelamentoService service;
//	@Autowired
//	private PessoaService serviceMedico;

	
//	@GetMapping("/novo")
//	public ModelAndView novo(Cooperado cooperado) {
//		ModelAndView mv = new ModelAndView("Cooperado/cooperado/CadastroCooperado");
//		
//		if(cooperado.isNovo()) {
//			mv.addObject("medicos", serviceMedico.findByAtivoFalseOrderByNome());
//		}else {
//			mv.addObject("medicos", serviceMedico.findByAtivoTrueOrderByNome());
//		}
//		mv.addObject("tiposRecebimento", TipoRecebimentoProducao.values());
//		return mv;
//	}
//	
//	
//	@PostMapping(value = {"/novo", "{\\d}"})
//	public ModelAndView salvar(@Valid Cooperado cooperado, BindingResult result, RedirectAttributes attributes) {
//		if (result.hasErrors()) {
//			return novo(cooperado);
//		}
//		
//		try {
//			service.salvar(cooperado);
//		} catch (RegistroJaCadastradoException e) {
//			result.rejectValue("registro", e.getMessage(), e.getMessage());
//			return novo(cooperado);
//		}
//		attributes.addFlashAttribute("mensagem", "Cooperado n° " + cooperado.getRegistro() + " salvo com sucesso");
//		return new ModelAndView("redirect:/cooperado/cooperado/novo");
//	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CancelamentoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cancelamento/PesquisarDescooperados");
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
