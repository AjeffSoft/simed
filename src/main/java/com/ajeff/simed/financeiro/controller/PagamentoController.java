package com.ajeff.simed.financeiro.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
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

import com.ajeff.simed.financeiro.dto.PeriodoRelatorio;
import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.PagamentoService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
@RequestMapping("/financeiro/pagamento")
public class PagamentoController {

	private static final Logger LOG = LoggerFactory.getLogger(PagamentoController.class);
	
	@Autowired
	private PagamentoService service;
	@Autowired
	private ContaPagarService contaService;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private UsuarioService usuarioService;

	
	
	@GetMapping("/novo")
	public ModelAndView novo(@RequestParam(value = "idConta", required = false) List<Long> ids,Pagamento pagamento) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/CadastroPagamento");
		mv.addObject("contasBancos", contaEmpresaService.buscarContaCorrenteBanco());
		List<ContaPagar> itens = service.listaDeContasSelecionadasParaPagamento(ids);
		
		if(itens.isEmpty()) {
			return new ModelAndView("ListaVaziaErro");		
		}else {
			mv.addObject("contasSelecionadas", itens);
			BigDecimal soma = itens.stream().map(c -> c.getValor()).reduce(BigDecimal.ZERO, BigDecimal::add);
			mv.addObject("soma", soma);
			return mv;		
		}
	}

	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Pagamento pagamento, @RequestParam(value = "idConta") List<Long> ids, BindingResult result) {
		if(result.hasErrors()) {
			return novo(ids, pagamento);
		}
		try {
			service.salvar(pagamento, ids);
		} catch (PeriodoMovimentacaoException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(ids, pagamento);
		} catch (PagamentoNaoEfetuadoException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(ids, pagamento);
		}

		if (pagamento.getUnico()) {
			return new ModelAndView("redirect:/financeiro/pagamento/comprovante/"+ pagamento.getId());
		}else {
			return new ModelAndView("redirect:/financeiro/pagamento/comprovante");
		}
	}	
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PagamentoFilter pagamentoFilter, BindingResult result, @PageableDefault(size=80) Pageable pageable,
			HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/PesquisarPagamentos");
		mv.addObject("contasBancos", contaEmpresaService.buscarContaCorrenteBanco());
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("total", service.totalGeral(pagamentoFilter));
		PageWrapper<Pagamento> paginaWrapper = new PageWrapper<>(service.filtrar(pagamentoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	
	@GetMapping("/autorizarConta")
	public ModelAndView autorizarConta(ContaPagarFilter contaPagarFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/AutorizarContaPagar");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		PageWrapper<ContaPagar> paginaWrapper = new PageWrapper<>(contaService.filtrarAutorizar(contaPagarFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	
	
	
	@GetMapping("/contasAutorizadas")
	public ModelAndView listaAutorizado(ContaPagar contaPagar) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/ListaContasAutorizadas");
		mv.addObject("contasAutorizadas", contaService.buscarTodasContasAutorizadas());
		return mv;
	}	
	
	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long id){

		try {
			service.excluir(id);
		} catch (PeriodoMovimentacaoException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, Pagamento pagamento) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/DetalhePagamento");
		pagamento = service.findOne(id);
		mv.addObject("contasPagar", contaService.findByPagamentoId(pagamento.getId()));
		mv.addObject(pagamento);
		return mv;
	}	
	
	
	@GetMapping("/pagar/{id}")
	public ModelAndView abrePagar(@PathVariable Long id, Pagamento pagamento) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/ConfirmarPagamento");
		pagamento = service.findOne(id);
		mv.addObject("contas", contaService.findByPagamentoId(pagamento.getId()));
		mv.addObject(pagamento);
		return mv;
	}	
	

	@PostMapping(value = {"/pagar/{\\d+}"})
	public ModelAndView pagar(@Valid Pagamento pagamento, BindingResult result) {

		if(result.hasErrors()) {
			return abrePagar(pagamento.getId(), pagamento);
		}
		
		try {
			service.pagar(pagamento);
		} catch (PagamentoNaoEfetuadoException e) {
			result.rejectValue("dataPago", e.getMessage(), e.getMessage());
			return abrePagar(pagamento.getId(), pagamento);
		} catch (PeriodoMovimentacaoException e) {
			result.rejectValue("dataPago", e.getMessage(), e.getMessage());
			return abrePagar(pagamento.getId(), pagamento);
		}		
		return new ModelAndView("redirect:/financeiro/pagamento/pesquisar?empresa="+pagamento.getEmpresa().getId()+"&status=EMITIDO");
	}
	
	
	@PutMapping("/cancelarConfirmacao")
	@ResponseStatus(HttpStatus.OK)
	public void cancelarConfirmacao(@RequestParam Long id) {
		Pagamento pagamento = service.findOne(id);
		service.cancelarConfirmacao(pagamento);
	}	


	@GetMapping("/comprovante")
	public ModelAndView comprovanteIndividual() {
		//TODO: Exibir a lista dos pagamentos individuais
		return new ModelAndView("Financeiro/pagamento/ComprovantePagamento");
	}
	
	
	@GetMapping("/comprovante/{id}")
	public ModelAndView comprovante(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/ComprovantePagamentoUnico");
		mv.addObject("pagamentos", service.findOne(id));
		return mv;
	}
	
	
	@GetMapping("/imprimirOrdemPagamento/{id}")
	public ModelAndView imprimirOrdemPagamento(@PathVariable Long id) {
		Pagamento pagamento = service.findOne(id);
		Map<String, Object> map = new HashMap<>();
		try {
			ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +pagamento.getContaEmpresa().getEmpresa().getId()+".jpg"));
			map.put("format", "pdf");
			map.put("logo", gto.getImage());
			map.put("id_pagamento", id);
			
			//OPÇÃO DE VISUALIZAÇÃO DO RELATÓRIO DEPENDENDO DO TIPO DO PAGAMENTO
			if(pagamento.getTipo().equals("DEBITO")) {
				return new ModelAndView("rel_PAGAMENTO_OrdemPagamento", map);
			}else if(pagamento.getTipo().equals("TRANSFERENCIA"))   {
				return new ModelAndView("rel_PAGAMENTO_OrdemTransferencia", map);
			}else {
				return null;
			}
		} catch (Exception e) {
			LOG.error("ERRO: OCORREU UM ERRO AO ABRIR O RELATÓRIO - A LOGO DA EMPRESA NO RELATÓRIO NÃO EXISTE : " + e.getMessage());
			return new ModelAndView("/ErroRelatorio");
		}
	}
	
	
	@GetMapping("/imprimirDetalhe/{id}")
	public ModelAndView imprimirDetalhe(@PathVariable Long id) {
		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		map.put("id_pagamento", id);
		return new ModelAndView("rel_PAGAMENTO_Detalhe", map);
	}
	
	
	
	//IMPRIMIR RELATÓRIOS DE PAGAMENTOS
	@GetMapping("/relatorio")
	public ModelAndView abrirRelatorio(){
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/RelatorioPagamento");
		mv.addObject(new PeriodoRelatorio());
		mv.addObject("empresas", empresaService.buscarTodos());
		return mv;
	}	
	
	
	//IMPRIMIR RELATÓRIOS DE PAGAMENTOS
	@PostMapping("/relatorio")
	public ModelAndView gerarRelatorio(PeriodoRelatorio periodoRelatorio){
		Map<String, Object> map = new HashMap<>();
		

		try {
			ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +periodoRelatorio.getEmpresa().getId()+".jpg"));
			map.put("logo", gto.getImage());
			map.put("format", "pdf");
		} catch (Exception e) {
			LOG.error("ERRO: OCORREU UM ERRO AO ABRIR O RELATÓRIO - A LOGO DA EMPRESA NO RELATÓRIO NÃO EXISTE : " + e.getMessage());
			return new ModelAndView("/ErroRelatorio");
		}
		
		if(periodoRelatorio.getDataInicio() != null && periodoRelatorio.getDataFim() != null){
			Date dataInicio = Date.from(LocalDateTime.of(periodoRelatorio.getDataInicio(), LocalTime.of(0, 0, 0))
					.atZone(ZoneId.systemDefault()).toInstant());
			Date dataFim = Date.from(LocalDateTime.of(periodoRelatorio.getDataFim(), LocalTime.of(23, 59, 59))
					.atZone(ZoneId.systemDefault()).toInstant());
			
			map.put("id_empresa", periodoRelatorio.getEmpresa().getId());
			map.put("data_inicio", dataInicio);
			map.put("data_fim", dataFim);
		}		
		
		return new ModelAndView("rel_" + periodoRelatorio.getTipoRelatorio(), map);
	}
}
