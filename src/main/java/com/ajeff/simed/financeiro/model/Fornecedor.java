package com.ajeff.simed.financeiro.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.Pessoa;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("FORNECEDOR")
@Getter
@Setter
@NoArgsConstructor
public class Fornecedor extends Pessoa{

	private static final long serialVersionUID = 1L;

	private Boolean clifor;
	
	@NotBlank(message = "Informe o tipo")
	private String tipo;
	
	public boolean isDependente() {
		if(getDependente() != null) {
			return this.getDependente().compareTo(Integer.valueOf(0)) == 1;
		}else {
			return false;
		}
	}
	
}
