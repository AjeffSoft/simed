package com.ajeff.simed.financeiro.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.geral.model.Empresa;

public class PeriodoRelatorio {
	

	private LocalDate dataInicio;
	private LocalDate dataFim;
	private LocalDate emissaoInicio;
	private LocalDate emissaoFim;
	private BigDecimal valorInicio;
	private BigDecimal valorFim;
	private Empresa empresa;
	private Fornecedor fornecedor;
	private String status;
	private Boolean pendente;
	private String nome;
	private String tipoRelatorio;
	private String ordenar;
	private PlanoConta planoConta;
	
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
	public String getTipoRelatorio() {
		return tipoRelatorio;
	}
	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
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
	public Empresa getEmpresa() {
		return empresa;
	}
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
	public Fornecedor getFornecedor() {
		return fornecedor;
	}
	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Boolean getPendente() {
		return pendente;
	}
	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}
	public LocalDate getEmissaoInicio() {
		return emissaoInicio;
	}
	public void setEmissaoInicio(LocalDate emissaoInicio) {
		this.emissaoInicio = emissaoInicio;
	}
	public LocalDate getEmissaoFim() {
		return emissaoFim;
	}
	public void setEmissaoFim(LocalDate emissaoFim) {
		this.emissaoFim = emissaoFim;
	}
	public String getOrdenar() {
		return ordenar;
	}
	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
	}
	public PlanoConta getPlanoConta() {
		return planoConta;
	}
	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}
}
