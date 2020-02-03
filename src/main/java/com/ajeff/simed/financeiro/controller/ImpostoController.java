package com.ajeff.simed.financeiro.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.ImpostoService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;

@Controller
@RequestMapping("/financeiro/imposto")
public class ImpostoController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ImpostoController.class);

	@Autowired
	private ImpostoService service;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private ContaPagarService contaPagarService;
	

	@GetMapping("/gerar/{id}")
	public ModelAndView abreGerar(@PathVariable Long id, Imposto imposto, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/imposto/GerarImposto");
		imposto = service.findOne(id);
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject(imposto);
		return mv;
	}
	
	@PutMapping("/cancelarGerar")
	@ResponseStatus(HttpStatus.OK)
	public void cancelarAutorizarPagamento(@RequestParam Long id) {
		Imposto imposto = service.findOne(id);
		service.cancelarGerar(imposto);
	}	
	

	
	@PostMapping("/gerar/{id}")
	public ModelAndView gerarImposto(@PathVariable Long id, @Valid Imposto imposto, 
			 @AuthenticationPrincipal UsuarioSistema usuarioSistema, BindingResult result, RedirectAttributes attributes) {
		
		if (result.hasErrors()) {
			return abreGerar(id, imposto, usuarioSistema);
		}
		try {
			service.gerar(imposto);
		} catch (PagamentoNaoEfetuadoException e) {
			result.rejectValue("valor", e.getMessage(), e.getMessage());
			return abreGerar(id, imposto, usuarioSistema);
		} catch (VencimentoMenorEmissaoException e) {
			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
			return abreGerar(id, imposto, usuarioSistema);
		}
		return new ModelAndView("redirect:/financeiro/imposto/pesquisar");
	}

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(ImpostoFilter impostoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/imposto/PesquisarImpostos");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		PageWrapper<Imposto> paginaWrapper = new PageWrapper<>(service.filtrar(impostoFilter, pageable), httpServletRequest);
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
	

	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, Imposto imposto) {
		ModelAndView mv = new ModelAndView("Financeiro/imposto/DetalheImposto");
		imposto = service.findOne(id);
		mv.addObject("contas", contaPagarService.findOne(imposto.getContaPagar().getId()));
		mv.addObject(imposto);
		return mv;
	}
	
	
}
