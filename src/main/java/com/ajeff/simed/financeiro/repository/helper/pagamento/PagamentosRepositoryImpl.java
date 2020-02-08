package com.ajeff.simed.financeiro.repository.helper.pagamento;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.repository.filter.PagamentoFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class PagamentosRepositoryImpl implements PagamentosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Pagamento> filtrar(PagamentoFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Pagamento.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	
	
	private Long total(PagamentoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Pagamento.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	
	@Transactional(readOnly = true)
	public BigDecimal totalGeral(PagamentoFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Pagamento.class);
		ProjectionList proj = Projections.projectionList();
		proj.add(Projections.sum("valor"));
		criteria.setProjection(proj);
		
		adicionarFiltro(filtro, criteria);
		
		BigDecimal result = (BigDecimal) criteria.uniqueResult();
		return result; 
	}
	

	private void adicionarFiltro(PagamentoFilter filtro, Criteria criteria) {
		criteria.createAlias("contaEmpresa", "c");
		criteria.createAlias("c.empresa", "e");
		criteria.addOrder(Order.asc("data")).addOrder(Order.desc("id")).addOrder(Order.asc("documento"));
		
		
		if (filtro != null) {
			
			if(!StringUtils.isEmpty(filtro.getTipo())) {
				criteria.add(Restrictions.eq("tipo", filtro.getTipo()));
			}

			if(!StringUtils.isEmpty(filtro.getStatus())) {
				criteria.add(Restrictions.eq("status", filtro.getStatus()));
			}
			
			if(!StringUtils.isEmpty(filtro.getDocumento())) {
				criteria.add(Restrictions.ilike("documento", filtro.getDocumento(), MatchMode.ANYWHERE));
			}

			if (filtro.getDataInicio() != null) {
				criteria.add(Restrictions.ge("data", filtro.getDataInicio()));
			}

			if (filtro.getDataFim() != null) {
				criteria.add(Restrictions.le("data", filtro.getDataFim()));
			}
			
			if(filtro.getValorInicio() != null) {
				criteria.add(Restrictions.ge("valor", filtro.getValorInicio()));
			}
			
			if(filtro.getValorFim() != null) {
				criteria.add(Restrictions.le("valor", filtro.getValorFim()));
			}

			if(filtro.getEmpresa() != null) {
				criteria.add(Restrictions.eq("e.fantasia", filtro.getEmpresa()));
			}			
		}
	}
	


//	@Override
//	public BigDecimal totalPagamentosNoMes(Usuario usuario) {
//		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("select sum(valor) from Pagamento where empresa = :emp and month(data) = :mes and pago = true", BigDecimal.class)
//				.setParameter("emp", usuario.getEmpresa())
//				.setParameter("mes", MonthDay.now().getMonthValue())
//				.getSingleResult());
//		return optional.orElse(BigDecimal.ZERO);
//	}
//
//	@Override
//	public BigDecimal totalPagamentosNoAno(Usuario usuario) {
//		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("select sum(valor) from Pagamento where empresa = :emp and year(data) = :ano and pago = true", BigDecimal.class)
//				.setParameter("emp", usuario.getEmpresa())
//				.setParameter("ano", YearMonth.now().getYear())
//				.getSingleResult());
//		return optional.orElse(BigDecimal.ZERO);
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<PagamentoSemestre> totalPagamentoSemestre(Usuario usuario) {
//		List<PagamentoSemestre> pagamentoSemestre =  manager.createNamedQuery("Pagamento.semestre")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.getResultList();
//		return pagamentoSemestre;
//	}
//	
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ContaPagarContaXpagamento> contaPagarXpagamentoMes(Usuario usuario) {
//		return manager.createNamedQuery("ContaPagar.contaPagarXpagamento")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("mes", MonthDay.now().getMonthValue())
//				.getResultList();
//	}
//	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ContaPagarContaXpagamento> contaPagarXpagamentoAno(Usuario usuario) {
//		return manager.createNamedQuery("ContaPagar.contaPagarXpagamentoAno")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("ano", YearMonth.now().getYear())
//				.getResultList();
//	}



}