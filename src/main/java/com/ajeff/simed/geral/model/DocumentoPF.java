package com.ajeff.simed.geral.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

@Embeddable
public class DocumentoPF implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "titulo_eleitor")
	private String tituloEleitor;
	
	@Column(name = "sessao_eleitor")
	private String tituloSessao;
	
	@Column(name = "zona_eleitor")
	private String tituloZona;	
	
	@Column(name = "data_eleitor")
	private LocalDate dataEleitor;

	private String ctps;
	
	@Column(name = "data_ctps")
	private LocalDate dataCtps;	
	
	private String pis;
	
	@Column(name = "data_pis")
	private LocalDate dataPis;	
	
	@NotBlank (message="O campo CPF é obrigatório.")
	@CPF(message="Informe um CPF válido.")
	private String cpf;

	private String rg;
	
	@Column(name = "data_rg")
	private LocalDate dataRG;

	private String habilitacao;

	@Column(name = "data_habilitacao")
	private LocalDate dataHabilitacao;

	private String reservista;

	@Column(name = "data_reservista")
	private LocalDate dataReservista;
	
	private String certidao;

	@Column(name = "tipo_certidao")
	private String tipoCertidao;

	@Column(name = "data_certidao")
	private LocalDate dataCertidao;

	
	public String getTituloEleitor() {
		return tituloEleitor;
	}

	public void setTituloEleitor(String tituloEleitor) {
		this.tituloEleitor = tituloEleitor;
	}

	public LocalDate getDataEleitor() {
		return dataEleitor;
	}

	public void setDataEleitor(LocalDate dataEleitor) {
		this.dataEleitor = dataEleitor;
	}

	public String getCtps() {
		return ctps;
	}

	public void setCtps(String ctps) {
		this.ctps = ctps;
	}

	public LocalDate getDataCtps() {
		return dataCtps;
	}

	public void setDataCtps(LocalDate dataCtps) {
		this.dataCtps = dataCtps;
	}

	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public LocalDate getDataPis() {
		return dataPis;
	}

	public void setDataPis(LocalDate dataPis) {
		this.dataPis = dataPis;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public LocalDate getDataRG() {
		return dataRG;
	}

	public void setDataRG(LocalDate dataRG) {
		this.dataRG = dataRG;
	}

	public String getHabilitacao() {
		return habilitacao;
	}

	public void setHabilitacao(String habilitacao) {
		this.habilitacao = habilitacao;
	}

	public LocalDate getDataHabilitacao() {
		return dataHabilitacao;
	}

	public void setDataHabilitacao(LocalDate dataHabilitacao) {
		this.dataHabilitacao = dataHabilitacao;
	}

	public String getReservista() {
		return reservista;
	}

	public void setReservista(String reservista) {
		this.reservista = reservista;
	}

	public LocalDate getDataReservista() {
		return dataReservista;
	}

	public void setDataReservista(LocalDate dataReservista) {
		this.dataReservista = dataReservista;
	}

	public String getCertidao() {
		return certidao;
	}

	public void setCertidao(String certidao) {
		this.certidao = certidao;
	}

	public String getTipoCertidao() {
		return tipoCertidao;
	}

	public void setTipoCertidao(String tipoCertidao) {
		this.tipoCertidao = tipoCertidao;
	}

	public LocalDate getDataCertidao() {
		return dataCertidao;
	}

	public void setDataCertidao(LocalDate dataCertidao) {
		this.dataCertidao = dataCertidao;
	}

	public String getTituloSessao() {
		return tituloSessao;
	}

	public void setTituloSessao(String tituloSessao) {
		this.tituloSessao = tituloSessao;
	}

	public String getTituloZona() {
		return tituloZona;
	}

	public void setTituloZona(String tituloZona) {
		this.tituloZona = tituloZona;
	}
	
}
