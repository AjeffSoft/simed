package com.ajeff.simed.cooperado.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.AdmissoesRepository;
import com.ajeff.simed.cooperado.repository.filter.AdmissaoFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class AdmissaoService {

	
	@Autowired
	private AdmissoesRepository repository;
	@Autowired
	private CooperadoService cooperadoService;
	
	
	@Transactional
	public void salvar(AdmissaoCooperado admissao) {
		if (admissao.isNovo()) {
			admissao.setAtivo(cooperadoService.ativarCooperado(admissao.getCooperado()));
		}
		
		testeRegistroJaCadastrado(admissao);		
		repository.save(admissao);
	}
	
	
	@Transactional
	public void ativarAdmissao(AdmissaoCooperado admissao) {
		admissao.setAtivo(cooperadoService.ativarCooperado(admissao.getCooperado()));
		repository.save(admissao);
	}
	
	@Transactional
	public void desativarAdmissao(AdmissaoCooperado admissao) {
		admissao.setAtivo(cooperadoService.desativarCooperado(admissao.getCooperado()));
		repository.save(admissao);
	}

	public AdmissaoCooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<AdmissaoCooperado> filtrar(AdmissaoFilter admissaoFilter, Pageable pageable) {
		return repository.filtrar(admissaoFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		AdmissaoCooperado admissao = repository.findOne(id);
		try {
			cooperadoService.desativarCooperado(admissao.getCooperado());
			repository.delete(admissao);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a admissão. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}

	private void testeRegistroJaCadastrado(AdmissaoCooperado admissao) {
		Optional<AdmissaoCooperado> optional = repository.findByRegistroIgnoreCase(admissao.getRegistro());
		
		if(optional.isPresent() && !optional.get().equals(admissao)) {
			throw new RegistroJaCadastradoException("Registro de admissão já cadastrado!");
		}
	}
	
	public List<AdmissaoCooperado> findByAdmissaoCooperadoAtivoTrue() {
		return repository.findByAdmissaoCooperadoAtivoTrue();
	}

	public List<AdmissaoCooperado> findByAdmissaoCooperadoAtivoFalse() {
		return repository.findByAdmissaoCooperadoAtivoFalse();
	}
	
	public List<AdmissaoCooperado> findByAdmissaoOrdenadoPorCooperado() {
		return repository.findByAdmissaoOrdenadoPorCooperado();
	}

}
