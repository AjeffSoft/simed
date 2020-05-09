package com.ajeff.simed.geral.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class OutrosDocumentos implements Serializable{

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
	
	@Column(name = "data_pis")
	private LocalDate dataPis;	
	
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
}
