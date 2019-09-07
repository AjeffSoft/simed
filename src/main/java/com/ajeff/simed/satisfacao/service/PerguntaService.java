package com.ajeff.simed.satisfacao.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.repository.QuestoesRepository;
import com.ajeff.simed.satisfacao.repository.PerguntasRepository;

@Service
public class PerguntaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PerguntaService.class);	
	
	@Autowired
	private PerguntasRepository repository;
	@Autowired
	private QuestaoService questaoService;
	
//	public List<Pergunta> listarTodasAsPerguntas(Pesquisa pesquisa, UsuarioSistema usuarioSistema) {
//		List<Questao> perguntas = perguntaRepository.findByEmpresa(usuarioSistema.getUsuario().getEmpresa());
//
//		List<Pergunta> questoes = new ArrayList<>();
//
//		return questoes;
//	}

	@Transactional
	public void salvar(Pergunta pergunta) {
		repository.save(pergunta);
	}

	public Pergunta findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Pergunta> findByQuestaoEmpresa(Empresa empresa) {
		return repository.findByQuestaoEmpresa(empresa);
	}

	public List<Pergunta> listarPerguntasPorEmpresa(Empresa empresa) {
		List<Questao> questoes = questaoService.listarTodasAsQuestoes(empresa);
		List<Pergunta> perguntas = new ArrayList<>();
		for (Questao q : questoes) {
			Pergunta pergunta = new Pergunta();
			pergunta.setQuestao(q);
			perguntas.add(pergunta);
			System.out.println(pergunta.getQuestao().getNome());
		}
		return perguntas;
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
