package com.ajeff.simed.satisfacao.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.PesquisasRepository;

@Service
public class PesquisaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PesquisaService.class);	
	
	@Autowired
	private PesquisasRepository repository;
	
	@Transactional
	public void salvar(Pesquisa pesquisa, Usuario usuario) {
		pesquisa.setData(LocalDateTime.now());
		pesquisa.setCliente("ANÔNIMO");
		pesquisa.setUsuario(usuario);
		repository.save(pesquisa);
	}

}
