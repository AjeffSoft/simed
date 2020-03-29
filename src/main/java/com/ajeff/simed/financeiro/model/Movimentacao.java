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
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "movimentacao")
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Informe a data inicial do movimento")
	@Column(name = "data_inicio")
	private LocalDate dataInicio;

	@NotNull(message = "Informe a data final do movimento")
	@Column(name = "data_final")
	private LocalDate dataFinal;

	@Column(name = "data_fechamento")
	private LocalDate dataFechamento;

	@Column(name = "saldo_inicial")
	private BigDecimal saldoInicial;

	@Column(name = "saldo_movimento")
	private BigDecimal saldoMovimento;

	@Column(name = "total_creditos")
	private BigDecimal totalCreditos;

	@Column(name = "total_debitos")
	private BigDecimal totalDebitos;
	
	@Column(name = "saldo_geral")
	private BigDecimal saldoGeral;
	
	private Boolean fechado;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@JsonIgnore
	@OneToMany(mappedBy = "movimentacao", cascade = CascadeType.ALL)
	private List<MovimentacaoItem> itens;
	

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

	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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

	public Boolean getFechado() {
		return fechado;
	}

	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoMovimento() {
		return saldoMovimento;
	}

	public void setSaldoMovimento(BigDecimal saldoMovimento) {
		this.saldoMovimento = saldoMovimento;
	}

	public BigDecimal getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(BigDecimal saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public List<MovimentacaoItem> getItens() {
		return itens;
	}

	public void setItens(List<MovimentacaoItem> itens) {
		this.itens = itens;
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
	

	public Boolean isDataDentroMovimento (LocalDate data) {
		return data.isAfter(this.dataInicio.minusDays(1)) && data.isBefore(this.dataFinal.plusDays(1));
	}
	
}
