package com.ajeff.simed.satisfacao.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.repository.QuestoesRepository;

@Service
public class QuestaoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(QuestaoService.class);	
	
	@Autowired
	private QuestoesRepository repository;

	public List<Questao> listarTodasAsQuestoes(Empresa empresa) {
		List<Questao> questoes = repository.findByEmpresa(empresa);
		return questoes;
	}

	public Questao findOne(Long idQuestao) {
		return repository.findOne(idQuestao);
	}


}
