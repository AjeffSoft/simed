package com.ajeff.simed.satisfacao.controller;

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

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.model.enums.TipoResposta;
import com.ajeff.simed.satisfacao.repository.filter.QuestionarioFilter;
import com.ajeff.simed.satisfacao.service.PerguntaService;
import com.ajeff.simed.satisfacao.service.QuestionarioService;

@Controller
@RequestMapping("/satisfacao/questionario")
public class QuestionarioController {

	@Autowired
	private QuestionarioService service;
	@Autowired
	private PerguntaService perguntaService;
	@Autowired
	private EmpresaService empresaService;
	
	
	
	@GetMapping("/nova")
	public ModelAndView nova(Questionario questionario, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/questionario/CadastroQuestionario");
		mv.addObject("perguntas", perguntaService.findAllOrderByNome());
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("tipos", TipoResposta.values());
		return mv;
	}
	
	@PostMapping(value = {"/nova", "{\\d}"})
	public ModelAndView salvar(@Valid Questionario questionario, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {

		if(result.hasErrors()) {
			return nova(questionario, usuarioSistema);
		}
		
		try {
			service.salvar(questionario);
		} catch (RegistroJaCadastradoException e) {
			result.rejectValue("questionario", e.getMessage(), e.getMessage());
			return nova(questionario, usuarioSistema);
		}
		attributes.addFlashAttribute("mensagem", "Questionario salvo com sucesso");
		return new ModelAndView("redirect:/satisfacao/questionario/nova");
	}
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(QuestionarioFilter questionarioFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/questionario/PesquisarQuestionarios");
		mv.addObject("perguntas", perguntaService.findAllOrderByNome());
		mv.addObject("tipos", TipoResposta.values());
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		PageWrapper<Questionario> paginaWrapper = new PageWrapper<>(service.filtrar(questionarioFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
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
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar (@PathVariable Long id, Questionario questionario ,@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = nova(questionario, usuarioSistema);
		questionario = service.findOne(id);
		mv.addObject(questionario);
		return mv;
	}	
	
}
