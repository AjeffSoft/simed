package com.ajeff.simed.financeiro.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.PagamentoService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.MovimentacaoFechadaException;
import com.ajeff.simed.financeiro.service.exception.PagamentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.EmpresaService;

@Controller
@RequestMapping("/financeiro/pagamento")
public class PagamentoController {

	
	@Autowired
	private PagamentoService service;
	@Autowired
	private ContaPagarService contaService;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private EmpresaService empresaService;


	
	
	@GetMapping("/novo")
	public ModelAndView novo(@RequestParam(value = "idConta", required = true) List<Long> ids,Pagamento pagamento) {
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
		} catch (MovimentacaoFechadaException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(ids, pagamento);
		}

		if (pagamento.getAgrupado()) {
			return new ModelAndView("redirect:/financeiro/pagamento/comprovante/"+ pagamento.getId());
		}else {
			return new ModelAndView("redirect:/financeiro/pagamento/comprovante");
		}
	}	
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PagamentoFilter pagamentoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
			HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/PesquisarPagamentos");
		mv.addObject("contasBancos", contaEmpresaService.buscarContaCorrenteBanco());
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("total", service.totalGeral(pagamentoFilter));
		PageWrapper<Pagamento> paginaWrapper = new PageWrapper<>(service.filtrar(pagamentoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	
	@GetMapping("/contasAutorizadas")
	public ModelAndView listaAutorizado(ContaPagar contaPagar) {
		ModelAndView mv = new ModelAndView("Financeiro/contaPagar/ContasPagarAutorizadas");
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
		} catch (PagamentoNaoEfetuadoException e) {
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
		} catch (MovimentacaoFechadaException e) {
			result.rejectValue("dataPago", e.getMessage(), e.getMessage());
			return abrePagar(pagamento.getId(), pagamento);
		}		
		return new ModelAndView("redirect:/financeiro/pagamento/pesquisar?empresa="+pagamento.getContaEmpresa().getEmpresa().getId()+"&status=EMITIDO");
	}
	
	
	@PutMapping("/cancelarConfirmacao")
	@ResponseStatus(HttpStatus.OK)
	public void cancelarConfirmacao(@RequestParam Long id) {
		Pagamento pagamento = service.findOne(id);
		service.cancelarConfirmacao(pagamento);
	}	


	@GetMapping("/comprovante")
	public ModelAndView comprovanteIndividual() {
		return new ModelAndView("Financeiro/pagamento/ComprovantePagamento");
	}

	@GetMapping("/comprovante/{id}")
	public ModelAndView comprovante(@PathVariable Long id) {
		ModelAndView mv = new ModelAndView("Financeiro/pagamento/ComprovantePagamentoAgrupado");
		mv.addObject("pagamentos", service.findOne(id));
		return mv;
	}
}
