package com.ajeff.simed.cooperado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.repository.DiretoriasRepository;
import com.ajeff.simed.cooperado.repository.filter.DiretoriaFilter;

@Service
public class DiretoriaService {
	
	@Autowired
	private DiretoriasRepository repository;
	
	@Transactional
	public void salvar(Diretoria diretoria) {
		diretoria.setAtivo(false);
		diretoria.setVencedor(false);
		repository.save(diretoria);
	}

	public Page<Diretoria> filtrar(DiretoriaFilter diretoriaFilter, Pageable pageable) {
		return repository.filtrar(diretoriaFilter, pageable);
	}

}
