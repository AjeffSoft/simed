package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;

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
import com.ajeff.simed.geral.model.Endereco;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "fornecedor")
public class Fornecedor implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message="Informe o nome")
	private String nome;

	@NotBlank(message = "Informe o nome fantasia")
	private String fantasia;
	
	@NotBlank(message = "Informe uma sigla")
	private String sigla;
	
	@JsonIgnore
	@Valid
	@Embedded
	private Endereco endereco;
	
	private String telefone;
	
	private String celular;
	
	@Email
	private String email;

	private String documento1;
	
	private String documento2;
	
	private Boolean clifor;
	
	private BigDecimal dependente;
	
	@NotBlank(message = "Informe o tipo")
	private String tipo;
	
	private String conta;
	
	@Column(name = "dados_bancarios")
	private Boolean dadosBancarios;
	
	@Column(name = "tipo_conta")
	private String tipoConta;

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

	public String getDocumento1() {
		return documento1;
	}

	public void setDocumento1(String documento1) {
		this.documento1 = documento1;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getDadosBancarios() {
		return dadosBancarios;
	}

	public void setDadosBancarios(Boolean dadosBancarios) {
		this.dadosBancarios = dadosBancarios;
	}

	public Boolean getClifor() {
		return clifor;
	}

	public void setClifor(Boolean clifor) {
		this.clifor = clifor;
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
		Fornecedor other = (Fornecedor) obj;
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
