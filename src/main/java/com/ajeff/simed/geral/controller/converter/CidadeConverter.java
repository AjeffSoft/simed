package com.ajeff.simed.geral.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.model.Cidade;

public class CidadeConverter implements Converter<String, Cidade>{

	@Override
	public Cidade convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Cidade cidade = new Cidade();
			cidade.setId(Long.valueOf(id));
			return cidade;
		}

		return null;
	}
}
