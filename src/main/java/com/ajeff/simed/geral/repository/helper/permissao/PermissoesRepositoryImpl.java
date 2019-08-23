package com.ajeff.simed.geral.repository.helper.permissao;

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

import com.ajeff.simed.geral.model.Permissao;
import com.ajeff.simed.geral.repository.filter.PermissaoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class PermissoesRepositoryImpl implements PermissoesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Permissao> filtrar(PermissaoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Permissao.class);

		paginacaoUtil.prepararPaginacao(criteria, pageable);

		adicionarFiltro(filtro, criteria);

		criteria.addOrder(Order.asc("nome"));

		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(PermissaoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Permissao.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(PermissaoFilter filtro, Criteria criteria) {
		if(filtro != null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
		}
	}
}
