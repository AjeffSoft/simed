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
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class CooperadoService {

	
	@Autowired
	private CooperadosRepository repository;
	@Autowired
	private MedicoService medicoService;
	
	
	@Transactional
	public void salvar(Cooperado cooperado) {
		testeRegistroJaCadastrado(cooperado);		
		if (cooperado.isNovo()) {
			cooperado.setAtivo(true);
			ativarDesativarMedico(repository.save(cooperado));
		}else {
			repository.save(cooperado);
		}
		
	}
	
	

	public void ativarDesativarMedico(Cooperado cooperado) {
		medicoService.ativarDesativarCooperado(cooperado.getMedico());
	}



	public Cooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<Cooperado> filtrar(CooperadoFilter admissaoFilter, Pageable pageable) {
		return repository.filtrar(admissaoFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		Cooperado cooperado = repository.findOne(id);
		try {
			ativarDesativarMedico(cooperado);
			repository.delete(cooperado);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o(a) cooperado(a)!"); 
		}
		
	}

	private void testeRegistroJaCadastrado(Cooperado cooperado) {
		Optional<Cooperado> optional = repository.findByRegistroIgnoreCase(cooperado.getRegistro());
		
		if(optional.isPresent() && !optional.get().equals(cooperado)) {
			throw new RegistroJaCadastradoException("Registro de cooperado já cadastrado!");
		}
	}
	
	public List<Cooperado> findByAdmissaoCooperadoAtivoTrue() {
		return repository.findByAdmissaoCooperadoAtivoTrue();
	}

	public List<Cooperado> findByAdmissaoCooperadoAtivoFalse() {
		return repository.findByAdmissaoCooperadoAtivoFalse();
	}
	
	public List<Cooperado> findByAdmissaoOrdenadoPorCooperado() {
		return repository.findByAdmissaoOrdenadoPorCooperado();
	}



	public List<Cooperado> findAllAtivoTrueOrderByMedicoNome() {
		return repository.findAllAtivoTrueOrderByMedicoNome();
	}

}
