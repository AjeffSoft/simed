package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ajeff.simed.geral.model.ContaEmpresa;

@Entity
@Table(name = "movimentacao_item")
public class MovimentacaoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "saldo_inicial")
	private BigDecimal saldoInicial;

	private BigDecimal creditos;

	private BigDecimal debitos;
	
	@Column(name = "saldo_movimento")
	private BigDecimal saldoMovimento;

	@Column(name = "saldo_geral")
	private BigDecimal saldoGeral;
	
	@Column(name = "cheque_pendente")
	private BigDecimal chequePendente;
	
	@ManyToOne
	@JoinColumn(name = "id_movimentacao")
	private Movimentacao movimentacao;

	@ManyToOne
	@JoinColumn(name = "id_conta_empresa")
	private ContaEmpresa contaEmpresa;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public ContaEmpresa getContaEmpresa() {
		return contaEmpresa;
	}

	public void setContaEmpresa(ContaEmpresa contaEmpresa) {
		this.contaEmpresa = contaEmpresa;
	}

	public BigDecimal getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(BigDecimal saldoGeral) {
		this.saldoGeral = saldoGeral;
	}
	
	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getCreditos() {
		return creditos;
	}

	public void setCreditos(BigDecimal creditos) {
		this.creditos = creditos;
	}

	public BigDecimal getDebitos() {
		return debitos;
	}

	public void setDebitos(BigDecimal debitos) {
		this.debitos = debitos;
	}

	public BigDecimal getSaldoMovimento() {
		return saldoMovimento;
	}

	public void setSaldoMovimento(BigDecimal saldoMovimento) {
		this.saldoMovimento = saldoMovimento;
	}

	public BigDecimal getChequePendente() {
		return chequePendente;
	}

	public void setChequePendente(BigDecimal chequePendente) {
		this.chequePendente = chequePendente;
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
		MovimentacaoItem other = (MovimentacaoItem) obj;
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
