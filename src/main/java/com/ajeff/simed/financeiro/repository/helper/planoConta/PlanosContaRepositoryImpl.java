package com.ajeff.simed.financeiro.repository.helper.planoConta;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.repository.filter.PlanoContaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class PlanosContaRepositoryImpl implements PlanosContaRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<PlanoConta> filtrar(PlanoContaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(PlanoConta.class);
		
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		
		adicionarFiltro(filtro, criteria);
		
		criteria.addOrder(Order.asc("nome"));
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(PlanoContaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(PlanoConta.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(PlanoContaFilter filtro, Criteria criteria) {
		if (filtro != null) {
			
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getTipo())) {
				criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
			}
		}
	}

}