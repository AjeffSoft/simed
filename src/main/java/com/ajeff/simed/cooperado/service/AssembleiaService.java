package com.ajeff.simed.cooperado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.repository.AssembleiasRepository;

@Service
public class AssembleiaService {
	
	@Autowired
	private AssembleiasRepository repository;
	
	@Transactional
	public void salvar(Assembleia assembleia) {
		repository.save(assembleia);
	}

}
