package com.ajeff.simed.satisfacao.controller;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.PesquisaSatisfacao;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.PerguntasRepository;
import com.ajeff.simed.satisfacao.service.PesquisaSatisfacaoService;
import com.ajeff.simed.satisfacao.service.QuestionarioService;
import com.ajeff.simed.satisfacao.session.TabelaQuestionariosSession;

@Controller
@RequestMapping("/satisfacao")
public class PesquisaSatisfacaoControler {

	@Autowired
	private PesquisaSatisfacaoService service;
	@Autowired
	private QuestionarioService questionarioService;
	@Autowired
	private PerguntasRepository perguntaRepository;
	@Autowired
	private TabelaQuestionariosSession tabelaQuestionarios;
	
	@GetMapping("/inicio")
	public ModelAndView abrir() {
		ModelAndView mv = new ModelAndView("Satisfacao/IndexSatisfacao");
		return mv;
	}

	@GetMapping("/novo")
	public ModelAndView novo(PesquisaSatisfacao pesquisaSatisfacao, Questionario questionario, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/Pesquisa");
		List<Questionario> questionarios = questionarioService.listarTodasAsPerguntas(pesquisaSatisfacao, usuarioSistema);
		pesquisaSatisfacao.setUuid(UUID.randomUUID().toString());
		mv.addObject("questionarios", questionarios);
		return mv;
	}
	
	@PostMapping("/questao")
	public @ResponseBody String adicionarQuestao(Long idPergunta, Long nota, String uuid) {
		Pergunta pergunta = perguntaRepository.findOne(idPergunta);
		tabelaQuestionarios.adicionarQuestao(uuid, pergunta, nota);
		return "questão adicionada!";
	}

	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid PesquisaSatisfacao pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		pesquisa.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisa.getUuid()));
		service.salvar(pesquisa, usuarioSistema.getUsuario());
		return new ModelAndView("redirect:/satisfacao/inicio");
	}	
}
