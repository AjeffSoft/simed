package com.ajeff.simed.financeiro.service;

import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Endereco;

public class MovimentacaoServiceTest {
	@Autowired
	private MovimentacaoService service;
	private Empresa empresa;
	private Cidade cidade;
	private Movimentacao movimentacao;
	
	@Before
	public void setUp() {
		cidade = new Cidade("IGUATU");
		empresa = new Empresa(1L, "UNIMED", "UNIMED", "131", "12154544", new Endereco(cidade));
		this.movimentacao = new Movimentacao();
	}
	
	@Test
	public void movimentacaoFechadaTest() {
		LocalDate dataInicio = LocalDate.of(2019, 11, 1);
		LocalDate dataFinal = LocalDate.of(2019, 11, 30);
		
		movimentacao.setDataInicio(dataInicio);
		movimentacao.setDataFinal(dataFinal);
		movimentacao.setFechado(true);
		Boolean result = this.service.isAberto(movimentacao);
		assertTrue(result);
	}
	
}
