package com.ajeff.simed.geral.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.validation.AtributoConfirmacao;

@AtributoConfirmacao(atributo = "senha", atributoConfirmacao = "confirmacaoSenha"
				, message = "Confirmação da senha não confere")
@Entity
@Table(name = "usuario")
@DynamicUpdate
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Informe um nome de login")
	private String nome;

	@NotBlank(message = "Informe um e-mail.")
	@Email(message = "Informe um e-mail válido")
	private String email;

	private String senha;
	
	@Transient
	private String confirmacaoSenha;

	private Boolean ativo;
	
	@Size(min = 1, message = "Informe pelo menos uma empresa.")
	@ManyToMany
	@JoinTable(name = "usuario_empresa", joinColumns = @JoinColumn(name = "id_usuario"),
				inverseJoinColumns = @JoinColumn(name = "id_empresa"))
	private List<Empresa> empresas;

	@OneToOne
	@JoinColumn(name = "id_empresa_atendente")
	private Empresa empresa;
	
	@Size(min = 1, message = "Informe pelo menos um grupo.")
	@ManyToMany
	@JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "id_usuario"),
				inverseJoinColumns = @JoinColumn(name = "id_grupo"))
	private List<Grupo> grupos;
	
	@PrePersist
	private void prePersistUpdate(){
		nome = nome.toUpperCase();
		email = email.toLowerCase();
	}

	@PreUpdate
	private void preUpdate() {
		this.confirmacaoSenha = senha;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	public void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}
	
	public boolean isNovo() {
		return id == null;
	}

	public List<Grupo> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public List<Empresa> getEmpresas() {
		return empresas;
	}

	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
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
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}