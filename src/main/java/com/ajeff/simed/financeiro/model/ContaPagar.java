package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.model.Empresa;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table (name = "conta_pagar")
public class ContaPagar implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String historico;
	
	@NotNull(message = "Informe a data de emissão")
	@Column(name = "data_emissao")
	private LocalDate dataEmissao;
	
	private LocalDate vencimento;

	@NotNull(message = "Informe o valor da conta")
	private BigDecimal valor;
	
	private String documento;
	
	private String status;
	
	@Column(name = "nota_fiscal")
	private String notaFiscal;
	
	@Column(name= "prazo_parcelamento")
	private Integer prazoParcelamento;

	private String upload;

	@Column(name = "content_type")
	private String contentType;

	private Integer parcela;
	
	@Column(name = "total_parcela")
	private Integer totalParcela;

	@Column(name = "tipo_pendencia")
	private String tipoPendencia;
	
	private Boolean pendente;
	
	@JsonIgnore
	@NotNull(message = "Informe p plano de contas")
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
	
	@OneToMany(mappedBy = "contaPagarOrigem", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Imposto> impostos;

//	@JsonIgnore
//	@OneToOne
//	@JoinColumn(name = "id_imposto_gerado")
//	private Imposto impostoGerado;

	@ManyToOne
	@JoinColumn(name = "id_pagamento")
	private Pagamento pagamento;
	
	@Transient
	private PlanoConta planoConta;
	
	@Transient
	private Boolean reterISS;

	@Transient
	private Boolean reterIR;

	@Transient
	private Boolean reterINSS;

	@Transient
	private Boolean reterCOFINS;

	@Transient
	private BigDecimal issPorcentagem;

	@PrePersist
	@PreUpdate
	private void prePersistUpdate() {
		if(this.historico != null ) {
			historico = historico.toUpperCase();
		}

//		if(this.documento != null ) {
//			documento = documento.toUpperCase();
//		}
		
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

	public Integer getPrazoParcelamento() {
		return prazoParcelamento;
	}

	public void setPrazoParcelamento(Integer prazoParcelamento) {
		this.prazoParcelamento = prazoParcelamento;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public Boolean getReterISS() {
		return reterISS;
	}

	public void setReterISS(Boolean reterISS) {
		this.reterISS = reterISS;
	}

	public BigDecimal getIssPorcentagem() {
		return issPorcentagem;
	}

	public void setIssPorcentagem(BigDecimal issPorcentagem) {
		this.issPorcentagem = issPorcentagem;
	}

	public Boolean getPendente() {
		return pendente;
	}

	public void setPendente(Boolean pendente) {
		this.pendente = pendente;
	}

	public String getTipoPendencia() {
		return tipoPendencia;
	}

	public void setTipoPendencia(String tipoPendencia) {
		this.tipoPendencia = tipoPendencia;
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

	public List<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(List<Imposto> impostos) {
		this.impostos = impostos;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
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
	
	
	public boolean existeRetencaoImpostos() {
		return this.reterCOFINS || this.reterINSS || this.reterIR || this.reterISS;
	}
	
	public boolean isVencimentoMaiorEmissao() {
		return this.vencimento.isEqual(this.dataEmissao) || this.vencimento.isAfter(this.dataEmissao);
	}
	
	public boolean isValorNegativo() {
		return (this.valor.compareTo(BigDecimal.ZERO) <=0);
	}
	
	public boolean isAberto() {
		return this.status.equals("ABERTO");
	}

	public boolean isAutorizado() {
		return this.status.equals("AUTORIZADO");
	}

	public boolean isEmitido() {
		return this.status.equals("EMITIDO");
	}

	public boolean isPagoOuEmitido() {
		return this.status.equals("PAGO") || this.status.equals("EMITIDO");
	}
	
	
	public boolean isPago() {
		return this.status.equals("PAGO");
	}
	
	public boolean isTemUpload() {
		return !StringUtils.isEmpty(upload); 
	}
	
	public boolean isNovoOuPrimeiraParcela() {
		return isNovo() || this.parcela == 1;
	}
}
