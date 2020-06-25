package com.ajeff.simed.cooperado.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ajeff.simed.cooperado.model.enums.TipoCargoDiretor;
import com.ajeff.simed.cooperado.model.enums.TipoDiretor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "candidato")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Candidato implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "tipo_diretor")
	@Enumerated(EnumType.STRING)
	private TipoDiretor tipoDiretor;
	
	@Column(name = "cargo_diretor")
	@Enumerated(EnumType.STRING)
	private TipoCargoDiretor cargoDiretor;
	
	private String anotacao;
	
	@ManyToOne
	@JoinColumn(name = "id_cooperado")
	private Cooperado cooperado;
	
	public boolean isNovo() {
		return this.id == null;
	}

}
