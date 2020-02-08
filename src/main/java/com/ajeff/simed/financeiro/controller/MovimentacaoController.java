package com.ajeff.simed.financeiro.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.repository.MovimentacoesItensRepository;
import com.ajeff.simed.financeiro.repository.filter.MovimentacaoFilter;
import com.ajeff.simed.financeiro.service.MovimentacaoService;
import com.ajeff.simed.financeiro.service.exception.ErroAoFecharMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;

@Controller
@RequestMapping("/financeiro/movimentacao")
public class MovimentacaoController {

	@Autowired
	private MovimentacaoService service;
	@Autowired
	private MovimentacoesItensRepository movBancarioRepository;
	@Autowired
	private EmpresaService empresaService;
	
	@GetMapping("/novo")
	public ModelAndView novo(Movimentacao movimentacao, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/movimentacao/CadastroMovimentacao");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		return mv;
	}
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(MovimentacaoFilter movimentacaoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/movimentacao/PesquisarMovimentacoes");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		PageWrapper<Movimentacao> paginaWrapper = new PageWrapper<>(service.filtrar(movimentacaoFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	

	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Movimentacao movimentacao, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if(result.hasErrors()) {
			return novo(movimentacao, usuarioSistema);
		}
		try {
			service.salvar(movimentacao);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("empresa", e.getMessage(), e.getMessage());
			return novo(movimentacao, usuarioSistema);
		}
		attributes.addFlashAttribute("mensagem", "Movimentação salva com sucesso");
		return new ModelAndView("redirect:/financeiro/movimentacao/novo");
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
	
	
	@GetMapping("/fecharMovimento/{id}")
	public ModelAndView abreFechar(@PathVariable Long id, Movimentacao movimentacao) {
		ModelAndView mv = new ModelAndView("Financeiro/movimentacao/ConfirmarFechamento");
		movimentacao = service.findOne(id);
		mv.addObject(movimentacao);
		return mv;
	}	
	
	@PostMapping(value = {"/fecharMovimento/{\\d+}"})
	public ModelAndView fechar(@Valid Movimentacao movimentacao, BindingResult result, RedirectAttributes attributes) {

		try {
			service.fecharMovimento(movimentacao);
		} catch (ErroAoFecharMovimentacaoException e) {
			result.rejectValue("empresa", e.getMessage(), e.getMessage());
			return abreFechar(movimentacao.getId(), movimentacao);
		}
		return new ModelAndView("redirect:/financeiro/movimentacao/pesquisar");
	}	


	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, Movimentacao movimentacao) {
		ModelAndView mv = new ModelAndView("Financeiro/movimentacao/DetalheMovimentacao");
		movimentacao = service.findOne(id);
		mv.addObject("movBancarias", movBancarioRepository.findByMovimentacao(movimentacao));
		mv.addObject(movimentacao);
		return mv;
	}		
	
	
	
}
