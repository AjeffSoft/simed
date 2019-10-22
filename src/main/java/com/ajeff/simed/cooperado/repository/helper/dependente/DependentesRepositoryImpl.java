package com.ajeff.simed.cooperado.repository.helper.dependente;

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

import com.ajeff.simed.cooperado.model.Dependente;
import com.ajeff.simed.cooperado.repository.filter.DependenteFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class DependentesRepositoryImpl implements DependentesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Dependente> filtrar(DependenteFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Dependente.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.addOrder(Order.asc("nome"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(DependenteFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Dependente.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(DependenteFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}			

			if(!StringUtils.isEmpty(filtro.getCpf())){
				criteria.add(Restrictions.eq("documento.cpf", filtro.getCpf()));
			}

			if (isCooperadoPresente(filtro)){
				criteria.add(Restrictions.eq("cooperado", filtro.getCooperado()));
			}

		}
	}
	
	
	
	private boolean isCooperadoPresente(DependenteFilter filtro) {
		return filtro.getCooperado() != null && filtro.getCooperado().getId() != null;
	}
	

}
