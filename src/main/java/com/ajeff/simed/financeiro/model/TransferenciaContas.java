package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.ContaEmpresa;

@Entity
@Table(name = "transferencia_conta")
public class TransferenciaContas implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Informe a data da transferencia")
	private LocalDate data;
	
	@NotNull(message = "Informe o valor da transferencia")
	private BigDecimal valor;
	
	@NotBlank(message = "Informe o tipo da transferencia")
	private String tipo;
	
	private String status;
	
	private Boolean fechado;
	
	private String observacao;
	
	@ManyToOne
	@JoinColumn(name = "id_conta_origem")
	private ContaEmpresa contaOrigem;

	@NotNull(message = "Informe a conta de destino")
	@ManyToOne
	@JoinColumn(name = "id_conta_destino")
	private ContaEmpresa contaDestino;

	@ManyToOne
	@JoinColumn(name = "id_movimentacao")
	private Movimentacao movimentacao;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public ContaEmpresa getContaOrigem() {
		return contaOrigem;
	}

	public void setContaOrigem(ContaEmpresa contaOrigem) {
		this.contaOrigem = contaOrigem;
	}

	public ContaEmpresa getContaDestino() {
		return contaDestino;
	}

	public void setContaDestino(ContaEmpresa contaDestino) {
		this.contaDestino = contaDestino;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Movimentacao getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Boolean getFechado() {
		return fechado;
	}

	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
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
		TransferenciaContas other = (TransferenciaContas) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public boolean isNovo() {
		return this.id == null;
	}
	
	public boolean isAberto() {
		return this.status.equals("ABERTO");
	}

	public boolean isAbertoENaoFechado() {
		return this.status.equals("ABERTO") && this.fechado == false;
	}
	
}
