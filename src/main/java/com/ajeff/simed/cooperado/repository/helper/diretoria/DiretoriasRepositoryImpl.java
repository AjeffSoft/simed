package com.ajeff.simed.cooperado.repository.helper.diretoria;

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

import com.ajeff.simed.cooperado.model.Diretoria;
import com.ajeff.simed.cooperado.repository.filter.DiretoriaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class DiretoriasRepositoryImpl implements DiretoriasRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Diretoria> filtrar(DiretoriaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Diretoria.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.addOrder(Order.asc("chapa"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(DiretoriaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Diretoria.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(DiretoriaFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getDescricao())){
				criteria.add(Restrictions.ilike("descricao", filtro.getDescricao(), MatchMode.ANYWHERE));
			}			

			if(!StringUtils.isEmpty(filtro.getChapa())){
				criteria.add(Restrictions.eq("chapa", filtro.getChapa()));
			}
		}
	}
	

}
