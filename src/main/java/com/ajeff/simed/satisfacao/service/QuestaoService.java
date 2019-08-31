package com.ajeff.simed.satisfacao.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.repository.QuestoesRepository;

@Service
public class QuestaoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(QuestaoService.class);	
	
	@Autowired
	private QuestoesRepository repository;

	public List<Questao> listarTodasAsQuestoes(Pesquisa pesquisa, UsuarioSistema usuarioSistema) {
		List<Questao> questoes = repository.findByEmpresa(usuarioSistema.getUsuario().getEmpresa());
		return questoes;
	}


}
