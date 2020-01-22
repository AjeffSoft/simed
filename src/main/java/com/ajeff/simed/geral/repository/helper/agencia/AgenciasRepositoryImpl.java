package com.ajeff.simed.geral.repository.helper.agencia;

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

import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.repository.filter.AgenciaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class AgenciasRepositoryImpl implements AgenciasRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Agencia> filtrar(AgenciaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Agencia.class);

		paginacaoUtil.prepararPaginacao(criteria, pageable);		

		adicionarFiltro(filtro, criteria);

		criteria.addOrder(Order.asc("agencia"));

		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(AgenciaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Agencia.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(AgenciaFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getAgencia())){
				criteria.add(Restrictions.ilike("agencia", filtro.getAgencia(), MatchMode.ANYWHERE));
			}
			
			if(!StringUtils.isEmpty(filtro.getSituacao())){
				criteria.add(Restrictions.eq("situacao", filtro.getSituacao()));
			}
		
			
			if (isBancoPresente(filtro)){
				criteria.add(Restrictions.eq("banco", filtro.getBanco()));
			}
		}
	}
	
	private boolean isBancoPresente(AgenciaFilter filtro) {
		return filtro.getBanco() != null && filtro.getBanco().getId() != null;
	}

}
