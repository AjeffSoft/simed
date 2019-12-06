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
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "imposto")
public class Imposto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "Informe o a data de apuração")
	private LocalDate apuracao;
	
	private String documento;

	@NotBlank(message = "Informe o código do imposto")
	private String codigo;
	
	private String nome;
	
	private String upload;

	@Column(name = "content_type")
	private String contentType;
	
	@NotNull(message = "Informe o vencimento do imposto")
	private LocalDate vencimento;

	@Column(name = "data_pago")
	private LocalDate dataPago;
	
	@NotNull(message = "Informe o valor do imposto")
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

	private String status;
	
	@Transient
	private Boolean gerarDarf;

	private String historico;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "id_conta_pagar_origem")
	private ContaPagar contaPagarOrigem;

	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "id_conta_pagar_gerada")
	private ContaPagar contaPagarGerada;
	
	
//	@JsonIgnore
//	@ManyToOne
//	@JoinColumn(name = "id_empresa")
//	private Empresa empresa;
	
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

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public ContaPagar getContaPagarGerada() {
		return contaPagarGerada;
	}


	public void setContaPagarGerada(ContaPagar contaPagarGerada) {
		this.contaPagarGerada = contaPagarGerada;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public LocalDate getDataPago() {
		return dataPago;
	}

	public void setDataPago(LocalDate dataPago) {
		this.dataPago = dataPago;
	}

	public ContaPagar getContaPagarOrigem() {
		return contaPagarOrigem;
	}

	public void setContaPagarOrigem(ContaPagar contaPagarOrigem) {
		this.contaPagarOrigem = contaPagarOrigem;
	}

//	public Empresa getEmpresa() {
//		return empresa;
//	}
//
//	public void setEmpresa(Empresa empresa) {
//		this.empresa = empresa;
//	}

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
		return this.status.equals("ABERTO");
	}

	public boolean isGerado() {
		return this.status.equals("GERADO");
	}
	
	public boolean isPago() {
		return this.status.equals("PAGO");
	}
	
}
