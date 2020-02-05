package com.ajeff.simed.financeiro.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.service.ContaReceberService;
import com.ajeff.simed.financeiro.service.RecebimentoService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.MovimentacaoFechadaException;
import com.ajeff.simed.financeiro.service.exception.RecebimentoNaoEfetuadoException;
import com.ajeff.simed.financeiro.service.exception.ValorInformadoInvalidoException;
import com.ajeff.simed.geral.service.ContaEmpresaService;

@Controller
@RequestMapping("/financeiro/recebimento")
public class RecebimentoController {

	@Autowired
	private RecebimentoService service;
	@Autowired
	private ContaReceberService contaReceberService;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	
	
	@GetMapping("/novo/{idContaReceber}")
	public ModelAndView novo (@PathVariable("idContaReceber") Long id, Recebimento recebimento) {
		ContaReceber contaReceber = contaReceberService.findOne(id);
		ModelAndView mv = new ModelAndView("Financeiro/recebimento/CadastroRecebimento");
		mv.addObject("idContaEmpresa", id);
		mv.addObject("contasBancos", contaEmpresaService.listarTodosOrdenadoPorNome());
		mv.addObject(contaReceber);
		return mv;
	}
	


	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Recebimento recebimento, @RequestParam("idContaReceber") Long id, BindingResult result) {
		if(result.hasErrors()) {
			return novo(id, recebimento);
		}
		try {
			service.salvar(recebimento, id);
		} catch (ValorInformadoInvalidoException e) {
			result.rejectValue("valor", e.getMessage(), e.getMessage());
			return novo(id, recebimento);
		} catch (MovimentacaoFechadaException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(id, recebimento);
		} catch (RecebimentoNaoEfetuadoException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return novo(id, recebimento);
		}
		return new ModelAndView("redirect:/financeiro/contaReceber/pesquisar");
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
	
}
