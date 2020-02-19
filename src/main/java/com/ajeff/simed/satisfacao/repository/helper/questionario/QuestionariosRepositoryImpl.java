package com.ajeff.simed.satisfacao.repository.helper.questionario;

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
import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.repository.filter.QuestionarioFilter;

public class QuestionariosRepositoryImpl implements QuestionariosRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	@Autowired
	private PaginacaoUtil paginacaoUtil;		

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public Page<Questionario> filtrar(QuestionarioFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Questionario.class);

		paginacaoUtil.prepararPaginacao(criteria, pageable);		

		adicionarFiltro(filtro, criteria);

		criteria.addOrder(Order.asc("id"));

		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}

	private Long total(QuestionarioFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Questionario.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}	
	
	private void adicionarFiltro(QuestionarioFilter filtro, Criteria criteria) {
		if (filtro !=null){
			if(!StringUtils.isEmpty(filtro.getTipoResposta())){
				criteria.add(Restrictions.eq("tipoResposta", filtro.getTipoResposta()));
			}
			
			if (isEmpresaPresente(filtro)){
				criteria.add(Restrictions.eq("empresa", filtro.getEmpresa()));
			}
			
			if (isPerguntaPresente(filtro)){
				criteria.add(Restrictions.eq("pergunta", filtro.getEmpresa()));
			}
		}
	}
	
	private boolean isPerguntaPresente(QuestionarioFilter filtro) {
		return filtro.getPergunta() != null && filtro.getPergunta().getId() != null;
	}

	private boolean isEmpresaPresente(QuestionarioFilter filtro) {
		return filtro.getEmpresa() != null && filtro.getEmpresa().getId() != null;
	}	
}
