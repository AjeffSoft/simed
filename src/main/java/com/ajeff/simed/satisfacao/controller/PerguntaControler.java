package com.ajeff.simed.satisfacao.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.service.PerguntaService;
import com.ajeff.simed.satisfacao.service.QuestaoService;

@Controller
@RequestMapping("/pergunta")
public class PerguntaControler {

	@Autowired
	private PerguntaService service;
	@Autowired
	private QuestaoService questaoService;
//	
//	
//	@GetMapping("/lista")
//	public ModelAndView lista(@RequestParam(value = "idPergunta") Long id, Questionario questionario) {
//		
//		Pergunta pergunta = perguntaService.findOne(id);
//		questionario.setPergunta(pergunta);
//		
//		List<Questionario> q = new ArrayList<>();
//		q.add(questionario);
//		q.forEach(i-> i.getPergunta().getNome());
//		return new ModelAndView("redirect:/pesquisaSatisfacao/lista");
//	}

	@GetMapping("/novo")
	public ModelAndView novo( @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/Pergunta");
//		List<Questao> questoes = questaoService.listarTodasAsQuestoes(usuarioSistema);
//		List<Pergunta> perguntas = new ArrayList<>();
//		for (Questao q : questoes) {
//			Pergunta pergunta = new Pergunta();
//			pergunta.setQuestao(q);
//			perguntas.add(pergunta);
//			System.out.println(pergunta.getQuestao().getNome());
//		}
//		mv.addObject("perguntas", perguntas);
		return mv;
	}
//	
//	
//	
////	@GetMapping("/novo")
////	public ModelAndView novo(PesquisaSatisfacao pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
////		ModelAndView mv = new ModelAndView("PesquisaSatisfacao/Questionario");
////		List<Questionario> questionarios = questionarioService.listarTodasAsPerguntas(pesquisa, usuarioSistema);
////		questionarios.forEach(q-> System.out.println(q.getPergunta().getNome()));
////		mv.addObject("questionarios", questionarios);
////		return mv;
////	}
//	
	@PostMapping("/novo")
	public ModelAndView salvar(Pergunta pergunta, @RequestParam("idQuestao") Long idQuestao, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {

//		try {
		Questao q = questaoService.findOne(idQuestao);
		pergunta.setQuestao(q); 
			service.salvar(pergunta);
//		} catch (AgenciaEBancoJaCadastradoException e) {
//			result.rejectValue("agencia", e.getMessage(), e.getMessage());
//			return nova(agencia);
//		}
		return new ModelAndView("redirect:/pergunta/novo");
	}	
}
