package com.ajeff.simed.cooperado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.CancelamentosRepository;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;

@Service
public class CancelamentoService {

	
	@Autowired
	private CancelamentosRepository repository;

	
	
	public Cooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<Cooperado> filtrar(CancelamentoFilter cooperadoFilter, Pageable pageable) {
		return repository.filtrar(cooperadoFilter, pageable);
	}

	public Object findByAtivoFalseOrderByRegistro() {
		return repository.findByAtivoFalseOrderByRegistro();
	}
	
//	@Transactional
//	public void excluir(Long id) {
//		Cooperado cooperado = repository.findOne(id);
//		try {
//			ativarDesativarMedico(cooperado);
//			repository.delete(cooperado);
//			repository.flush();
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o(a) cooperado(a)!"); 
//		}
//		
//	}


}
