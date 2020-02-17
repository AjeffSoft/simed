package com.ajeff.simed.satisfacao.controller;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pesquisa;

@Controller
@RequestMapping("/satisfacao")
public class PesquisaSatisfacaoControler {

//	@Autowired
//	private PesquisaService service;
//	@Autowired
//	private PerguntaService perguntaService;
//	@Autowired
//	private QuestaoService questaoService;
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
//		List<Pergunta> perguntas = perguntaService.listarPerguntasPorEmpresa(usuarioSistema.getUsuario().getEmpresa());
//		pesquisa.setUuid(UUID.randomUUID().toString());
//		pesquisa.setPerguntas(perguntas);
//		mv.addObject("todasPerguntas", perguntas);
		return mv;
	}	
	
	@PostMapping("/questao")
	public @ResponseBody String adicionarQuestao(Long pergunta, String resposta, String uuid) {
//		Questionario questao = questaoService.findOne(pergunta);
//		tabelaQuestionarios.adicionarQuestao(uuid, questao, resposta);
		return "questão adicionada!";
	}

	

	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		pesquisa.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisa.getUuid()));
//		service.salvar(pesquisa, usuarioSistema.getUsuario());
		return new ModelAndView("redirect:/satisfacao/inicio");
	}	
}
