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
@Table(name = "tabela_ir_pj")
public class TabelaIRPJ implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String nome;

	@Column(name = "aliquota_ir")
	private BigDecimal aliquotaIR;

	@Column(name = "aliquota_pis")
	private BigDecimal aliquotaPIS;

	@Column(name = "aliquota_cofins")
	private BigDecimal aliquotaCOFINS;

	@Column(name = "aliquota_csll")
	private BigDecimal aliquotaCSLL;



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


	public BigDecimal getAliquotaIR() {
		return aliquotaIR;
	}

	public void setAliquotaIR(BigDecimal aliquotaIR) {
		this.aliquotaIR = aliquotaIR;
	}

	public BigDecimal getAliquotaPIS() {
		return aliquotaPIS;
	}

	public void setAliquotaPIS(BigDecimal aliquotaPIS) {
		this.aliquotaPIS = aliquotaPIS;
	}

	public BigDecimal getAliquotaCOFINS() {
		return aliquotaCOFINS;
	}

	public void setAliquotaCOFINS(BigDecimal aliquotaCOFINS) {
		this.aliquotaCOFINS = aliquotaCOFINS;
	}

	public BigDecimal getAliquotaCSLL() {
		return aliquotaCSLL;
	}

	public void setAliquotaCSLL(BigDecimal aliquotaCSLL) {
		this.aliquotaCSLL = aliquotaCSLL;
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
		TabelaIRPJ other = (TabelaIRPJ) obj;
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
