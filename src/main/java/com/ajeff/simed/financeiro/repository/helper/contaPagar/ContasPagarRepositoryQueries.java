package com.ajeff.simed.financeiro.repository.helper.contaPagar;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;

public interface ContasPagarRepositoryQueries {

	public Page<ContaPagar> filtrar(ContaPagarFilter filtro, Pageable pageable);

//	public Page<ContaPagar> filtrarCadastradas(ContaPagarFilter filtro, Pageable pageable, Usuario usuarioSistema);
//	
	public Page<ContaPagar> filtrarAutorizar(ContaPagarFilter filtro, Pageable pageable);

	public Page<ContaPagar> filtrarAutorizadas(ContaPagarFilter filtro, Pageable pageable);
	
	//	
//	public Page<ContaPagar> filtrarAutorizadoPagar(ContaPagarFilter filtro, Pageable pageable, Usuario usuarioSistema);	
	
	public ContaPagar buscarComPlanoConta(Long id);
	
	public BigDecimal total(ContaPagarFilter filtro);
	
//	public List<ContaPagarTrimestre> totalContaPagarTrimestre(Usuario usuario);

//	public List<ReceberXpagar> listarReceberEpagarMes(Usuario usuario);
//
//	public List<ReceberXpagar> listarReceberEpagarAno(Usuario usuario);
//	
//	public List<PagarXsaldoContas> listarPagarEsaldoContas(Usuario usuario);
	
	
}
