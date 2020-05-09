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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="pessoa_type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "pessoa")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
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
}
