package com.ajeff.simed.cooperado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.repository.AssembleiasRepository;
import com.ajeff.simed.cooperado.repository.filter.AssembleiaFilter;

@Service
public class AssembleiaService {
	
	@Autowired
	private AssembleiasRepository repository;
	
	@Transactional
	public void salvar(Assembleia assembleia) {
		repository.save(assembleia);
	}

	public Page<Assembleia> filtrar(AssembleiaFilter assembleiaFilter, Pageable pageable) {
		return repository.filtrar(assembleiaFilter, pageable);
	}

}
