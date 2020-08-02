package com.ajeff.simed.financeiro.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.TabelaIRPF;

public interface TabelasIrpfRepository extends JpaRepository<TabelaIRPF, Long>{

	List<TabelaIRPF> findByValorFinalGreaterThanEqual(BigDecimal valorBase);

}
