package com.ajeff.simed.financeiro.repository.filter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;

public class PagamentoFilter {

	private LocalDate dataInicio;
	private LocalDate dataFim;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private String tipo;
	private ContaEmpresa contaEmpresa;
	private String observacao;
	private String status;
	private String documento;
	private Empresa empresa;
	
	public LocalDate getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}
	public LocalDate getDataFim() {
		return dataFim;
	}
	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public ContaEmpresa getContaEmpresa() {
		return contaEmpresa;
	}
	public void setContaEmpresa(ContaEmpresa contaEmpresa) {
		this.contaEmpresa = contaEmpresa;
	}
	public String getObservacao() {
		return observacao;
	}
	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}	
}
