package com.ajeff.simed.financeiro.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;
import com.ajeff.simed.financeiro.service.ExtratoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;

@Controller
@RequestMapping("/financeiro/extrato")
public class ExtratoController {

	@Autowired
	private ExtratoService service;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private EmpresaService empresaService;

	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(ExtratoFilter extratoFilter, BindingResult result, @PageableDefault(size=200) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/extrato/PesquisarExtratos");
		
		MovimentacaoItem movimentacao = service.setarMovimentacao(extratoFilter);
		extratoFilter.setMovimentacao(movimentacao);
		service.calcularSaldoExtrato(extratoFilter);
		PageWrapper<Extrato> paginaWrapper = new PageWrapper<>(service.filtrar(extratoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	
	
	
	
	@GetMapping("/selecionarConta")
	public ModelAndView selecionarConta(ContaEmpresa contaEmpresa, @AuthenticationPrincipal UsuarioSistema usuarioSistema, ExtratoFilter extratoFilter) {
		ModelAndView mv = new ModelAndView("Financeiro/extrato/SelecionarContaBancaria");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("contasBancarias", contaEmpresaService.listarTodosOrdenadoPorNome());
		return mv;
	}


}