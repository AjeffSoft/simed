package com.ajeff.simed.financeiro.repository.helper.contaReceber;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.repository.filter.ContaReceberFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ContasReceberRepositoryImpl implements ContasReceberRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<ContaReceber> filtrar(ContaReceberFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaReceber.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}


	@Override
	@Transactional(readOnly = true)
	public ContaReceber buscarComPlanoConta(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaReceber.class);
		criteria.createAlias("planoContaSecundaria", "s", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("s.planoConta", "p", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (ContaReceber) criteria.uniqueResult();
	}
	
	@Transactional(readOnly = true)
	public BigDecimal totalGeral(ContaReceberFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaReceber.class);
		
		ProjectionList proj = Projections.projectionList();
		proj.add(Projections.sum("valor"));
		criteria.setProjection(proj);
		
		adicionarFiltro(filtro, criteria);
		
		BigDecimal result = (BigDecimal) criteria.uniqueResult();
		return result; 
	}
	

	
	private Long total(ContaReceberFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaReceber.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(ContaReceberFilter filtro, Criteria criteria) {
		criteria.createAlias("fornecedor", "f");
		criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.addOrder(Order.asc("vencimento"));
		
		
		if (filtro != null) {
			
			if (!StringUtils.isEmpty(filtro.getFornecedor())) {
				criteria.add(Restrictions.ilike("f.nome", filtro.getFornecedor(), MatchMode.ANYWHERE));
			}
			
			if(!StringUtils.isEmpty(filtro.getHistorico())) {
				criteria.add(Restrictions.ilike("historico", filtro.getHistorico(), MatchMode.ANYWHERE));
			}

			if(!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}
			
			if(filtro.getVencimentoInicio() != null) {
				criteria.add(Restrictions.ge("vencimento", filtro.getVencimentoInicio()));
			}
			
			if(filtro.getVencimentoFim() != null) {
				criteria.add(Restrictions.le("vencimento", filtro.getVencimentoFim()));
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
			
		}
	}
	
	private boolean isEmpresaPresente(ContaReceberFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}
	
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<RecebimentoPagoAberto> totalRecebimentoAbertoPendente(Usuario usuario) {
//		return manager.createNamedQuery("Recebimento.pagoAberto")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("mes", MonthDay.now().getMonthValue())
//				.getResultList();
//	}
//
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<RecebimentoPagoAberto> totalRecebimentoAbertoPendenteAno(Usuario usuario) {
//		return manager.createNamedQuery("Recebimento.pagoAbertoAno")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("ano", YearMonth.now().getYear())
//				.getResultList();
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<RecebimentoSemestre> totalRecebimentoSemestre(Usuario usuario) {
//		return manager.createNamedQuery("Recebimento.semestre")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.getResultList();
//	}


//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ContaReceberTrimestre> totalRecebimentoTrimestre(Usuario usuario) {
//		return manager.createNamedQuery("Recebimento.trimestre")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.getResultList();
//	}
}