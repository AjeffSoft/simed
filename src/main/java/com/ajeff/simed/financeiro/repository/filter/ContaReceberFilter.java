package com.ajeff.simed.financeiro.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajeff.simed.geral.model.Empresa;

public class ContaReceberFilter {

	private String fornecedor;
	private Long id;
	private Empresa empresa;
	private String historico;
	private String status;
	private LocalDate vencimentoInicio;
	private LocalDate vencimentoFim;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private String documento;
	
	public String getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public String getHistorico() {
		return historico;
	}
	public void setHistorico(String historico) {
		this.historico = historico;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
}
