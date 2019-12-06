package com.ajeff.simed.financeiro.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.geral.model.Empresa;

public class ImpostoFilter {

	private String fornecedor;
	private String codigo;
	private String numeroNF;
	private String status;
	private LocalDate apuracaoInicio;
	private LocalDate apuracaoFim;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private ContaPagar contaPagar;
	private String empresa;
	
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getNumeroNF() {
		return numeroNF;
	}
	public void setNumeroNF(String numeroNF) {
		this.numeroNF = numeroNF;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LocalDate getApuracaoInicio() {
		return apuracaoInicio;
	}
	public void setApuracaoInicio(LocalDate apuracaoInicio) {
		this.apuracaoInicio = apuracaoInicio;
	}
	public LocalDate getApuracaoFim() {
		return apuracaoFim;
	}
	public void setApuracaoFim(LocalDate apuracaoFim) {
		this.apuracaoFim = apuracaoFim;
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
	public ContaPagar getContaPagar() {
		return contaPagar;
	}
	public void setContaPagar(ContaPagar contaPagar) {
		this.contaPagar = contaPagar;
	}
	public String getEmpresa() {
		return empresa;
	}
	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}
}
