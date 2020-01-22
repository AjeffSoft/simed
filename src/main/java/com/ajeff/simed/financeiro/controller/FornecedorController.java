package com.ajeff.simed.financeiro.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.enums.TipoConta;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.financeiro.service.ContaPagarService;
import com.ajeff.simed.financeiro.service.FornecedorService;
import com.ajeff.simed.financeiro.service.ImpostoService;
import com.ajeff.simed.financeiro.service.exception.CpfCnpjInvalidoException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.EstadoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/financeiro/fornecedor")
public class FornecedorController {

	@Autowired
	private FornecedorService service;
	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private EstadoService estadoService;
	@Autowired
	private ContaPagarService contaPagarService;
	@Autowired
	private ImpostoService impostoService;

	
	@GetMapping("/novo")
	public ModelAndView novo(Fornecedor fornecedor) {
		ModelAndView mv = new ModelAndView("Financeiro/fornecedor/CadastroFornecedor");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		mv.addObject("agencias", agenciaService.findAllOrderByAgencia());
		mv.addObject("tiposConta", TipoConta.values());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Fornecedor fornecedor, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(fornecedor);
		}
		
		try {
			service.salvar(fornecedor);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(fornecedor);
		} catch (CpfCnpjInvalidoException e) {
			result.rejectValue("documento1", e.getMessage(), e.getMessage());
			return novo(fornecedor);
		}
		attributes.addFlashAttribute("mensagem", "Fornecedor " + fornecedor.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/financeiro/fornecedor/novo");
	}
	

	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Fornecedor> pesquisarFornecedor(String nome){
		validarTamanhoNomePesquisa(nome);
		return service.findByNomeContainingIgnoreCase('%' + nome + '%');
		               
	}


	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(FornecedorFilter fornecedorFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/fornecedor/PesquisarFornecedores");

		PageWrapper<Fornecedor> paginaWrapper = new PageWrapper<>(service.filtrar(fornecedorFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable Long id, Fornecedor fornecedor) {
		fornecedor = service.buscarComCidadeEstado(id);
		ModelAndView mv = novo(fornecedor);
		mv.addObject(fornecedor);
		return mv;
	}
	
	
	private void validarTamanhoNomePesquisa(String nome) {
		if(StringUtils.isEmpty(nome) || nome.length() <3) {
			throw new IllegalArgumentException();
		}
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
		return ResponseEntity.badRequest().build();
	}
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView historico(@PathVariable Long id, Fornecedor fornecedor) {
		ModelAndView mv = new ModelAndView("Financeiro/fornecedor/DetalheFornecedor");
		fornecedor = service.findOne(id);
		mv.addObject("contasPagar", contaPagarService.findByContaPagarFornecedor(fornecedor));
		mv.addObject("impostos", impostoService.findByContaPagarOrigemFornecedorOrderByVencimento(fornecedor));
		mv.addObject(fornecedor);
		return mv;
	}	
	
	
}
