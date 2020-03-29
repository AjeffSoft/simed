package com.ajeff.simed.cooperado.controller;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.model.enums.TipoRecebimentoProducao;
import com.ajeff.simed.cooperado.repository.filter.AdmissaoFilter;
import com.ajeff.simed.cooperado.service.AdmissaoService;
import com.ajeff.simed.cooperado.service.CooperadoService;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/cooperado/admissao")
public class AdmissaoController {

	@Autowired
	private AdmissaoService service;
	@Autowired
	private CooperadoService serviceCooperado;

	
	@GetMapping("/novo")
	public ModelAndView novo(AdmissaoCooperado admissao) {
		ModelAndView mv = new ModelAndView("Cooperado/admissao/CadastroAdmissao");
		
		if(admissao.isNovo()) {
			mv.addObject("cooperados", serviceCooperado.findByCooperadoAtivoFalse());
		}else {
			mv.addObject("cooperados", serviceCooperado.findByCooperadoAtivoTrue());
		}
		mv.addObject("tiposRecebimento", TipoRecebimentoProducao.values());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid AdmissaoCooperado admissao, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(admissao);
		}
		
		try {
			service.salvar(admissao);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("registro", e.getMessage(), e.getMessage());
			return novo(admissao);
		}
		attributes.addFlashAttribute("mensagem", "Admissão n° " + admissao.getRegistro() + " salva com sucesso");
		return new ModelAndView("redirect:/cooperado/admissao/novo");
	}
	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(AdmissaoFilter admissaoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Cooperado/admissao/PesquisarAdmissoes");
		mv.addObject("cooperados", serviceCooperado.findByCooperadoAtivoTrue());

		PageWrapper<AdmissaoCooperado> paginaWrapper = new PageWrapper<>(service.filtrar(admissaoFilter, pageable), httpServletRequest);
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
	public ModelAndView alterar(@PathVariable Long id, AdmissaoCooperado admissao) {
		admissao = service.findOne(id);
		ModelAndView mv = novo(admissao);
		mv.addObject(admissao);
		return mv;
	}
	
	
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
