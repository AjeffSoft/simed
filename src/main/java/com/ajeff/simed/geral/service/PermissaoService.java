package com.ajeff.simed.geral.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Permissao;
import com.ajeff.simed.geral.repository.PermissoesRepository;
import com.ajeff.simed.geral.repository.filter.PermissaoFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class PermissaoService {
	
	@Autowired
	private PermissoesRepository repository;
	
	@Transactional
	public Permissao salvar(Permissao permissao){
		Optional<Permissao> optional = repository.findByNomeIgnoreCase(permissao.getNome());
		
		if(optional.isPresent() && !optional.get().equals(permissao)){
			throw new RegistroJaCadastradoException("Nome da permissão já cadastrado!!!");
		}
		return repository.save(permissao);
	}
	

	
	@Transactional
	public void excluir(Long id) {
		String tipo = "a permissão";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	public Permissao findOne(Long id) {
		return repository.findOne(id);
	}

	public List<Permissao> findAll() {
		return repository.findAll();
	}



	public Page<Permissao> filtrar(PermissaoFilter permissaoFilter, Pageable pageable) {
		return repository.filtrar(permissaoFilter, pageable);
	}



	public List<Permissao> buscarTodasPermissoes() {
		return repository.buscarTodasPermissoes();
	}

}
