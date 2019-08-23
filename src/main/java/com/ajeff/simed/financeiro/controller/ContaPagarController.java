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

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.repository.ImpostosRepository;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.PlanoContaService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
@RequestMapping("/financeiro/contaPagar")
public class ContaPagarController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ContaPagarController.class);

	@Autowired
	private ContaPagarService service;
	@Autowired
	private PlanoContaService planoContaService;
	@Autowired
	private ImpostosRepository impostoRepository;
	@Autowired
	private UsuarioService usuarioService;
	
	

	@GetMapping("/nova")
	public ModelAndView nova(ContaPagar contaPagar, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/CadastroContaPagar");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("planosConta", planoContaService.listarTodosPlanosContaDebito());
		return mv;
	}
	
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView nova(@Valid ContaPagar contaPagar, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if (result.hasErrors()) {
			return nova(contaPagar, usuarioSistema);
		}
		try {
			service.salvar(contaPagar);
		} catch (Exception e) {
			result.rejectValue("documento", e.getMessage(), e.getMessage());
			return nova(contaPagar, usuarioSistema);
		}
		
		attributes.addFlashAttribute("mensagem", "Conta a pagar "+ contaPagar.getDocumento()+" salva com sucesso");
		return new ModelAndView("redirect:/financeiro/contaPagar/nova");
	}
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(ContaPagarFilter contaPagarFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/PesquisarContasPagar");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("total", service.total(contaPagarFilter));
		
		PageWrapper<ContaPagar> paginaWrapper = new PageWrapper<>(service.filtrar(contaPagarFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, ContaPagar contaPagar, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		contaPagar = service.buscarComPlanoConta(id);
		ModelAndView mv = nova(contaPagar, usuarioSistema);
		contaPagar = service.findOne(id);
		mv.addObject(contaPagar);
		return mv;
	}

	
	@GetMapping("/detalheConta/{id}")
	public ModelAndView detalhe(@PathVariable Long id, ContaPagar contaPagar) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/DetalheContaPagar");
		contaPagar = service.findOne(id);
		mv.addObject("impostos", impostoRepository.findByContaPagarOrigem(contaPagar));
		mv.addObject(contaPagar);
		return mv;
	}

	

	/*
	 * Autorização da conta a pagar para pagamento
	 */
	@PutMapping("/autorizar")
	@ResponseStatus(HttpStatus.OK)
	public void autorizarPagamento(@RequestParam Long id) {
		ContaPagar contaPagar = service.findOne(id);
		service.autorizarPagamento(contaPagar);
	}	
	
	

	
	/*
	 * Cancelar a autorização da conta a pagar para pagamento
	 */
	@PutMapping("/cancelarAutorizar")
	@ResponseStatus(HttpStatus.OK)
	public void cancelarAutorizarPagamento(@RequestParam Long id) {
		ContaPagar contaPagar = service.findOne(id);
		service.cancelarAutorizarPagamento(contaPagar);
	}	

	@GetMapping("/imprimirDetalhe/{id}")
	public ModelAndView imprimirDetalhe(@PathVariable Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		map.put("conta_pagar", id);
		return new ModelAndView("rel_CONTAPAGAR_DetalheConta", map);
	}
}