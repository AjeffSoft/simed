package com.ajeff.simed.cooperado.service;

import java.util.List;

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
		
		if(diretoria.isNovo()){
			diretoria.setAtivo(true);
			diretoria.setVencedor(false);
		}
		repository.save(diretoria);
	}

	public Page<Diretoria> filtrar(DiretoriaFilter diretoriaFilter, Pageable pageable) {
		return repository.filtrar(diretoriaFilter, pageable);
	}

	public Diretoria findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Diretoria> findAll() {
		return repository.findAll();
	}

}
