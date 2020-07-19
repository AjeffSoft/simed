package com.ajeff.simed.geral.repository.helper.empresa;

import java.util.List;

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

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.filter.EmpresaFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class EmpresasRepositoryImpl implements EmpresasRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)	
	public Page<Empresa> filtrar(EmpresaFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Empresa.class);

		paginacaoUtil.prepararPaginacao(criteria, pageable);

		adicionarFiltro(filtro, criteria);

		criteria.addOrder(Order.asc("nome"));

		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(EmpresaFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Empresa.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(EmpresaFilter filtro, Criteria criteria) {
		if(filtro != null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}
			
			if(!StringUtils.isEmpty(filtro.getCnpj())){
				criteria.add(Restrictions.eq("cnpj", filtro.getCnpj()));
			}

		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Empresa buscarComCidadeEstado(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Empresa.class);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (Empresa) criteria.uniqueResult();
	}

	@Override
	public List<Empresa> buscarTodasEmpresas() {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Empresa.class);
		criteria.addOrder(Order.asc("nome"));
		return criteria.list();
	}	
	
}
