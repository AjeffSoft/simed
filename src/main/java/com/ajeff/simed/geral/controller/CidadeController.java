package com.ajeff.simed.geral.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.repository.CidadesRepository;

@Controller
@RequestMapping("/cidade")
public class CidadeController {

	@Autowired
	private CidadesRepository repository;
	

	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Cidade> pesquisarPorCodigoEstado(
			@RequestParam(name = "estado", defaultValue = "-1") Long idEstado) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {	}
		return repository.findByEstadoId(idEstado);
	}

	
}
