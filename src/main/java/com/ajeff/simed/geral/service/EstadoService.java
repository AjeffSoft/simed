package com.ajeff.simed.geral.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.geral.model.Estado;
import com.ajeff.simed.geral.repository.EstadosRepository;

@Service
public class EstadoService {
	
	@Autowired
	private EstadosRepository repository;


	public List<Estado> findTodosOrderByNome() {
		return repository.findAllOrderByNome();
	}

}
