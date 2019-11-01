package com.ajeff.simed.cooperado.repository.helper.demissao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.repository.filter.DemissaoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class DemissoesRepositoryImpl implements DemissoesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<DemissaoCooperado> filtrar(DemissaoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(DemissaoCooperado.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.addOrder(Order.asc("data"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	private Long total(DemissaoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(DemissaoCooperado.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(DemissaoFilter filtro, Criteria criteria) {
//		if (filtro !=null){
//			if(!StringUtils.isEmpty(filtro.getRegistro())){
//				criteria.add(Restrictions.ilike("registro", filtro.getRegistro()));
//			}			
//
//			if (filtro.getDataInicio() != null || filtro.getDataFim() != null) {
//				criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
//			}
//			
//			if (isCooperadoPresente(filtro)){
//				criteria.add(Restrictions.eq("cooperado", filtro.getCooperado()));
//			}
//
//		}
	}
	
	
//	private boolean isCooperadoPresente(AdmissaoFilter filtro) {
//		return filtro.getCooperado() != null && filtro.getCooperado().getId() != null;
//	}
	

}
