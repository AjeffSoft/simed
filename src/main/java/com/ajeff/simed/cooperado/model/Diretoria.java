package com.ajeff.simed.cooperado.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "diretoria")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Diretoria implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "data_final")
	private LocalDate dataFinal;
	
	private String chapa;
	
	private String descricao;
	
	private Boolean vencedor;
	
	private Boolean ativo;
	
	private String anotacao;

}
