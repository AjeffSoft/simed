package com.ajeff.simed.cooperado.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.service.CooperadoService;

@Controller
@RequestMapping("/cooperado/dependente")
public class DependenteController {

	@Autowired
	private CooperadoService serviceCooperado;

	
	@GetMapping("/novo")
	public ModelAndView novo(Dependente dependente) {
		ModelAndView mv = new ModelAndView("Cooperado/dependente/CadastroDependente");
		mv.addObject("cooperados", serviceCooperado.listarCooperados());
		return mv;
	}
	
	
//	@PostMapping(value = {"/novo", "{\\d}"})
//	public ModelAndView salvar(@Valid Cooperado cooperado, BindingResult result, RedirectAttributes attributes) {
//		if (result.hasErrors()) {
//			return novo(cooperado);
//		}
//		
//		try {
//			service.salvar(cooperado);
//		} catch (RegistroJaCadastradoException e) {
//			result.rejectValue("nome", e.getMessage(), e.getMessage());
//			return novo(cooperado);
//		}
//		attributes.addFlashAttribute("mensagem", "Cooperado " + cooperado.getNome() + " salvo com sucesso");
//		return new ModelAndView("redirect:/cooperado/cooperado/novo");
//	}
//	
//
//	@GetMapping("/pesquisar")
//	public ModelAndView pesquisar(CooperadoFilter cooperadoFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
//										HttpServletRequest httpServletRequest) {
//		ModelAndView mv = new ModelAndView("Cooperado/cooperado/PesquisarCooperados");
//
//		PageWrapper<Cooperado> paginaWrapper = new PageWrapper<>(service.filtrar(cooperadoFilter, pageable), httpServletRequest);
//		mv.addObject("pagina", paginaWrapper);
//		return mv;
//	}	
//
//
//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir (@PathVariable Long id){
//		try {
//			service.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
//	
//	@GetMapping("/alterar/{id}")
//	public ModelAndView alterar(@PathVariable Long id, Cooperado cooperado) {
//		cooperado = service.buscarComCidadeEstado(id);
//		ModelAndView mv = novo(cooperado);
//		mv.addObject(cooperado);
//		return mv;
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
