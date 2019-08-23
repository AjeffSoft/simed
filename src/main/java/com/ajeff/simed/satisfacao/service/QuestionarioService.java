package com.ajeff.simed.satisfacao.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.PesquisaSatisfacao;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.PerguntasRepository;
import com.ajeff.simed.satisfacao.repository.QuestionariosRepository;

@Service
public class QuestionarioService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(QuestionarioService.class);	
	
	@Autowired
	private QuestionariosRepository repository;
	@Autowired
	private PerguntasRepository perguntaRepository;
	
	public List<Questionario> listarTodasAsPerguntas(PesquisaSatisfacao pesquisa, UsuarioSistema usuarioSistema) {
		List<Pergunta> perguntas = perguntaRepository.findByEmpresa(usuarioSistema.getUsuario().getEmpresa());

		List<Questionario> questoes = new ArrayList<>();

		for (Pergunta p : perguntas) {
			Questionario questao = new Questionario();
			questao.setPergunta(p);
			questao.setNota(0L);
			questoes.add(questao);
		}
		return questoes;
	}

	@Transactional
	public void salvar(Questionario questionario) {
		repository.save(questionario);
	}

	public Questionario findOne(Long id) {
		return repository.findOne(id);
	}
	
//	@GetMapping("/novo")
//	public ModelAndView novo(PesquisaSatisfacao pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("PesquisaSatisfacao/Questionario");
//		List<Questionario> questionarios = questionarioService.listarTodasAsPerguntas(pesquisa, usuarioSistema);
//		questionarios.forEach(q-> System.out.println(q.getPergunta().getNome()));
//		mv.addObject("questionarios", questionarios);
//		return mv;
//	}
	
	
//	@Transactional
//	public void salvar(PesquisaSatisfacao pesquisa, Usuario usuario) {
//		pesquisa.setData(LocalDateTime.now());
//		pesquisa.setConcluiu(true);
//		pesquisa.setNome("FULANO DE TAL");
//		pesquisa.setUsuario(usuario);
//		repository.save(pesquisa);
//	}

}
