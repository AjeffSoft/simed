package com.ajeff.simed.financeiro.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.hibernate.TransientObjectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.PlanoContaService;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;

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
	private EmpresaService empresaService;
	
	

	@GetMapping("/nova")
	public ModelAndView nova(ContaPagar contaPagar, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/CadastroContaPagar");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
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
		} catch (TransientObjectException e) {
			result.rejectValue("fornecedor", e.getMessage(), e.getMessage());
			return nova(contaPagar, usuarioSistema);
		} catch (DocumentoEFornecedorJaCadastradoException e) {
			result.rejectValue("notaFiscal", e.getMessage(), e.getMessage());
			return nova(contaPagar, usuarioSistema);
		} catch (VencimentoMenorEmissaoException e) {
			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
			return nova(contaPagar, usuarioSistema);
		} catch (RegistroNaoCadastradoException e) {
			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
			return nova(contaPagar, usuarioSistema);
		}
		attributes.addFlashAttribute("mensagem", "Conta a pagar "+ contaPagar.getDocumento()+" salva com sucesso");
		return new ModelAndView("redirect:/financeiro/contaPagar/nova");
	}
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(ContaPagarFilter contaPagarFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/PesquisarContasPagar");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
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
		mv.addObject(contaPagar);
		return mv;
	}
	
	@GetMapping("/pendencia/{id}")
	public ModelAndView alterar (@PathVariable Long id, ContaPagar contaPagar) {
		contaPagar = service.findOne(id);
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/AlterarPendenciaContaPagar");
		mv.addObject(contaPagar);
		return mv;
	}	

	
//	@PutMapping("/autorizar")
//	@ResponseStatus(HttpStatus.OK)
//	public void autorizarPagamento(@RequestParam Long id) {
//		ContaPagar contaPagar = service.findOne(id);
//		service.autorizarPagamento(contaPagar);
//	}	

//	
//	@PutMapping("/cancelarAutorizar")
//	@ResponseStatus(HttpStatus.OK)
//	public void cancelarAutorizarPagamento(@RequestParam Long id) {
//		ContaPagar contaPagar = service.findOne(id);
//		service.cancelarAutorizarPagamento(contaPagar);
//	}	
	
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, ContaPagar contaPagar) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/DetalheContaPagar");
		contaPagar = service.findOne(id);
//		mv.addObject("impostos", impostoRepository.findByContaPagarOrigem(contaPagar));
		mv.addObject(contaPagar);
		return mv;
	}

	


	@GetMapping("/imprimirDetalhe/{id}")
	public ModelAndView imprimirDetalhe(@PathVariable Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		map.put("conta_pagar", id);
		return new ModelAndView("rel_CONTAPAGAR_DetalheConta", map);
	}
}