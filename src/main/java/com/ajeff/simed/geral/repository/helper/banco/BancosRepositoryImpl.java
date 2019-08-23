package com.ajeff.simed.geral.repository.helper.banco;

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

import com.ajeff.simed.geral.model.Banco;
import com.ajeff.simed.geral.repository.filter.BancoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class BancosRepositoryImpl implements BancosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Banco> filtrar(BancoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Banco.class);
		
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		
		adicionarFiltro(filtro, criteria);
		
		criteria.addOrder(Order.asc("nome"));
		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(BancoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Banco.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(BancoFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getCodigo())) {
				criteria.add(Restrictions.ilike("codigo", filtro.getCodigo(), MatchMode.ANYWHERE));
			}
		}
	}

}