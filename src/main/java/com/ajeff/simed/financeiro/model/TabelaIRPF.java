package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tabela_ir_pf")
public class TabelaIRPF implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	public TabelaIRPF(Long id, String nome, BigDecimal valorInicial, BigDecimal valorFinal, BigDecimal aliquota,
			BigDecimal deducao, BigDecimal dependente) {
		this.id = id;
		this.nome = nome;
		this.valorInicial = valorInicial;
		this.valorFinal = valorFinal;
		this.aliquota = aliquota;
		this.deducao = deducao;
		this.dependente = dependente;
	}

	public TabelaIRPF() {};

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Column(name = "valor_inicial")
	private BigDecimal valorInicial;

	@Column(name = "valor_final")
	private BigDecimal valorFinal;

	private BigDecimal aliquota;

	private BigDecimal deducao;

	private BigDecimal dependente;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getValorInicial() {
		return valorInicial;
	}

	public void setValorInicial(BigDecimal valorInicial) {
		this.valorInicial = valorInicial;
	}

	public BigDecimal getValorFinal() {
		return valorFinal;
	}

	public void setValorFinal(BigDecimal valorFinal) {
		this.valorFinal = valorFinal;
	}

	public BigDecimal getAliquota() {
		return aliquota;
	}

	public void setAliquota(BigDecimal aliquota) {
		this.aliquota = aliquota;
	}

	public BigDecimal getDeducao() {
		return deducao;
	}

	public void setDeducao(BigDecimal deducao) {
		this.deducao = deducao;
	}

	public BigDecimal getDependente() {
		return dependente;
	}

	public void setDependente(BigDecimal dependente) {
		this.dependente = dependente;
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
		TabelaIRPF other = (TabelaIRPF) obj;
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
