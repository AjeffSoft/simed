package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.AgenciaService;
import com.ajeff.simed.geral.service.EstadoService;

@Controller
@RequestMapping("/cooperado/cooperado")
public class CooperadoController {

	@Autowired
	private CooperadoService service;
	@Autowired
	private AgenciaService agenciaService;
	@Autowired
	private EstadoService estadoService;

	
	@GetMapping("/novo")
	public ModelAndView novo(Cooperado cooperado) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/CadastroCooperado");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		mv.addObject("agencias", agenciaService.findAllOrderByAgencia());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid Cooperado cooperado, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(cooperado);
		}
		
		try {
			service.salvar(cooperado);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(cooperado);
		}
		attributes.addFlashAttribute("mensagem", "Cooperado " + cooperado.getNome() + " salvo com sucesso");
		return new ModelAndView("redirect:/cooperado/cooperado/novo");
	}
	

	//PESQUISA RAPIDA FILTRADO POR FORNECEDORES
//	@RequestMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
//	public @ResponseBody List<Fornecedor> pesquisarFornecedor(String nome){
//		validarTamanhoNomePesquisa(nome);
//		return service.findByNomeContainingIgnoreCase('%' + nome + '%');
//		               
//	}


	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(CooperadoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/cooperado/PesquisarCooperados");

		PageWrapper<Cooperado> paginaWrapper = new PageWrapper<>(service.filtrar(cooperadoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}	


//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
//		try {
//			service.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
	
//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, Fornecedor fornecedor) {
//		fornecedor = service.buscarComCidadeEstado(id);
//		ModelAndView mv = novo(fornecedor);
//		mv.addObject(fornecedor);
//		return mv;
//	}
	
	
//	@ExceptionHandler(IllegalArgumentException.class)
//	public ResponseEntity<Void> tratarIllegalArgumentException(IllegalArgumentException e) {
//		return ResponseEntity.badRequest().build();
//	}
	

//	@GetMapping("/historico/{id}")
//	public ModelAndView imprimirHistorico(@PathVariable Long id) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("format", "pdf");
//		map.put("id_fornecedor", id);
//		return new ModelAndView("rel_FORNECEDOR_Historico", map);
//	}
	
//	@GetMapping("/detalhe/{id}")
//	public ModelAndView detalhe(@PathVariable Long id, Fornecedor fornecedor) {
//		ModelAndView mv = new ModelAndView("Financeiro/fornecedor/DetalheFornecedor");
//		fornecedor = service.findOne(id);
//		mv.addObject("contasPagar", contasPagarRepository.findByContaPagarFornecedor(fornecedor));
//		mv.addObject("impostos", impostosRepository.findByContaPagarOrigemFornecedorOrderByVencimento(fornecedor));
//		mv.addObject(fornecedor);
//		return mv;
//	}
	
	
}
