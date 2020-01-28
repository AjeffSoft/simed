package com.ajeff.simed.financeiro.controller;

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

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.repository.filter.ContaReceberFilter;
import com.ajeff.simed.financeiro.service.ContaReceberService;
import com.ajeff.simed.financeiro.service.PlanoContaService;
import com.ajeff.simed.financeiro.service.RecebimentoService;
import com.ajeff.simed.financeiro.service.exception.DocumentoEFornecedorJaCadastradoException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.financeiro.service.exception.VencimentoMenorEmissaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
@RequestMapping("/financeiro/contaReceber")
public class ContaReceberController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ContaReceberController.class);

	@Autowired
	private ContaReceberService service;
	@Autowired
	private PlanoContaService planoContaService;
	@Autowired
	private RecebimentoService recebimentoService;
	@Autowired
	private EmpresaService empresaService;	
	

	@GetMapping("/nova")
	public ModelAndView nova(ContaReceber contaReceber, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaReceber/CadastroContaReceber");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("planosConta", planoContaService.listarTodosPlanosContaCredito());
		return mv;
	}
	
	
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, ContaReceber contaReceber) {
		ModelAndView mv = new ModelAndView("Financeiro/contaReceber/DetalheContaReceber");
		mv.addObject("recebimentos", recebimentoService.findByContaReceber(contaReceber));
		contaReceber = service.findOne(id);
		mv.addObject(contaReceber);
		return mv;
	}

	
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView nova(@Valid ContaReceber contaReceber, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if (result.hasErrors()) {
			return nova(contaReceber, usuarioSistema);
		}
		try {
			service.salvar(contaReceber);
		} catch (TransientObjectException e) {
			result.rejectValue("fornecedor", e.getMessage(), e.getMessage());
			return nova(contaReceber, usuarioSistema);
		} catch (DocumentoEFornecedorJaCadastradoException e) {
			result.rejectValue("documento", e.getMessage(), e.getMessage());
			return nova(contaReceber, usuarioSistema);
		} catch (VencimentoMenorEmissaoException e) {
			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
			return nova(contaReceber, usuarioSistema);
		} catch (RegistroNaoCadastradoException e) {
			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
			return nova(contaReceber, usuarioSistema);
		}

		attributes.addFlashAttribute("mensagem", "Conta a receber salva com sucesso");
		return new ModelAndView("redirect:/financeiro/contaReceber/nova");
	}
	
	
	
	@GetMapping("/pesquisar")
	public ModelAndView listaCadastrar(ContaReceberFilter contaReceberFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaReceber/PesquisarContasReceber");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		
		mv.addObject("total", service.totalGeral(contaReceberFilter));
				
		PageWrapper<ContaReceber> paginaWrapper = new PageWrapper<>(service.filtrar(contaReceberFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar (@PathVariable Long id, ContaReceber contaReceber, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		contaReceber = service.buscarComPlanoConta(id);
		ModelAndView mv = nova(contaReceber, usuarioSistema);
		contaReceber = service.findOne(id);
		mv.addObject(contaReceber);
		return mv;
	}
	
}