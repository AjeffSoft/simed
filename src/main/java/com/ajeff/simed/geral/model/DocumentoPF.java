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
	
	@Column(name = "zona_eleitor")
	private String zonaEleitor;
	
	@Column(name = "sessao_eleitor")
	private String sessaoEleitor;
	
	@Column(name = "data_eleitor")
	private LocalDate dataEleitor;

	private String ctps;
	
	@Column(name = "serie_ctps")
	private String serieCtps;
	
	@Column(name = "data_ctps")
	private LocalDate dataCtps;	
	
	@NotBlank(message = "O campo PIS/NIT é obrigatório.")
	private String pis;
	
	@Column(name = "banco_pis")
	private String bancoPis;
	
	@Column(name = "data_pis")
	private LocalDate dataPis;	
	
	@NotBlank (message="O campo CPF é obrigatório.")
	@CPF(message="Informe um CPF válido.")
	private String cpf;

	private String rg;
	
	@Column(name = "data_rg")
	private LocalDate dataRG;

	private String habilitacao;

	@Column(name = "categoria_hab")
	private String categoriaHab;

	@Column(name = "data_habilitacao")
	private LocalDate dataHabilitacao;

	private String reservista;

	@Column(name = "regiao_reservista")
	private String regiaoReservista;

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

	public String getZonaEleitor() {
		return zonaEleitor;
	}

	public void setZonaEleitor(String zonaEleitor) {
		this.zonaEleitor = zonaEleitor;
	}

	public String getSessaoEleitor() {
		return sessaoEleitor;
	}

	public void setSessaoEleitor(String sessaoEleitor) {
		this.sessaoEleitor = sessaoEleitor;
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

	public String getSerieCtps() {
		return serieCtps;
	}

	public void setSerieCtps(String serieCtps) {
		this.serieCtps = serieCtps;
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

	public String getBancoPis() {
		return bancoPis;
	}

	public void setBancoPis(String bancoPis) {
		this.bancoPis = bancoPis;
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

	public String getCategoriaHab() {
		return categoriaHab;
	}

	public void setCategoriaHab(String categoriaHab) {
		this.categoriaHab = categoriaHab;
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

	public String getRegiaoReservista() {
		return regiaoReservista;
	}

	public void setRegiaoReservista(String regiaoReservista) {
		this.regiaoReservista = regiaoReservista;
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
	
}
