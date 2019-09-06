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
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.service.PerguntaService;
import com.ajeff.simed.satisfacao.service.PesquisaService;
import com.ajeff.simed.satisfacao.service.QuestaoService;

@Controller
@RequestMapping("/satisfacao")
public class PesquisaSatisfacaoControler {

	@Autowired
	private PesquisaService service;
	@Autowired
	private PerguntaService perguntaService;
	@Autowired
	private QuestaoService questaoService;
//	@Autowired
//	private TabelaQuestionariosSession tabelaQuestionarios;
	
	@GetMapping("/inicio")
	public ModelAndView abrir() {
		ModelAndView mv = new ModelAndView("Satisfacao/Inicio");
		return mv;
	}

//	@GetMapping("/novo")
//	public ModelAndView novo(Pesquisa pesquisaSatisfacao, Pergunta questionario, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("Satisfacao/Pesquisa");
////		List<Pergunta> questionarios = questionarioService.listarTodasAsPerguntas(pesquisaSatisfacao, usuarioSistema);
//		pesquisaSatisfacao.setUuid(UUID.randomUUID().toString());
////		mv.addObject("questionarios", questionarios);
//		return mv;
//	}
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/Pesquisa");
		List<Questao> questoes = questaoService.listarTodasAsQuestoes(pesquisa, usuarioSistema);
		pesquisa.setUuid(UUID.randomUUID().toString());
		mv.addObject("questoes", questoes);
		return mv;
	}	
	
//	@PostMapping("/questao")
//	public @ResponseBody String adicionarQuestao(Long idPergunta, Long nota, String uuid) {
//		Questao pergunta = perguntaRepository.findOne(idPergunta);
//		tabelaQuestionarios.adicionarQuestao(uuid, pergunta, nota);
//		return "questão adicionada!";
//	}

	
	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		pesquisaSatisfacao.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisaSatisfacao.getUuid()));
		service.salvar(pesquisa, usuarioSistema.getUsuario());
		return new ModelAndView("redirect:/satisfacao/inicio");
	}	
}
