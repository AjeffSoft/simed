package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.geral.model.Empresa;

@Entity
@Table (name = "conta_receber")
public class ContaReceber implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String historico;
	
	@NotNull(message = "Informe a data de emissão")
	@Column(name = "data_emissao")
	private LocalDate dataEmissao;
	
	@NotNull(message = "Informe o vencimento")
	private LocalDate vencimento;
	
	@NotNull(message = "Informe o valor da conta")
	private BigDecimal valor;

	@Column(name = "valor_recebido")
	private BigDecimal valorRecebido;
	
	@NotBlank(message = "Informe o documento")
	private String documento;
	
	private String status;
	
	private Integer parcela;
	
	@Column(name = "total_parcela")
	private Integer totalParcela;

	
	@ManyToOne
	@JoinColumn(name = "id_conta_secundaria")
	@NotNull(message = "Informe a conta secundária")
	private PlanoContaSecundaria planoContaSecundaria;
	
	@NotNull(message = "Informe a empresa")
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@NotNull(message = "Informe o cliente")
	@ManyToOne
	@JoinColumn(name = "id_fornecedor")
	private Fornecedor fornecedor;


	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		if(this.historico != null ) {
			historico = historico.toUpperCase();
		}

		if(this.documento != null ) {
			documento = documento.toUpperCase();
		}
		
	}
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHistorico() {
		return historico;
	}

	public void setHistorico(String historico) {
		this.historico = historico;
	}

	public LocalDate getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(LocalDate dataEmissao) {
		this.dataEmissao = dataEmissao;
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

	public BigDecimal getValorRecebido() {
		return valorRecebido;
	}

	public void setValorRecebido(BigDecimal valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getParcela() {
		return parcela;
	}

	public void setParcela(Integer parcela) {
		this.parcela = parcela;
	}

	public Integer getTotalParcela() {
		return totalParcela;
	}

	public void setTotalParcela(Integer totalParcela) {
		this.totalParcela = totalParcela;
	}

	public PlanoContaSecundaria getPlanoContaSecundaria() {
		return planoContaSecundaria;
	}

	public void setPlanoContaSecundaria(PlanoContaSecundaria planoContaSecundaria) {
		this.planoContaSecundaria = planoContaSecundaria;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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
		ContaReceber other = (ContaReceber) obj;
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
	
	public boolean isVencimentoMaiorEmissao() {
		return this.vencimento.isEqual(this.dataEmissao) || this.vencimento.isAfter(this.dataEmissao);
	}

	public boolean isAberto() {
		return this.status.equals("ABERTO");
	}

	public boolean isRecebido() {
		return this.status.equals("RECEBIDO");
	}
	
	
}
