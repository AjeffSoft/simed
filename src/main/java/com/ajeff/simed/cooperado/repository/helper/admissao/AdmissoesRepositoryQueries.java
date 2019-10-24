package com.ajeff.simed.cooperado.repository.helper.admissao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.filter.AdmissaoFilter;

public interface AdmissoesRepositoryQueries {
	
	Page<AdmissaoCooperado> filtrar(AdmissaoFilter filtro, Pageable pageable);
	

	
}
