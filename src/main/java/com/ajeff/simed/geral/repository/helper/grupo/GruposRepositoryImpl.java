package com.ajeff.simed.geral.repository.helper.grupo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.model.Grupo;
import com.ajeff.simed.geral.repository.filter.GrupoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class GruposRepositoryImpl implements GruposRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Grupo> filtrar(GrupoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Grupo.class);

		paginacaoUtil.prepararPaginacao(criteria, pageable);

		adicionarFiltro(filtro, criteria);

		criteria.addOrder(Order.asc("nome"));

		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(GrupoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Grupo.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(GrupoFilter filtro, Criteria criteria) {
		if(filtro != null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
		}
	}
	
	@Transactional(readOnly = true)
	@Override
	public Grupo buscarGrupoComPermissoes(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Grupo.class);
		criteria.createAlias("permissoes", "p", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Grupo) criteria.uniqueResult();
	}		
}
