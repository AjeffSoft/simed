package com.ajeff.simed.geral.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

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

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.filter.UsuarioFilter;
import com.ajeff.simed.geral.repository.paginacao.PaginacaoUtil;

public class UsuariosRepositoryImpl implements UsuariosRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	@Autowired
	private PaginacaoUtil paginacaoUtil;
	
	@Override
	public Optional<Usuario> porNomeEAtivo(String nome) {
		return manager
				.createQuery("from Usuario where upper(nome) = upper(:nome) and ativo = true", Usuario.class)
				.setParameter("nome", nome).getResultList().stream().findFirst();
	}

	@Override
	public List<String> permissoes(Usuario usuario) {
		return manager.createQuery(
				"select distinct p.nome from Usuario u inner join u.grupos g inner join g.permissoes p where u = :usuario", String.class)
				.setParameter("usuario", usuario)
				.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		
		paginacaoUtil.prepararPaginacao(criteria, pageable);
		adicionarFiltro(filtro, criteria);
		
		criteria.addOrder(Order.asc("nome"));		
		return new PageImpl<>(criteria.list(), pageable, total(filtro));
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario buscarUsuarioComGrupos(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.createAlias("grupos", "g", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Usuario) criteria.uniqueResult();
	}
	
	@Transactional(readOnly = true)
	@Override
	public Usuario buscarEmpresaPorUsuario(Long id) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		criteria.createAlias("empresas", "e", JoinType.LEFT_OUTER_JOIN);
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("e.situacao", true));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Usuario) criteria.uniqueResult();
	}	
	
	
	
//	@Transactional(readOnly = true)
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Empresa> buscarEmpresaPorUsuario(Long id) {
//		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
//		criteria.createAlias("empresas", "e", JoinType.LEFT_OUTER_JOIN);
//		criteria.add(Restrictions.eq("id", id));
//		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
//		return (List<Empresa>) criteria.list();
//	}		

	
	private Long total(UsuarioFilter filtro) {
		Criteria criteria = manager.unwrap(Session.class).createCriteria(Usuario.class);
		adicionarFiltro(filtro, criteria);
		criteria.setProjection(Projections.rowCount());
		return (Long) criteria.uniqueResult();
	}

	private void adicionarFiltro(UsuarioFilter filtro, Criteria criteria) {
		if (filtro != null) {
			if (!StringUtils.isEmpty(filtro.getNomeCompleto())) {
				criteria.add(Restrictions.ilike("nome", filtro.getNomeCompleto(), MatchMode.ANYWHERE));
			}
		}
	}

}