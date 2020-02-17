package com.ajeff.simed.satisfacao.session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.ajeff.simed.satisfacao.model.Pergunta;
import com.ajeff.simed.satisfacao.model.Questionario;

@SessionScope
@Component
public class TabelaQuestionariosSession {
	
	private Set<TabelaQuestionarios> tabelas = new HashSet<>();

	public void adicionarQuestao(String uuid, Questionario questao, String resposta) {
		TabelaQuestionarios tabela = buscarTabelaPorUuid(uuid);
//		tabela.adicionarQuestao(questao, resposta);
		tabelas.add(tabela);
	}


	private TabelaQuestionarios buscarTabelaPorUuid(String uuid) {
		TabelaQuestionarios tabela = tabelas.stream()
				.filter(t -> t.getUuid().equals(uuid))
				.findAny()
				.orElse(new TabelaQuestionarios(uuid));
		return tabela;
	}

	
	public List<Pergunta> getQuestionarios(String uuid) {
		return buscarTabelaPorUuid(uuid).getQuestionarios();		
	}


	public int total(String uuid) {
		return buscarTabelaPorUuid(uuid).total();
	}
	
}
