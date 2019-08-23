package com.ajeff.simed.financeiro.repository.helper.contaReceber;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.repository.filter.ContaReceberFilter;

public interface ContasReceberRepositoryQueries {

	public Page<ContaReceber> filtrar(ContaReceberFilter filtro, Pageable pageable);
	
	public ContaReceber buscarComPlanoConta(Long id);
	
	public BigDecimal totalGeral(ContaReceberFilter filtro);
	
	
//	public List<RecebimentoPagoAberto> totalRecebimentoAbertoPendente(Usuario usuario);
//
//	public List<RecebimentoPagoAberto> totalRecebimentoAbertoPendenteAno(Usuario usuario);
//	
//	public List<RecebimentoSemestre> totalRecebimentoSemestre(Usuario usuario);

//	public List<ContaReceberTrimestre> totalRecebimentoTrimestre(Usuario usuario);
	
}
