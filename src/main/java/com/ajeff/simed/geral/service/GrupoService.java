package com.ajeff.simed.geral.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Grupo;
import com.ajeff.simed.geral.repository.GruposRepository;
import com.ajeff.simed.geral.repository.filter.GrupoFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class GrupoService {
	
	@Autowired
	private GruposRepository repository;
	
	@Transactional
	public Grupo salvar(Grupo grupo){
		Optional<Grupo> grupoOptional = repository.findByNomeIgnoreCase(grupo.getNome());
		
		if(grupoOptional.isPresent() && !grupoOptional.get().equals(grupo)){
			throw new RegistroJaCadastradoException("Nome do grupo já cadastrado!!!");
		}
		return repository.save(grupo);
	}
	

	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o grupo";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	public Grupo findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Grupo> findAll() {
		return repository.findAll();
	}



	public Page<Grupo> filtrar(GrupoFilter grupoFilter, Pageable pageable) {
		return repository.filtrar(grupoFilter, pageable);
	}

	public Grupo buscarGrupoComPermissoes(Long id) {
		return repository.buscarGrupoComPermissoes(id);
	}
}
