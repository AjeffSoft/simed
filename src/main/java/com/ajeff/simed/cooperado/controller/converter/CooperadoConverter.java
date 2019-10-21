package com.ajeff.simed.cooperado.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.cooperado.model.Cooperado;

public class CooperadoConverter implements Converter<String, Cooperado>{

	@Override
	public Cooperado convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Cooperado cooperado = new Cooperado();
			cooperado.setId(Long.valueOf(id));
			return cooperado;
		}

		return null;
	}
}
