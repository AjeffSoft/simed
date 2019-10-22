package com.ajeff.simed.cooperado.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.DocumentoPF;
import com.ajeff.simed.geral.model.InformacaoPessoal;

@Entity
@Table(name = "dependente_cooperado")
public class Dependente implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe o nome")
	private String nome;
	
	@Embedded
	private DocumentoPF documento;
	
	@Embedded
	private InformacaoPessoal informacaoPessoal;
	
	private String celular;
	
	@Email
	private String email;
	
	@NotNull(message = "Informe o grau de parentesco")
	private String parentesco;

	private Boolean ativo;
	
	@Column(name = "imposto_renda")
	private Boolean impostoRenda;
	
	@NotNull(message = "Informe o cooperado")
	@ManyToOne
	@JoinColumn(name ="id_cooperado")
	private Cooperado cooperado;

	@PrePersist
	@PreUpdate
	public void prePersistUpdate() {
		nome = nome.toUpperCase();
		email = email.toLowerCase();
	}
	
	
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

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public DocumentoPF getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoPF documento) {
		this.documento = documento;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public InformacaoPessoal getInformacaoPessoal() {
		return informacaoPessoal;
	}

	public void setInformacaoPessoal(InformacaoPessoal informacaoPessoal) {
		this.informacaoPessoal = informacaoPessoal;
	}

	public Boolean getImpostoRenda() {
		return impostoRenda;
	}

	public void setImpostoRenda(Boolean impostoRenda) {
		this.impostoRenda = impostoRenda;
	}

	public String getParentesco() {
		return parentesco;
	}

	public void setParentesco(String parentesco) {
		this.parentesco = parentesco;
	}

	public Cooperado getCooperado() {
		return cooperado;
	}

	public void setCooperado(Cooperado cooperado) {
		this.cooperado = cooperado;
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
		Dependente other = (Dependente) obj;
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
	
	public boolean isDependenteIR() {
		return this.impostoRenda == true;
	}
}
