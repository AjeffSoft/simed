package com.ajeff.simed.financeiro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Empresa;

@Entity
@Table(name = "pagamento")
public class Pagamento implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull(message="Informe a data de pagamento")
	private LocalDate data;

	@Column(name="data_pago")
	private LocalDate dataPago;

	private String tipo;

	private String documento;
	
	private Boolean fechado;
	
	private String status; 

	private BigDecimal valor;
	
	//Opção se marcado gera todas as contas em um único pagamento
	@Transient
	private Boolean unico;
	
	@Transient
	private Integer numCheque;
	
	@ManyToOne
	@JoinColumn(name = "id_conta_empresa")
	private ContaEmpresa contaEmpresa;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;
	
	@OneToMany(mappedBy = "pagamento")
	private List<ContaPagar> itens;
	
	@ManyToOne
	@JoinColumn(name = "id_movimentacao")
	private MovimentacaoBancaria movimentacao;
	
	
		
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

	public LocalDate getDataPago() {
		return dataPago;
	}

	public void setDataPago(LocalDate dataPago) {
		this.dataPago = dataPago;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public ContaEmpresa getContaEmpresa() {
		return contaEmpresa;
	}

	public void setContaEmpresa(ContaEmpresa contaEmpresa) {
		this.contaEmpresa = contaEmpresa;
	}

	public List<ContaPagar> getItens() {
		return itens;
	}

	public void setItens(List<ContaPagar> itens) {
		this.itens = itens;
	}

	public Boolean getUnico() {
		return unico;
	}

	public void setUnico(Boolean unico) {
		this.unico = unico;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public MovimentacaoBancaria getMovimentacao() {
		return movimentacao;
	}

	public void setMovimentacao(MovimentacaoBancaria movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Boolean getFechado() {
		return fechado;
	}

	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

	public Integer getNumCheque() {
		return numCheque;
	}

	public void setNumCheque(Integer numCheque) {
		this.numCheque = numCheque;
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
		Pagamento other = (Pagamento) obj;
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
	
	public boolean isPago() {
		return this.status.equals("PAGO");
	}

	public boolean isPagoEAberto() {
		return this.status.equals("PAGO") && this.fechado.equals(false);
	}
	
	public boolean isEmitidoEAberto() {
		return this.status.equals("EMITIDO") && this.fechado.equals(false);
	}
	
	public boolean isCheque() {
		return this.tipo.equals("CHEQUE");
	}

}
