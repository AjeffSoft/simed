package com.ajeff.simed.financeiro.repository.helper.imposto;

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

import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ImpostosRepositoryImpl implements ImpostosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Imposto> filtrar(ImpostoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Imposto.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	
	private Long total(ImpostoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Imposto.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ImpostoFilter filtro, Criteria criteria) {
		criteria.createAlias("contaPagarOrigem", "c");
		criteria.createAlias("c.fornecedor", "f");
		criteria.createAlias("c.empresa", "e");
		criteria.addOrder(Order.asc("vencimento"));
		
		if (filtro != null) {
			
			if (!StringUtils.isEmpty(filtro.getFornecedor())) {
				criteria.add(Restrictions.ilike("f.nome", filtro.getFornecedor(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getEmpresa())) {
				criteria.add(Restrictions.eq("e.fantasia", filtro.getEmpresa()));
			}
			
			if (!StringUtils.isEmpty(filtro.getNumeroNF())) {
				criteria.add(Restrictions.eq("numeroNF", filtro.getNumeroNF()));
			}
			
			if(!StringUtils.isEmpty(filtro.getCodigo())) {
				criteria.add(Restrictions.eq("codigo", filtro.getCodigo()));
			}

			if(!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}
			
			if (filtro.getApuracaoInicio() != null || filtro.getApuracaoFim() != null) {
				criteria.add(Restrictions.between("apuracao", filtro.getApuracaoInicio(), filtro.getApuracaoFim()));
			}
			
			if(filtro.getValorInicio() != null) {
				criteria.add(Restrictions.ge("total", filtro.getValorInicio()));
			}
			
			if(filtro.getValorFim() != null) {
				criteria.add(Restrictions.le("total", filtro.getValorFim()));
			}
			
//			if(isEmpresaPresente(filtro)) {
//				criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
//			}
			
		}
	}
	
//	private boolean isEmpresaPresente(ImpostoFilter filtro) {
//		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
//	}
	
}