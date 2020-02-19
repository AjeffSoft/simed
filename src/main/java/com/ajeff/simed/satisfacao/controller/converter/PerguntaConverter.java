package com.ajeff.simed.satisfacao.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.satisfacao.model.Pergunta;

public class PerguntaConverter implements Converter<String, Pergunta>{

	@Override
	public Pergunta convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Pergunta pergunta = new Pergunta();
			pergunta.setId(Long.valueOf(id));
			return pergunta;
		}

		return null;
	}
}
