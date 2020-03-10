package com.ajeff.simed.financeiro.repository.filter;

import java.time.LocalDate;

import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.geral.model.ContaEmpresa;

public class ExtratoFilter {

	private LocalDate dataInicio;
	private LocalDate dataFinal;
	private ContaEmpresa contaEmpresa;
	private MovimentacaoItem movimentacaoItem;
	
	public ContaEmpresa getContaEmpresa() {
		return contaEmpresa;
	}
	public void setContaEmpresa(ContaEmpresa contaEmpresa) {
		this.contaEmpresa = contaEmpresa;
	}
	public MovimentacaoItem getMovimentacaoItem() {
		return movimentacaoItem;
	}
	public void setMovimentacaoItem(MovimentacaoItem movimentacaoItem) {
		this.movimentacaoItem = movimentacaoItem;
	}
	public LocalDate getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDate getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}
}
