package com.ajeff.simed.financeiro.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.financeiro.model.ExtratoBancario;
import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;
import com.ajeff.simed.financeiro.service.ExtratoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
@RequestMapping("/financeiro/extrato")
public class ExtratoController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ExtratoController.class);

	@Autowired
	private ExtratoService service;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private UsuarioService usuarioService;

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(ExtratoFilter extratoFilter, BindingResult result, @PageableDefault(size=80) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/extrato/PesquisarExtratos");
		
		extratoFilter.setEmpresa(extratoFilter.getContaEmpresa().getEmpresa());
		MovimentacaoBancaria movimentacao = service.setarMovimentacao(extratoFilter);
		extratoFilter.setMovimentacao(movimentacao);
		service.exibirSaldo(extratoFilter);
		PageWrapper<ExtratoBancario> paginaWrapper = new PageWrapper<>(service.filtrar(extratoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	
	
	
	
	@GetMapping("/selecionarConta")
	public ModelAndView selecionarConta(ContaEmpresa contaEmpresa, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ExtratoFilter extratoFilter) {
		ModelAndView mv = new ModelAndView("Financeiro/extrato/SelecionarContaBancaria");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("contasBancarias", contaEmpresaService.listarTodosOrdenadoPorNome());
		return mv;
	}


}