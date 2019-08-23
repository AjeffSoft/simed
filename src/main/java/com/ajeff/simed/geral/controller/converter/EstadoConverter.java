package com.ajeff.simed.geral.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.model.Estado;

public class EstadoConverter implements Converter<String, Estado>{

	@Override
	public Estado convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Estado estado = new Estado();
			estado.setId(Long.valueOf(id));
			return estado;
		}

		return null;
	}
}
