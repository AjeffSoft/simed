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
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.financeiro.service.ClienteService;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.EstadoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Controller
@RequestMapping("/financeiro/cliente")
public class ClienteController {

	@Autowired
	private ClienteService service;
	@Autowired
	private EstadoService estadoService;
//	@Autowired
//	private ContasReceberRepository contasReceberRepository;
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Fornecedor fornecedor) {
		ModelAndView mv = new ModelAndView("Financeiro/cliente/CadastroCliente");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		return mv;
	}

	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Fornecedor cliente, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cliente);
		}
		
		try {
			service.salvar(cliente);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(cliente);
		}
		attributes.addFlashAttribute("mensagem", "Cliente " + cliente.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/financeiro/cliente/novo");
	}
	

	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
	public @ResponseBody List<Fornecedor> pesquisar(String nome){
		validarTamanhoNomePesquisa(nome);
		return service.findByNomeClienteContainingIgnoreCase('%' + nome + '%');
		               
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisarClientes(FornecedorFilter fornecedorFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Financeiro/cliente/PesquisarClientes");

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
		ModelAndView mv = novo(fornecedor);
		fornecedor = service.buscarComCidadeEstado(id);
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
	
//	@GetMapping("/detalhe/{id}")
//	public ModelAndView detalhe(@PathVariable Long id, Fornecedor fornecedor) {
//		ModelAndView mv = new ModelAndView("Financeiro/cliente/DetalheCliente");
//		fornecedor = service.findOne(id);
//		mv.addObject("contasReceber", contasReceberRepository.findByFornecedor(fornecedor));
//		mv.addObject(fornecedor);
//		return mv;
//	}	
	
//	@GetMapping("/historico/{id}")
//	public ModelAndView imprimirHistorico(@PathVariable Long id) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("format", "pdf");
//		map.put("id_fornecedor", id);
//		return new ModelAndView("rel_CLIENTE_Historico", map);
//	}	
	
}
