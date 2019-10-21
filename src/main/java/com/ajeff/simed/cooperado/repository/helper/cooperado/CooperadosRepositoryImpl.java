package com.ajeff.simed.cooperado.repository.helper.cooperado;

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
		criteria.addOrder(Order.asc("nome"));
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
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}			

			if(!StringUtils.isEmpty(filtro.getCpf())){
				criteria.add(Restrictions.eq("documento.cpf", filtro.getCpf()));
			}

			if(!StringUtils.isEmpty(filtro.getCrm())){
				criteria.add(Restrictions.eq("crm", filtro.getCpf()));
			}
			
			if (isCidadePresente(filtro)){
				criteria.add(Restrictions.eq("endereco.cidade", filtro.getCidade()));
			}

		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Cooperado buscarComCidadeEstado(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Cooperado.class);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (Cooperado) criteria.uniqueResult();
	}
	
	
	
	private boolean isCidadePresente(CooperadoFilter filtro) {
		return filtro.getCidade() != null && filtro.getCidade().getId() != null;
	}
	

}
