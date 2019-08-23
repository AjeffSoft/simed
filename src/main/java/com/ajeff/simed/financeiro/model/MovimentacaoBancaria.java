package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
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

import com.ajeff.simed.geral.model.ContaEmpresa;

@Entity
@Table(name = "movimentacao_bancaria")
public class MovimentacaoBancaria implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name = "valor_abertura")
	private BigDecimal valorAbertura;

	@Column(name = "valor_creditos")
	private BigDecimal valorCreditos;

	@Column(name = "valor_debitos")
	private BigDecimal valorDebitos;
	
	@Column(name = "saldo_periodo")
	private BigDecimal saldoPeriodo;

	@Column(name = "saldo_geral")
	private BigDecimal saldoGeral;
	
	@Column(name = "valor_pendente")
	private BigDecimal valorPendente;
	
	@ManyToOne
	@JoinColumn(name = "id_movimentacao")
	private Movimentacao movimentacao;

	@ManyToOne
	@JoinColumn(name = "id_conta_empresa")
	private ContaEmpresa contaEmpresa;
	
	@OneToMany(mappedBy = "movimentacao", cascade = CascadeType.REMOVE)
	private List<ExtratoBancario> extratosBancarias;

	
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

	public BigDecimal getValorAbertura() {
		return valorAbertura;
	}

	public void setValorAbertura(BigDecimal valorAbertura) {
		this.valorAbertura = valorAbertura;
	}

	public BigDecimal getValorCreditos() {
		return valorCreditos;
	}

	public void setValorCreditos(BigDecimal valorCreditos) {
		this.valorCreditos = valorCreditos;
	}

	public BigDecimal getValorDebitos() {
		return valorDebitos;
	}

	public void setValorDebitos(BigDecimal valorDebitos) {
		this.valorDebitos = valorDebitos;
	}

	public BigDecimal getSaldoPeriodo() {
		return saldoPeriodo;
	}

	public void setSaldoPeriodo(BigDecimal saldoPeriodo) {
		this.saldoPeriodo = saldoPeriodo;
	}

	public BigDecimal getSaldoGeral() {
		return saldoGeral;
	}

	public void setSaldoGeral(BigDecimal saldoGeral) {
		this.saldoGeral = saldoGeral;
	}

	public BigDecimal getValorPendente() {
		return valorPendente;
	}

	public void setValorPendente(BigDecimal valorPendente) {
		this.valorPendente = valorPendente;
	}

	public List<ExtratoBancario> getExtratosBancarias() {
		return extratosBancarias;
	}

	public void setExtratosBancarias(List<ExtratoBancario> extratosBancarias) {
		this.extratosBancarias = extratosBancarias;
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
		MovimentacaoBancaria other = (MovimentacaoBancaria) obj;
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
