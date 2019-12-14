package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ajeff.simed.geral.model.Empresa;

@Entity
@Table(name = "movimentacao")
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Informe a data do inicio")
	@Column(name = "data_inicio")
	private LocalDate dataInicio;

	@NotNull(message = "Informe a data do fim")
	@Column(name = "data_final")
	private LocalDate dataFinal;

	@Column(name = "data_fechamento")
	private LocalDate dataFechamento;

	@Column(name = "total_abertura")
	private BigDecimal totalAbertura;

	@Column(name = "total_periodo")
	private BigDecimal totalPeriodo;

	@Column(name = "total_creditos")
	private BigDecimal totalCreditos;

	@Column(name = "total_debitos")
	private BigDecimal totalDebitos;

	
	@Column(name = "total_pendente")
	private BigDecimal totalPendente;
	
	@Column(name = "total_geral")
	private BigDecimal totalGeral;
	
	private Boolean fechado;
	
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@OneToMany(mappedBy = "movimentacao", cascade = CascadeType.REMOVE)
	private List<MovimentacaoBancaria> movimentacoesBancarias;
	
	
	public Movimentacao(LocalDate dataInicio, LocalDate dataFinal, Boolean fechado, Empresa empresa) {
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;
		this.fechado = fechado;
		this.empresa = empresa;
	}
	
	public Movimentacao() {};

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDate getDataFechamento() {
		return dataFechamento;
	}

	public void setDataFechamento(LocalDate dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public BigDecimal getTotalPendente() {
		return totalPendente;
	}

	public void setTotalPendente(BigDecimal totalPendente) {
		this.totalPendente = totalPendente;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public List<MovimentacaoBancaria> getMovimentacoesBancarias() {
		return movimentacoesBancarias;
	}

	public void setMovimentacoesBancarias(List<MovimentacaoBancaria> movimentacoesBancarias) {
		this.movimentacoesBancarias = movimentacoesBancarias;
	}

	public BigDecimal getTotalAbertura() {
		return totalAbertura;
	}

	public void setTotalAbertura(BigDecimal totalAbertura) {
		this.totalAbertura = totalAbertura;
	}

	public BigDecimal getTotalPeriodo() {
		return totalPeriodo;
	}

	public void setTotalPeriodo(BigDecimal totalPeriodo) {
		this.totalPeriodo = totalPeriodo;
	}

	public BigDecimal getTotalCreditos() {
		return totalCreditos;
	}

	public void setTotalCreditos(BigDecimal totalCreditos) {
		this.totalCreditos = totalCreditos;
	}

	public BigDecimal getTotalDebitos() {
		return totalDebitos;
	}

	public void setTotalDebitos(BigDecimal totalDebitos) {
		this.totalDebitos = totalDebitos;
	}

	public BigDecimal getTotalGeral() {
		return totalGeral;
	}

	public void setTotalGeral(BigDecimal totalGeral) {
		this.totalGeral = totalGeral;
	}

	public Boolean getFechado() {
		return fechado;
	}

	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Movimentacao other = (Movimentacao) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public boolean isNovo() {
		return this.id == null;
	}
	
}
