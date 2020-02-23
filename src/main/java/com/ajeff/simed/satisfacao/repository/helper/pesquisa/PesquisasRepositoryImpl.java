package com.ajeff.simed.satisfacao.repository.helper.pesquisa;

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

import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.filter.PesquisaFilter;

public class PesquisasRepositoryImpl implements PesquisasRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Pesquisa> filtrar(PesquisaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Pesquisa.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(PesquisaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Pesquisa.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(PesquisaFilter filtro, Criteria criteria) {
		criteria.createAlias("usuario", "u");
		criteria.createAlias("u.empresa", "e");
		criteria.addOrder(Order.asc("data"));
		
		
		if (filtro != null) {
			if(!StringUtils.isEmpty(filtro.getFechado())) {
				criteria.add(Restrictions.eq("fechado", filtro.getFechado()));
			}
			
			if(filtro.getDataInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getDataInicio()));
			}
			
			if(filtro.getDataFim() != null) {
				criteria.add(Restrictions.le("data", filtro.getDataFim()));
			}
			
			if(isEmpresaPresente(filtro)) {
				criteria.add(Restrictions.eq("u.empresa", filtro.getEmpresa()));
			}

			if(isUsuarioPresente(filtro)) {
				criteria.add(Restrictions.eq("usuario", filtro.getUsuario()));
			}		
		}
	}
	
	private boolean isEmpresaPresente(PesquisaFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}

	private boolean isUsuarioPresente(PesquisaFilter filtro) {
		return filtro.getUsuario() != null && filtro.getUsuario().getId() != null;
	}	
}