package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imposto")
public class Imposto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private LocalDate apuracao;
	
	private String nome;
	
	@NotNull(message = "Informe a data de vencimento")
	private LocalDate vencimento;

	@NotNull(message = "Informe o valor")	
	private BigDecimal valor;
	
	private BigDecimal multa;
	
	private BigDecimal juros;
	
	private BigDecimal total;
	
	@Column(name = "valor_nf")
	private BigDecimal valorNF;

	@Column(name = "numero_nf")
	private String numeroNF;

	@Column(name = "emissao_nf")
	private LocalDate emissaoNF;

	@Enumerated(EnumType.STRING)
	private StatusContaPagar status;
	
	@Transient
	private Boolean gerarDarf;

	@NotBlank(message = "Informe um histórico")	
	private String historico;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_conta_pagar")
	private ContaPagar contaPagar;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id_conta_pagar_gerada")
	private ContaPagar contaPagarGerada;
	
	@PrePersist
	@PreUpdate
	public void prePersistUpdate() {
		historico = historico.toUpperCase();		
	}	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getApuracao() {
		return apuracao;
	}

	public void setApuracao(LocalDate apuracao) {
		this.apuracao = apuracao;
	}

	public LocalDate getVencimento() {
		return vencimento;
	}

	public void setVencimento(LocalDate vencimento) {
		this.vencimento = vencimento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getMulta() {
		return multa;
	}

	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}

	public BigDecimal getJuros() {
		return juros;
	}

	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getValorNF() {
		return valorNF;
	}

	public void setValorNF(BigDecimal valorNF) {
		this.valorNF = valorNF;
	}

	public String getNumeroNF() {
		return numeroNF;
	}

	public void setNumeroNF(String numeroNF) {
		this.numeroNF = numeroNF;
	}

	public LocalDate getEmissaoNF() {
		return emissaoNF;
	}

	public void setEmissaoNF(LocalDate emissaoNF) {
		this.emissaoNF = emissaoNF;
	}

	public StatusContaPagar getStatus() {
		return status;
	}

	public void setStatus(StatusContaPagar status) {
		this.status = status;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public Boolean getGerarDarf() {
		return gerarDarf;
	}

	public void setGerarDarf(Boolean gerarDarf) {
		this.gerarDarf = gerarDarf;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ContaPagar getContaPagar() {
		return contaPagar;
	}

	public void setContaPagar(ContaPagar contaPagar) {
		this.contaPagar = contaPagar;
	}

	public ContaPagar getContaPagarGerada() {
		return contaPagarGerada;
	}

	public void setContaPagarGerada(ContaPagar contaPagarGerada) {
		this.contaPagarGerada = contaPagarGerada;
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
		Imposto other = (Imposto) obj;
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
		return this.status.equals(StatusContaPagar.ABERTO);
	}

	public boolean isEmitido() {
		return this.status.equals(StatusContaPagar.EMITIDO);
	}
	
	public boolean isPago() {
		return this.status.equals(StatusContaPagar.PAGO);
	}
	
}
