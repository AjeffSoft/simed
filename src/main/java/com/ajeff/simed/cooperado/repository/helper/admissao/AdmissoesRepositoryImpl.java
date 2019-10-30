package com.ajeff.simed.cooperado.repository.helper.admissao;

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

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.filter.AdmissaoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class AdmissoesRepositoryImpl implements AdmissoesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<AdmissaoCooperado> filtrar(AdmissaoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(AdmissaoCooperado.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.addOrder(Order.asc("data"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(AdmissaoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(AdmissaoCooperado.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(AdmissaoFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getRegistro())){
				criteria.add(Restrictions.ilike("registro", filtro.getRegistro()));
			}			

			if (filtro.getDataInicio() != null || filtro.getDataFim() != null) {
				criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
			}
			
			if (isCooperadoPresente(filtro)){
				criteria.add(Restrictions.eq("cooperado", filtro.getCooperado()));
			}

		}
	}
	
	
	private boolean isCooperadoPresente(AdmissaoFilter filtro) {
		return filtro.getCooperado() != null && filtro.getCooperado().getId() != null;
	}
	

}
