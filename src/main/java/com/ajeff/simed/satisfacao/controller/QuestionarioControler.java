package com.ajeff.simed.satisfacao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/questionario")
public class QuestionarioControler {
//
//	@Autowired
//	private QuestionarioService service;
//	@Autowired
//	private PerguntaService perguntaService;
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

//	@GetMapping("/novo")
//	public ModelAndView novo(PesquisaSatisfacao pesquisaSatisfacao, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("PesquisaSatisfacao/Pesquisa");
//		List<Questionario> questionarios = questionarioService.listarTodasAsPerguntas(pesquisaSatisfacao, usuarioSistema);
//		questionarios.forEach(q-> System.out.println(q.getPergunta().getNome()));
//		mv.addObject("questionarios", questionarios);
//		return mv;
//	}
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
//	@PostMapping("/novo")
//	public ModelAndView salvar(@Valid PesquisaSatisfacao pesquisa, BindingResult result, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//
////		if(result.hasErrors()) {
////			return nova(agencia);
////		}
//		
////		try {
//			service.salvar(pesquisa, usuarioSistema.getUsuario());
////		} catch (AgenciaEBancoJaCadastradoException e) {
////			result.rejectValue("agencia", e.getMessage(), e.getMessage());
////			return nova(agencia);
////		}
////		attributes.addFlashAttribute("mensagem", "Agencia " + agencia.getAgencia() + " salva com sucesso");
//		return new ModelAndView("redirect:/pesquisaSatisfacao/inicio");
//	}	
}
