package com.ajeff.simed.financeiro.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajeff.simed.geral.model.ContaEmpresa;

public class TransferenciaFilter {

	private LocalDate vencimentoInicio;
	private LocalDate vencimentoFim;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private String status;
	private String tipo;
	private String empresa;
	private ContaEmpresa contaOrigem;
	

	public LocalDate getVencimentoInicio() {
		return vencimentoInicio;
	}
	public void setVencimentoInicio(LocalDate vencimentoInicio) {
		this.vencimentoInicio = vencimentoInicio;
	}
	public LocalDate getVencimentoFim() {
		return vencimentoFim;
	}
	public void setVencimentoFim(LocalDate vencimentoFim) {
		this.vencimentoFim = vencimentoFim;
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
	public ContaEmpresa getContaOrigem() {
		return contaOrigem;
	}
	public void setContaOrigem(ContaEmpresa contaOrigem) {
		this.contaOrigem = contaOrigem;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
	
}
