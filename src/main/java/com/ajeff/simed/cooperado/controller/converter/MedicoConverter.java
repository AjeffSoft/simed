package com.ajeff.simed.cooperado.controller.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import com.ajeff.simed.cooperado.model.Medico;

public class MedicoConverter implements Converter<String, Medico>{

	@Override
	public Medico convert(String id) {
		if (!StringUtils.isEmpty(id)){ 
			Medico cooperado = new Medico();
			cooperado.setId(Long.valueOf(id));
			return cooperado;
		}

		return null;
	}
}
