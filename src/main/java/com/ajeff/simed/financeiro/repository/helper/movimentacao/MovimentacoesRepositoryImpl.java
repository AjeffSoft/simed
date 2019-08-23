package com.ajeff.simed.financeiro.repository.helper.movimentacao;

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

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.repository.filter.MovimentacaoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class MovimentacoesRepositoryImpl implements MovimentacoesRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Movimentacao> filtrar(MovimentacaoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Movimentacao.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	
	
	private Long total(MovimentacaoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Movimentacao.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(MovimentacaoFilter filtro, Criteria criteria) {
		
		criteria
		.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.addOrder(Order.desc("dataInicio"))
				.addOrder(Order.desc("id"));
		
		if (filtro != null) {
			
			if(!StringUtils.isEmpty(filtro.getFechado())) {
				criteria.add(Restrictions.eq("fechado", filtro.getFechado()));
			}
			
			if(isEmpresaPresente(filtro)) {
				criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
			}
			
		}
	}
	
	private boolean isEmpresaPresente(MovimentacaoFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}
	
}