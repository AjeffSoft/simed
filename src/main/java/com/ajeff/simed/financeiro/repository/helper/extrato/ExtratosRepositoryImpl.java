package com.ajeff.simed.financeiro.repository.helper.extrato;

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

import com.ajeff.simed.financeiro.model.Extrato;
import com.ajeff.simed.financeiro.repository.filter.ExtratoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ExtratosRepositoryImpl implements ExtratosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Extrato> filtrar(ExtratoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Extrato.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
		
	private Long total(ExtratoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Extrato.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ExtratoFilter filtro, Criteria criteria) {
		criteria
		.add(Restrictions.eq("contaBancaria", filtro.getContaEmpresa()))
		.add(Restrictions.ge("data", filtro.getMovimentacao().getMovimentacao().getDataInicio()))
		.add(Restrictions.le("data", filtro.getMovimentacao().getMovimentacao().getDataFinal()))
		.add(Restrictions.eq("contaBancaria", filtro.getContaEmpresa()))
		.addOrder(Order.asc("data"))
		.addOrder(Order.asc("id"));
		
		if (filtro != null) {
			if(filtro.getDataInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getMovimentacao().getMovimentacao().getDataInicio()));
			}
			
			if(filtro.getDataFinal() != null) {
				criteria.add(Restrictions.le("data", filtro.getMovimentacao().getMovimentacao().getDataFinal()));
			}
			
			if(isMovimentacaoPresente(filtro)) {
				criteria.add(Restrictions.eq("movimentacaoItem", filtro.getMovimentacao()));
				
			}
		}
	}
	
	private boolean isMovimentacaoPresente(ExtratoFilter filtro) {
		return filtro.getMovimentacao() != null && filtro.getMovimentacao().getId() != null;
	}
}