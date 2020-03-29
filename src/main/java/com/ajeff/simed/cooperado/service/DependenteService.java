package com.ajeff.simed.cooperado.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.repository.DependentesRepository;
import com.ajeff.simed.cooperado.repository.filter.DependenteFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class DependenteService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(DependenteService.class);	
	
	@Autowired
	private DependentesRepository repository;
	
	
	@Transactional
	public void salvar(Dependente dependente) {
		testeRegistroJaCadastrado(dependente);
		
		repository.save(dependente);
	}
	

	public Page<Dependente> filtrar(DependenteFilter dependenteFilter, Pageable pageable) {
		return repository.filtrar(dependenteFilter, pageable);
	}	
	
//	public Integer qtdDependentes(Cooperado cooperado) {
//		List<Dependente> depends = repository.findByCooperado(cooperado);
//		Integer i = 0;
//		for (Dependente d : depends) {
//			i++;
//		}
//		return i;
//	}
	
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o(a) dependente(a)";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}


	private void testeRegistroJaCadastrado(Dependente dependente) {
		Optional<Dependente> optional = repository.findByNomeIgnoreCase(dependente.getNome());
		
		if(optional.isPresent() && !optional.get().equals(dependente)) {
			throw new RegistroJaCadastradoException("Nome de dependente já cadastrado!");
		}
	}


	public Dependente findOne(Long id) {
		return repository.findOne(id);
	}


//	public Cooperado buscarComCidadeEstado(Long id) {
//		return repository.buscarComCidadeEstado(id);
//	}
//
//	public List<Cooperado> listarCooperados() {
//		return repository.listarCooperados();
//	}

}
