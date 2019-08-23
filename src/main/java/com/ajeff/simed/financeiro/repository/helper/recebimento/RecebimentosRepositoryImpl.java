package com.ajeff.simed.financeiro.repository.helper.recebimento;

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

import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.filter.RecebimentoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class RecebimentosRepositoryImpl implements RecebimentosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Recebimento> filtrar(RecebimentoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Recebimento.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	
	
	private Long total(RecebimentoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Recebimento.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	

	private void adicionarFiltro(RecebimentoFilter filtro, Criteria criteria) {
		
		criteria
		.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.addOrder(Order.desc("data"));
		
		if (filtro != null) {

			if (filtro.getDataInicio() != null || filtro.getDataFim() != null) {
				criteria.add(Restrictions.between("data", filtro.getDataInicio(), filtro.getDataFim()));
			}
			
			if(filtro.getValorInicio() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorInicio()));
			}
			
			if(filtro.getValorFim() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorFim()));
			}
			
			if(isEmpresaPresente(filtro)) {
				criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
			}
			
			if(isContaEmpresaPresente(filtro)) {
				criteria.add(Restrictions.eq("contaEmpresa", filtro.getContaEmpresa()));
			}
			
		}
	}
	
	private boolean isEmpresaPresente(RecebimentoFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}
	
	private boolean isContaEmpresaPresente(RecebimentoFilter filtro) {
		return filtro.getContaEmpresa() != null && filtro.getContaEmpresa().getId() != null;
	}

}