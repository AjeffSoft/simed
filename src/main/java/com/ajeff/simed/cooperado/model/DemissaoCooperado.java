package com.ajeff.simed.cooperado.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ajeff.simed.cooperado.model.enums.TipoDemissaoCooperado;

@Entity
@Table(name = "demissao_cooperado")
public class DemissaoCooperado implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message = "Informe a data de demissão")
	private LocalDate data;

	@Column(name = "anotacao")
	private String observacao;
	
	private String motivo;
	
	@Column(name = "tipo")
	@Enumerated(EnumType.STRING)
	private TipoDemissaoCooperado tipoDemissao;
	
	@NotNull(message = "Informe o cooperado")
	@ManyToOne
	@JoinColumn(name = "id_admissao")
	private AdmissaoCooperado admissao;
	
	
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

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public TipoDemissaoCooperado getTipoDemissao() {
		return tipoDemissao;
	}

	public void setTipoDemissao(TipoDemissaoCooperado tipoDemissao) {
		this.tipoDemissao = tipoDemissao;
	}

	public AdmissaoCooperado getAdmissao() {
		return admissao;
	}

	public void setAdmissao(AdmissaoCooperado admissao) {
		this.admissao = admissao;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
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
		DemissaoCooperado other = (DemissaoCooperado) obj;
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
