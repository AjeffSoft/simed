package com.ajeff.simed.satisfacao.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.service.PesquisaService;
import com.ajeff.simed.satisfacao.service.QuestionarioService;
import com.ajeff.simed.satisfacao.service.RespostaService;

@Controller
@RequestMapping("/satisfacao/pesquisa")
public class PesquisaController {

//	Timer timer;
//	String nome;
//	
	
	@Autowired
	private PesquisaService service;
	@Autowired
	private QuestionarioService questionarioService;
	@Autowired
	private RespostaService respostaService;
//	@Autowired
//	private TabelaQuestionariosSession tabelaQuestionarios;

	@GetMapping("/inicio")
	public ModelAndView abrir() {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/InicioPesquisa");
		return mv;
	}
	

	@GetMapping("/fim")
	public ModelAndView agradecimento() {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/FinalPesquisa");
		return mv;
	}	
	
	
	
//	@GetMapping("/fim1")
//	public ModelAndView terminar() {
//        timer = new Timer();
//        timer.schedule(new RemindTask(),1000L);
//		ModelAndView mv = new ModelAndView(nome);
//		return mv;
//	}
//
//        
//	class RemindTask extends TimerTask {
//		@Override
//        public void run() {
//            nome = "Satisfacao/pesquisa/InicioPesquisa";
//            timer.cancel();
//        }
//    }



	@GetMapping("/novo")
	public ModelAndView novo(Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/CadastroDasPerguntas");
		List<Questionario> questionarios = questionarioService.findByEmpresaAndAtivoTrue(usuarioSistema.getUsuario().getEmpresa());
		
		List<Resposta> respostas = new ArrayList<Resposta>();
		for (Questionario q : questionarios) {
			respostas.clear();
			q.setRespostas(respostaService.buscarQuestionarioComRespostas(q.getId()));
//			
//			for(Resposta r: q.getRespostas()) {
//				respostas.add(r);
//			}
////			mv.addObject("respostas", respostas);
		}
		mv.addObject("questionarios", questionarios);

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
	
	
	
//	@PostMapping("/questao")
//	public @ResponseBody String adicionarQuestao(Long pergunta, String resposta, String uuid) {
//		Questionario questao = questaoService.findOne(pergunta);
//		tabelaQuestionarios.adicionarQuestao(uuid, questao, resposta);
//		return "questão adicionada!";
//	}

	

	@PostMapping("/novo")
	public ModelAndView salvar(@Valid Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {

//		pesquisa.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisa.getUuid()));
		service.salvar(pesquisa, usuarioSistema.getUsuario());
		return new ModelAndView("redirect:/satisfacao/pesquisa/fim");
	}	
}
