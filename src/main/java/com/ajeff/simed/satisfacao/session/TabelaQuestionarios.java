package com.ajeff.simed.satisfacao.session;

import java.util.ArrayList;
import java.util.List;

import com.ajeff.simed.satisfacao.model.Questao;
import com.ajeff.simed.satisfacao.model.Pergunta;

class TabelaQuestionarios {
	
	private String uuid;
	private List<Pergunta> questionarios = new ArrayList<>();
	
	public TabelaQuestionarios(String uuid) {
		this.uuid = uuid;
	}
	
	
	public void adicionarQuestao(Questao pergunta, Long nota) {
		Pergunta questionario = new Pergunta();
		questionario.setPergunta(pergunta);
		questionario.setNota(nota);
		
		questionarios.add(questionario);
	}
	
	public int total() {
		return questionarios.size();
	}

	public List<Pergunta> getQuestionarios() {
		return questionarios;
	}

	public String getUuid() {
		return uuid;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TabelaQuestionarios other = (TabelaQuestionarios) obj;
		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;
		return true;
	}
	
}
