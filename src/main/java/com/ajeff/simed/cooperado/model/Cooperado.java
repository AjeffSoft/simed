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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.cooperado.model.enums.TipoDescooperar;
import com.ajeff.simed.cooperado.model.enums.TipoRecebimentoProducao;

@Entity
@Table(name = "cooperado")
public class Cooperado implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe o numero de registro")
	private String registro;
	
	@NotBlank(message = "Informe o código")
	private String codigo;

	@NotNull(message = "Informe a data")
	private LocalDate data;

	private Boolean ativo;
	
	private String anotacao;
	
//	@Column(name = "data_cancelamento")
//	private LocalDate dataCancelamento;
//	
//	@Size(max = 200, message = "O motivo deverá conter no máximo 200 caracteres!")
//	private String motivo;
//	
//	@Column(name = "tipo_cancelamento")
//	@Enumerated(EnumType.STRING)
//	private TipoDescooperar tipoCancelamento;
	
	@Column(name = "tipo_recebimento")
	@Enumerated(EnumType.STRING)
	private TipoRecebimentoProducao tipoRecebimento;
	
	@NotNull(message = "Informe o medico")
	@ManyToOne
	@JoinColumn(name = "id_medico")
	private Medico medico;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getAnotacao() {
		return anotacao;
	}

	public void setAnotacao(String anotacao) {
		this.anotacao = anotacao;
	}

	public String getRegistro() {
		return registro;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public TipoRecebimentoProducao getTipoRecebimento() {
		return tipoRecebimento;
	}

	public void setTipoRecebimento(TipoRecebimentoProducao tipoRecebimento) {
		this.tipoRecebimento = tipoRecebimento;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

//	public LocalDate getDataCancelamento() {
//		return dataCancelamento;
//	}
//
//	public void setDataCancelamento(LocalDate dataCancelamento) {
//		this.dataCancelamento = dataCancelamento;
//	}
//
//	public String getMotivo() {
//		return motivo;
//	}
//
//	public void setMotivo(String motivo) {
//		this.motivo = motivo;
//	}
//
//	public TipoDescooperar getTipoCancelamento() {
//		return tipoCancelamento;
//	}
//
//	public void setTipoCancelamento(TipoDescooperar tipoCancelamento) {
//		this.tipoCancelamento = tipoCancelamento;
//	}

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
		Cooperado other = (Cooperado) obj;
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
	
	public boolean isAtivado() {
		return this.ativo == true;
	}
}
