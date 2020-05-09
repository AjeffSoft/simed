package com.ajeff.simed.cooperado.repository.helper.medico;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.filter.MedicoFilter;

@Repository
public interface MedicosRepositoryQueries {
	
	Page<Medico> filtrar(MedicoFilter filtro, Pageable pageable);
	
	public Medico buscarComCidadeEstado(Long id);
	
	
}
