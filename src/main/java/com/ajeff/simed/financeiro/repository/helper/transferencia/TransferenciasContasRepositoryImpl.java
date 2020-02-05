package com.ajeff.simed.financeiro.repository.helper.transferencia;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.filter.TransferenciaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class TransferenciasContasRepositoryImpl implements TransferenciasContasRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<TransferenciaContas> filtrar(TransferenciaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(TransferenciaContas.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(TransferenciaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(TransferenciaContas.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(TransferenciaFilter filtro, Criteria criteria) {
		criteria.createAlias("contaOrigem", "c");
		criteria.createAlias("c.empresa", "e");
		criteria.addOrder(Order.asc("data"));

		
		if (filtro != null) {

			if(!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}

			if(!StringUtils.isEmpty(filtro.getTipo())) {
				criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
			}
			
			if (filtro.getVencimentoInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getVencimentoInicio()));
			}

			if (filtro.getVencimentoFim() != null) {
				criteria.add(Restrictions.le("data", filtro.getVencimentoFim()));
			}
			
			if(filtro.getValorInicio() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorInicio()));
			}
			
			if(filtro.getValorFim() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorFim()));
			}

			if(filtro.getEmpresa() != null) {
				criteria.add(Restrictions.eq("e.fantasia", filtro.getEmpresa()));
			}	
			
			if(isContaOrigemPresente(filtro)) {
				criteria.add(Restrictions.eq("contaOrigem", filtro.getContaOrigem()));
			}
		}
	}
	
	private boolean isContaOrigemPresente(TransferenciaFilter filtro) {
		return filtro.getContaOrigem() != null && filtro.getContaOrigem().getId() != null;
	}

	
}