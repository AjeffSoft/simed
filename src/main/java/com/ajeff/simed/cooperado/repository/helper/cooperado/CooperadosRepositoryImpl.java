package com.ajeff.simed.cooperado.repository.helper.cooperado;

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

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.filter.CooperadoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class CooperadosRepositoryImpl implements CooperadosRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Cooperado> filtrar(CooperadoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cooperado.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.add(Restrictions.eq("ativo", true)).addOrder(Order.asc("data"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(CooperadoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cooperado.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(CooperadoFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getRegistro())){
				criteria.add(Restrictions.ilike("registro", filtro.getRegistro()));
			}			

			if(filtro.getDataInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getDataInicio()));
			}
			
			if(filtro.getDataFim() != null) {
				criteria.add(Restrictions.le("data", filtro.getDataFim()));
			}			
			
			if (isMedicoCooperadoPresente(filtro)){
				criteria.add(Restrictions.eq("medico", filtro.getMedicoCooperado()));
			}
		}
	}
	
	
	private boolean isMedicoCooperadoPresente(CooperadoFilter filtro) {
		return filtro.getMedicoCooperado() != null && filtro.getMedicoCooperado().getId() != null;
	}
	

}
