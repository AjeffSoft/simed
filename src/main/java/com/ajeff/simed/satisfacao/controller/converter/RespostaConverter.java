package com.ajeff.simed.satisfacao.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.satisfacao.model.Resposta;

public class RespostaConverter implements Converter<String, Resposta>{

	@Override
	public Resposta convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Resposta resposta = new Resposta();
			resposta.setId(Long.valueOf(id));
			return resposta;
		}

		return null;
	}
}
