package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.geral.model.Empresa;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "conta_pagar")
public class ContaPagar implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Informe uma descrição para o movimento")
	private String historico;
	
	@NotNull(message = "Informe a data de emissão")
	@Column(name = "data_emissao")
	private LocalDate dataEmissao;
	
	@NotNull(message = "Informe a data de vencimento")	
	private LocalDate vencimento;

	@NotNull(message = "Informe o valor da conta a pagar")
	private BigDecimal valor;
	
	private String documento;
	
	@Enumerated(EnumType.STRING)
	private StatusContaPagar status;
	
	private Boolean recibo;
	
	@Column(name = "tem_nota")
	private Boolean temNota;
	
	@NotBlank(message = "Informe a nota fiscal")
	@Column(name = "nota_fiscal")
	private String notaFiscal;
	
	private Integer parcela;
	
	@Column(name = "total_parcela")
	private Integer totalParcela;
	
	private Boolean fixo;
	
	private String upload;
	
	@Column(name = "content_type")
	private String contentType;

	@JsonIgnore
	@NotNull(message = "Informe o plano de contas")
	@ManyToOne
	@JoinColumn(name = "id_conta_secundaria")
	private PlanoContaSecundaria planoContaSecundaria;
	
	@JsonIgnore
	@NotNull(message = "Informe a empresa")
	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@NotNull(message = "Informe o fornecedor")
	@ManyToOne
	@JoinColumn(name = "id_fornecedor")
	private Fornecedor fornecedor;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_pagamento")
	private Pagamento pagamento;

	@JsonIgnore
	@OneToMany(mappedBy = "contaPagar", cascade = CascadeType.ALL)
	private List<Imposto> impostos = new ArrayList<>();
	
	@Transient
	private PlanoConta planoConta;

	@Transient
	private Boolean reterIR;

	@Transient
	private Boolean reterINSS;

	@Transient
	private Boolean reterCOFINS;

	@Transient
	private BigDecimal issPorcentagem = BigDecimal.ZERO;

	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		if(this.historico != null ) {
			historico = historico.toUpperCase();
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

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public StatusContaPagar getStatus() {
		return status;
	}

	public void setStatus(StatusContaPagar status) {
		this.status = status;
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

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public PlanoContaSecundaria getPlanoContaSecundaria() {
		return planoContaSecundaria;
	}

	public void setPlanoContaSecundaria(PlanoContaSecundaria planoContaSecundaria) {
		this.planoContaSecundaria = planoContaSecundaria;
	}

	public Boolean getReterIR() {
		return reterIR;
	}

	public void setReterIR(Boolean reterIR) {
		this.reterIR = reterIR;
	}

	public Boolean getReterINSS() {
		return reterINSS;
	}

	public void setReterINSS(Boolean reterINSS) {
		this.reterINSS = reterINSS;
	}

	public Boolean getReterCOFINS() {
		return reterCOFINS;
	}

	public void setReterCOFINS(Boolean reterCOFINS) {
		this.reterCOFINS = reterCOFINS;
	}

	public BigDecimal getIssPorcentagem() {
		return issPorcentagem;
	}

	public void setIssPorcentagem(BigDecimal issPorcentagem) {
		this.issPorcentagem = issPorcentagem;
	}

	public PlanoConta getPlanoConta() {
		if (this.planoContaSecundaria != null){
			return this.planoContaSecundaria.getPlanoConta();
		}
		return planoConta;
	}

	public void setPlanoConta(PlanoConta planoConta) {
		this.planoConta = planoConta;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getRecibo() {
		return recibo;
	}

	public void setRecibo(Boolean recibo) {
		this.recibo = recibo;
	}

	public Boolean getTemNota() {
		return temNota;
	}

	public void setTemNota(Boolean temNota) {
		this.temNota = temNota;
	}	

	public List<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

	public Boolean getFixo() {
		return fixo;
	}

	public void setFixo(Boolean fixo) {
		this.fixo = fixo;
	}

	public String getUpload() {
		return upload;
	}

	public void setUpload(String upload) {
		this.upload = upload;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
		ContaPagar other = (ContaPagar) obj;
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

	public boolean isAutorizado() {
		return this.status.equals(StatusContaPagar.AUTORIZADO);
	}

	public boolean isEmitido() {
		return this.status.equals(StatusContaPagar.EMITIDO);
	}

	public boolean isPago() {
		return this.status.equals(StatusContaPagar.PAGO);
	}

	public boolean isPagoOuEmitido() {
		return this.status.equals(StatusContaPagar.PAGO) || this.status.equals(StatusContaPagar.EMITIDO);
	}
	
	
	public boolean isPendencia () {
		return this.recibo.equals(false) || this.temNota.equals(false);
	}
	
	public boolean isTemImpostoRetido() {
		return this.reterCOFINS.equals(true) 
				|| this.reterINSS.equals(true) 
				|| this.reterIR.equals(true) 
				|| this.issPorcentagem.compareTo(BigDecimal.ZERO) == 1;
	}
	
	public boolean isIssRetido() {
		return this.issPorcentagem != null || this.issPorcentagem.compareTo(BigDecimal.ZERO) ==1;
	}

}
