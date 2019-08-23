package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.ContaEmpresa;

@Entity
@Table(name = "extrato_bancario")
public class ExtratoBancario implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate data;

	private Boolean credito;
	
	private String status;

	private String tipo;
	
	private BigDecimal valor;

	private BigDecimal saldo;
	
	@NotBlank(message = "Informe um breve histórico da movimentação")
	private String historico;
	
	@OneToOne
	@JoinColumn(name = "id_pagamento")
	private Pagamento pagamento;
	
	@ManyToOne
	@JoinColumn(name = "id_movimentacao")
	private MovimentacaoBancaria movimentacao;	

	@OneToOne
	@JoinColumn(name = "id_transferencia")
	private TransferenciaContas transferencia;
	
	@ManyToOne
	@JoinColumn(name = "id_recebimento")
	private Recebimento recebimento;	

	@ManyToOne
	@JoinColumn(name = "id_conta_bancaria")
	private ContaEmpresa contaBancaria;	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Boolean getCredito() {
		return credito;
	}

	public void setCredito(Boolean credito) {
		this.credito = credito;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public MovimentacaoBancaria getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoBancaria movimentacao) {
		this.movimentacao = movimentacao;
	}

	public TransferenciaContas getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(TransferenciaContas transferencia) {
		this.transferencia = transferencia;
	}

	public Recebimento getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(Recebimento recebimento) {
		this.recebimento = recebimento;
	}

	public ContaEmpresa getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaEmpresa contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ExtratoBancario other = (ExtratoBancario) obj;
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
	
	public boolean isCompensado() {
		return this.status.equals("CONFERIDO");
	}

}
