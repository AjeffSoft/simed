package com.ajeff.simed.geral.repository.helper.contaEmpresa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class ContaEmpresaRepositoryImpl implements ContaEmpresaRepositoryQueries{

	@PersistenceContext
	private EntityManager manager;
	
	
//	@Override
//	public BigDecimal saldoTodasAsConta(Usuario usuario) {
//		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("select sum(saldo) from ContaEmpresa where empresa = :emp", BigDecimal.class)
//				.setParameter("emp", usuario.getEmpresa())
//				.getSingleResult());
//		return optional.orElse(BigDecimal.ZERO);
//	}
//
//
//	@Override
//	public BigDecimal saldoTodasAsContaCorrentes(Usuario usuario) {
//		Optional<BigDecimal> optional = Optional.ofNullable(manager.createQuery("select sum(saldo) from ContaEmpresa where empresa =:emp and tipo = 'CORRENTE'", BigDecimal.class)
//				.setParameter("emp", usuario.getEmpresa())
//				.getSingleResult());
//		return optional.orElse(BigDecimal.ZERO);
//	}


}
