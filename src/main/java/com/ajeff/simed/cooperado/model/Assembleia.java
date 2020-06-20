package com.ajeff.simed.cooperado.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.cooperado.model.enums.TipoAssembleia;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Assembleia implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Informe a data da assembléia")
	private LocalDate data;
	
	@NotBlank(message = "Informe uma descrição para a assembléia")
	private String descricao;

	@NotBlank(message = "Informe um resumo para a assembléia")
	private String resumo;
	
	private String anexo;
	
	private Boolean eleicao;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipo_assembleia")
	private TipoAssembleia tipoAssembleia;

	@OneToMany
	@JoinColumn(name = "id_cooperado")
	private List<Cooperado> presentes = new ArrayList<>();
	
	public boolean isNovo() {
		return this.id == null;
	}
	
	
}
