package com.ajeff.simed.geral.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.repository.CidadesRepository;

@Service
public class CidadeService {

	@Autowired
	private CidadesRepository repository;

	public List<Cidade> findByEstadoCodigo(Long idEstado) {
		return repository.findByEstadoId(idEstado);
	}
}
