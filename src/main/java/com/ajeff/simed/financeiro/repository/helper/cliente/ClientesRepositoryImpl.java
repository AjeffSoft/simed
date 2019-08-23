package com.ajeff.simed.financeiro.repository.helper.cliente;

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

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ClientesRepositoryImpl implements ClientesRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Fornecedor> filtrar(FornecedorFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Fornecedor.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);		
		adicionarFiltro(filtro, criteria);
		criteria.add(Restrictions.eq("clifor", false)).addOrder(Order.asc("nome"));
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	
	private Long total(FornecedorFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Fornecedor.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(FornecedorFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getNome())){
				criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
			}

			if(!StringUtils.isEmpty(filtro.getDocumento1())){
				criteria.add(Restrictions.eq("documento1", filtro.getDocumento1()));
			}
			
			if (isCidadePresente(filtro)){
				criteria.add(Restrictions.eq("endereco.cidade", filtro.getCidade()));
			}

		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public Fornecedor buscarComCidadeEstado(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Fornecedor.class);
		criteria.createAlias("endereco.cidade", "c", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("c.estado", "e", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (Fornecedor) criteria.uniqueResult();
	}
	
	
	
	private boolean isCidadePresente(FornecedorFilter filtro) {
		return filtro.getCidade() != null && filtro.getCidade().getId() != null;
	}


}
