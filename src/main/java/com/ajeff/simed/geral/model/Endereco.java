package com.ajeff.simed.geral.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Embeddable
public class Endereco implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String logradouro;

	private String bairro;

	private String cep;

	private String complemento;
	
	@ManyToOne
	@JoinColumn(name="id_cidade")
//	@NotNull(message = "Informe o estado e a cidade.")
	@JsonIgnore
	private Cidade cidade;
	
	@Transient
	private Estado estado;
	
	
	
	public Endereco(Cidade cidade) {
		this.cidade = cidade;
	}
	
	public Endereco() {};

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Estado getEstado() {
		if (this.cidade != null){
			return this.cidade.getEstado();
		}
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
	

}
