package com.ajeff.simed.cooperado.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.CooperadosRepository;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class CooperadoService {

	
	@Autowired
	private CooperadosRepository repository;
	
	
	@Transactional
	public void salvar(Cooperado cooperado) {
		testeRegistroJaCadastrado(cooperado);
		cooperado.setAtivo(true);
		repository.save(cooperado);
	}
	
	@Transactional
	public Boolean ativarCooperado(Cooperado cooperado) {
		cooperado.setAtivo(true);		
		repository.save(cooperado);
		return true;
	}
	
	@Transactional
	public Boolean desativarCooperado(Cooperado cooperado) {
		cooperado.setAtivo(false);		
		repository.save(cooperado);
		return false;
	}

	
	public Page<Cooperado> filtrar(CooperadoFilter cooperadoFilter, Pageable pageable) {
		return repository.filtrar(cooperadoFilter, pageable);
	}
	
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o(a) cooperado(a)";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}


	private void testeRegistroJaCadastrado(Cooperado cooperado) {
		Optional<Cooperado> optional = repository.findByDocumentoCpf(cooperado.getDocumento().getCpf());
		
		if(optional.isPresent() && !optional.get().equals(cooperado)) {
			throw new RegistroJaCadastradoException("CPF do cooperado já cadastrado!");
		}
	}


	public Cooperado buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}

	public List<Cooperado> listarCooperados() {
		return repository.listarCooperados();
	}

	public List<Cooperado> findByCooperadoAtivoFalse() {
		return repository.findByAtivoFalse();
	}

	public List<Cooperado> findByCooperadoAtivoTrue() {
		return repository.findByAtivoTrue();
	}

}
