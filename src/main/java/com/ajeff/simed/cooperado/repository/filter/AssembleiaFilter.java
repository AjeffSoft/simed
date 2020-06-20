package com.ajeff.simed.cooperado.repository.filter;

import java.time.LocalDate;

import com.ajeff.simed.cooperado.model.enums.TipoAssembleia;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssembleiaFilter {

	private LocalDate dataInicio;
	private LocalDate dataFim;
	private String descricao;
	private String resumo;
	private TipoAssembleia tipoAssembleia;
}
