package com.ajeff.simed.financeiro.repository.helper.contaPagar;

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

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.repository.filter.ContaPagarFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class ContasPagarRepositoryImpl implements ContasPagarRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	

	//MÉTODO DA PESQUISA DE CONTA A PAGAR
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<ContaPagar> filtrar(ContaPagarFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltroTodos(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, totalTodos(filtro));
	}

	private void adicionarFiltroTodos(ContaPagarFilter filtro, Criteria criteria) {
		criteria.createAlias("fornecedor", "f");
		criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.addOrder(Order.asc("vencimento"));
		camposParaFiltro(filtro, criteria);
	}
	
	private Long totalTodos(ContaPagarFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		adicionarFiltroTodos(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	

	//MÉTODO DA PESQUISA DE CONTA A PAGAR - SOMENTE AS CONTAS COM STATUS ABERTO
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<ContaPagar> filtrarAutorizar(ContaPagarFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltroAutorizar(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, totalAutorizar(filtro));
	}
	
	private Long totalAutorizar(ContaPagarFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		adicionarFiltroAutorizar(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	private void adicionarFiltroAutorizar(ContaPagarFilter filtro, Criteria criteria) {
		criteria.createAlias("fornecedor", "f");
		criteria
		.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.add(Restrictions.eq("status", "ABERTO"))
		.addOrder(Order.asc("vencimento"))
		.addOrder(Order.desc("valor"));
		camposParaFiltro(filtro, criteria);
	}	

	
	
	//MÉTODO DA PESQUISA DE CONTA A PAGAR - SOMENTE AS CONTAS COM STATUS AUTORIZADA
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<ContaPagar> filtrarAutorizadas(ContaPagarFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltroAutorizadas(filtro, criteria);
		return new PageImpl<>(criteria.list(), pageable, totalAutorizadas(filtro));
	}
	
	private Long totalAutorizadas(ContaPagarFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		adicionarFiltroAutorizar(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}
	
	private void adicionarFiltroAutorizadas(ContaPagarFilter filtro, Criteria criteria) {
		criteria.createAlias("fornecedor", "f");
		criteria
		.add(Restrictions.eq("empresa", filtro.getEmpresa()))
		.add(Restrictions.eq("status", "AUTORIZADO"))
		.addOrder(Order.asc("vencimento"))
		.addOrder(Order.desc("valor"));
		camposParaFiltro(filtro, criteria);
	}	


	
	@Override
	@Transactional(readOnly = true)
	public ContaPagar buscarComPlanoConta(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		criteria.createAlias("planoContaSecundaria", "s", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("s.planoConta", "p", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		return (ContaPagar) criteria.uniqueResult();
	}	
	

	@Transactional(readOnly = true)
	public BigDecimal total(ContaPagarFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(ContaPagar.class);
		criteria.createAlias("fornecedor", "f");
		
		ProjectionList proj = Projections.projectionList();
		proj.add(Projections.sum("valor"));
		criteria.setProjection(proj);
		
		camposParaFiltro(filtro, criteria);
		
		BigDecimal result = (BigDecimal) criteria.uniqueResult();
		return result; 
	}

	

	private void camposParaFiltro(ContaPagarFilter filtro, Criteria criteria) {
		
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
			
			if (filtro.getVencimentoInicio() != null || filtro.getVencimentoFim() != null) {
				criteria.add(Restrictions.between("vencimento", filtro.getVencimentoInicio(), filtro.getVencimentoFim()));
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
			
			if(!StringUtils.isEmpty(filtro.getPendente())) {
				criteria.add(Restrictions.eq("pendente", filtro.getPendente()));
			}
		
		}
	}
	
	private boolean isEmpresaPresente(ContaPagarFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}

//	@Override
//	public BigDecimal totalGeral() {
//		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("select sum(valor) from ContaPagar", BigDecimal.class).getSingleResult());
//		return optional.orElse(BigDecimal.ZERO);
//	}

	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ContaPagarTrimestre> totalContaPagarTrimestre(Usuario usuario) {
//		return manager.createNamedQuery("Pagamento.trimestre")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.getResultList();
//	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ReceberXpagar> listarReceberEpagarMes(Usuario usuario) {
//		return manager.createNamedQuery("Saldo.receberXpagarMes")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("mes", MonthDay.now().getMonthValue())
//				.getResultList();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<ReceberXpagar> listarReceberEpagarAno(Usuario usuario) {
//		return manager.createNamedQuery("Saldo.receberXpagarAno")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("ano", YearMonth.now().getYear())
//				.getResultList();
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<PagarXsaldoContas> listarPagarEsaldoContas(Usuario usuario) {
//		return manager.createNamedQuery("Saldo.contasPagarESaldoConta")
//				.setParameter("emp", usuario.getEmpresa().getId())
//				.setParameter("mes", MonthDay.now().getMonthValue())
//				.getResultList();
//	}

}