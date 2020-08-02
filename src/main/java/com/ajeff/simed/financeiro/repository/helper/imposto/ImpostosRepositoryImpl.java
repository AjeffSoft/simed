package com.ajeff.simed.financeiro.repository.helper.imposto;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Predicate;

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
import com.ajeff.simed.financeiro.model.TabelaIRPF;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.filter.ImpostoFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ImpostosRepositoryImpl implements ImpostosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	@Autowired
	private TabelasIrpfRepository tabelaRepository;
	

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
		criteria.createAlias("contaPagar", "c");
		criteria.createAlias("c.fornecedor", "f");
		criteria.createAlias("c.empresa", "e");
		criteria.addOrder(Order.asc("vencimento"));
		
		if (filtro != null) {
			
			if (!StringUtils.isEmpty(filtro.getFornecedor())) {
				criteria.add(Restrictions.ilike("f.nome", filtro.getFornecedor(), MatchMode.ANYWHERE));
			}

			if (!StringUtils.isEmpty(filtro.getNumeroNF())) {
				criteria.add(Restrictions.eq("numeroNF", filtro.getNumeroNF()));
			}
			
			if(!StringUtils.isEmpty(filtro.getNome())) {
				criteria.add(Restrictions.eq("nome", filtro.getNome()));
			}

			if(!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}
			
			if (filtro.getApuracaoInicio() != null) {
				criteria.add(Restrictions.ge("apuracao", filtro.getApuracaoInicio()));
			}

			if (filtro.getApuracaoFim() != null) {
				criteria.add(Restrictions.le("apuracao", filtro.getApuracaoFim()));
			}
			
			if(filtro.getValorInicio() != null) {
				criteria.add(Restrictions.ge("total", filtro.getValorInicio()));
			}
			
			if(filtro.getValorFim() != null) {
				criteria.add(Restrictions.le("total", filtro.getValorFim()));
			}
			
			if(filtro.getEmpresa() != null) {
				criteria.add(Restrictions.eq("e.fantasia", filtro.getEmpresa()));
			}
			

		}
	}


	
}