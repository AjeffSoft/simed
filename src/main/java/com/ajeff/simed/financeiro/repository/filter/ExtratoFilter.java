package com.ajeff.simed.financeiro.repository.filter;

import java.math.BigDecimal;

import com.ajeff.simed.financeiro.model.MovimentacaoBancaria;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;

public class ExtratoFilter {

	private String historico;
	private String tipo;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private ContaEmpresa contaEmpresa;
	private Empresa empresa;
	private MovimentacaoBancaria movimentacao;
	
	public String getHistorico() {
		return historico;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public BigDecimal getValorInicio() {
		return valorInicio;
	}
	public void setValorInicio(BigDecimal valorInicio) {
		this.valorInicio = valorInicio;
	}
	public BigDecimal getValorFim() {
		return valorFim;
	}
	public void setValorFim(BigDecimal valorFim) {
		this.valorFim = valorFim;
	}
	public ContaEmpresa getContaEmpresa() {
		return contaEmpresa;
	}
	public void setContaEmpresa(ContaEmpresa contaEmpresa) {
		this.contaEmpresa = contaEmpresa;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public MovimentacaoBancaria getMovimentacao() {
		return movimentacao;
	}
	public void setMovimentacao(MovimentacaoBancaria movimentacao) {
		this.movimentacao = movimentacao;
	}
}
