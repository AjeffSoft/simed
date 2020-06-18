package com.ajeff.simed.geral.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.Valid;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.financeiro.model.enums.TipoConta;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="pessoa_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "pessoa")
public abstract class  Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe o nome")
	private String nome;

	@NotBlank(message = "Informe o nome fantasia/apelido")
	private String fantasia;
	
	@NotBlank(message = "Informe uma sigla")
	private String sigla;

	@JsonIgnore
	@Valid
	@Embedded
	private Endereco endereco;
	
	private String telefone;
	
	private Boolean ativo = true;
	
	private String celular;
	
	@Email
	private String email;

	/*
	 * PJ = CNPJ ; PF = CPF
	 */
	private String documento1;

	/*
	 * PJ = INSCRIÇÃO ESTADUAL ; PF = PIS/PASEP
	 */
	private String documento2;
	
	private String conta;
	
	private Integer dependente = 0;
	
	@Column(name = "dados_bancarios")
	private Boolean dadosBancarios;
	
	@Column(name = "tipo_pessoa")
	private String tipoPessoa;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_conta")
	private TipoConta tipoConta;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_agencia")
	private Agencia agencia;
	
	
	@PrePersist
	@PreUpdate
	public void prePersistUpdate() {
		nome = nome.toUpperCase();
		fantasia = fantasia.toUpperCase();
		sigla = sigla.toUpperCase();
		email = email.toLowerCase();
	}

	
	public boolean isNovo() {
		return this.id == null;
	}
	
	public boolean isAtivado() {
		return this.ativo == true;
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


	public String getFantasia() {
		return fantasia;
	}


	public void setFantasia(String fantasia) {
		this.fantasia = fantasia;
	}


	public String getSigla() {
		return sigla;
	}


	public void setSigla(String sigla) {
		this.sigla = sigla;
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


	public Boolean getAtivo() {
		return ativo;
	}


	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
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


	public String getDocumento1() {
		return documento1;
	}


	public void setDocumento1(String documento1) {
		this.documento1 = documento1;
	}


	public String getDocumento2() {
		return documento2;
	}


	public void setDocumento2(String documento2) {
		this.documento2 = documento2;
	}


	public String getConta() {
		return conta;
	}


	public void setConta(String conta) {
		this.conta = conta;
	}


	public Integer getDependente() {
		return dependente;
	}


	public void setDependente(Integer dependente) {
		this.dependente = dependente;
	}


	public Boolean getDadosBancarios() {
		return dadosBancarios;
	}


	public void setDadosBancarios(Boolean dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}


	public String getTipoPessoa() {
		return tipoPessoa;
	}


	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}


	public TipoConta getTipoConta() {
		return tipoConta;
	}


	public void setTipoConta(TipoConta tipoConta) {
		this.tipoConta = tipoConta;
	}


	public Agencia getAgencia() {
		return agencia;
	}


	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
	
}
