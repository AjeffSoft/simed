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
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.Agencia;
import com.ajeff.simed.geral.model.DocumentoPF;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.InformacaoPessoal;

@Entity
@Table(name = "cooperado")
public class Cooperado implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe o nome")
	private String nome;
	
	@NotBlank(message = "Informe o CRM")
	private String crm;

	@Valid
	@Embedded
	private Endereco endereco;
	
	@Valid
	@Embedded
	private DocumentoPF documento;
	
	@Embedded
	private InformacaoPessoal informacaoPessoal;
	
	private String telefone;
	
	private String celular;
	
	@Email
	private String email;

	private Boolean ativo;
	
	private String conta;

	@Column(name = "tipo_conta")
	private String tipoConta;

	@ManyToOne
	@JoinColumn(name = "id_agencia")
	private Agencia agencia;

	private String anotacao;
	
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

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
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

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public Agencia getAgencia() {
		return agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getCrm() {
		return crm;
	}

	public void setCrm(String crm) {
		this.crm = crm;
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

	public String getAnotacao() {
		return anotacao;
	}

	public void setAnotacao(String anotacao) {
		this.anotacao = anotacao;
	}

	public InformacaoPessoal getInformacaoPessoal() {
		return informacaoPessoal;
	}

	public void setInformacaoPessoal(InformacaoPessoal informacaoPessoal) {
		this.informacaoPessoal = informacaoPessoal;
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
