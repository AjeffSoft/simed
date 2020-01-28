package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.ContaReceber;
import com.ajeff.simed.financeiro.model.Recebimento;
import com.ajeff.simed.financeiro.repository.helper.recebimento.RecebimentosRepositoryQueries;

public interface RecebimentosRepository extends JpaRepository<Recebimento, Long>, RecebimentosRepositoryQueries{

	List<Recebimento> findByContaReceberOrderByData(ContaReceber contaReceber);

}
