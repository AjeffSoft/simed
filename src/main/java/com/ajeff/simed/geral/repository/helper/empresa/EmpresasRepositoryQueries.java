package com.ajeff.simed.geral.repository.helper.empresa;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.filter.EmpresaFilter;

public interface EmpresasRepositoryQueries {
	
	public Page<Empresa> filtrar(EmpresaFilter filtro, Pageable pageable);
	
	public Empresa buscarComCidadeEstado(Long id);
	
	public List<Empresa> buscarTodasEmpresas();
	
	

}
