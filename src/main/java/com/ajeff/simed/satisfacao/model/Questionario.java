package com.ajeff.simed.satisfacao.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.satisfacao.model.enums.TipoResposta;

@Entity
@Table(name = "questionario")
public class Questionario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Informe um tipo")
	@Column(name = "tipo_resposta")
	@Enumerated(EnumType.STRING)
	private TipoResposta tipoResposta;

	@NotNull(message = "Informe uma empresa")
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@NotNull(message = "Informe uma pergunta")
	@ManyToOne
	@JoinColumn(name = "id_pergunta")
	private Pergunta pergunta;
	
	@ManyToMany
	@JoinTable(name = "questionario_resposta", joinColumns = @JoinColumn(name = "id_questionario"), 
				inverseJoinColumns = @JoinColumn(name = "id_resposta"))
	private List<Resposta> respostas;
	
	private boolean ativo;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}	
	
	public TipoResposta getTipoResposta() {
		return tipoResposta;
	}

	public void setTipoResposta(TipoResposta tipoResposta) {
		this.tipoResposta = tipoResposta;
	}

	public Pergunta getPergunta() {
		return pergunta;
	}

	public void setPergunta(Pergunta pergunta) {
		this.pergunta = pergunta;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}

	public List<Resposta> getRespostas() {
		return respostas;
	}

	public void setRespostas(List<Resposta> respostas) {
		this.respostas = respostas;
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
		Questionario other = (Questionario) obj;
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