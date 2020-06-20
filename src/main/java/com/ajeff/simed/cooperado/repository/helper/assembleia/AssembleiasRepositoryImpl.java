package com.ajeff.simed.cooperado.repository.helper.assembleia;

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

import com.ajeff.simed.cooperado.model.Assembleia;
import com.ajeff.simed.cooperado.repository.filter.AssembleiaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class AssembleiasRepositoryImpl implements AssembleiasRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Assembleia> filtrar(AssembleiaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Assembleia.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.addOrder(Order.asc("data"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(AssembleiaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Assembleia.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(AssembleiaFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getDescricao())){
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}			

			if(!StringUtils.isEmpty(filtro.getResumo())){
				criteria.add(Restrictions.ilike("resumo", filtro.getResumo(), MatchMode.ANYWHERE));
			}			

			if(!StringUtils.isEmpty(filtro.getTipoAssembleia())){
				criteria.add(Restrictions.eq("tipoAssembleia", filtro.getTipoAssembleia()));
			}

			if(filtro.getDataInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getDataInicio()));
			}
			
			if(filtro.getDataFim() != null) {
				criteria.add(Restrictions.le("data", filtro.getDataFim()));
			}
		}
	}
	

}
