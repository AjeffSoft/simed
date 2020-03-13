package com.ajeff.simed.financeiro.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.ImageIcon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.model.Movimentacao;
import com.ajeff.simed.financeiro.model.MovimentacaoItem;
import com.ajeff.simed.financeiro.model.Pagamento;
import com.ajeff.simed.financeiro.model.TransferenciaContas;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Service
public class RelatoriosService {
	
	@Autowired
	DataSource dataSource;
	@Autowired
	private PagamentoService pagamentoService;
	@Autowired
	private TransferenciaService transferenciaService;
	@Autowired
	private MovimentacaoItensService movimentacaoItemService;
	@Autowired
	private MovimentacaoService movimentacaoService;
	
	
	
	
	public byte[] imprimirHistoricoFornecedor(Long id) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/Blank_A4.jasper");
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}
	

	
	public byte[] imprimirOrdemPagamento(Long id) throws Exception {
		Pagamento pagamento = pagamentoService.findOne(id);
		Map<String, Object> map = new HashMap<>();
		ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +pagamento.getContaEmpresa().getEmpresa().getId()+".jpg"));
		map.put("format", "pdf");
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		map.put("logo", gto.getImage());
		map.put("id_pagamento", id);
		InputStream inputStream = null;
		if(pagamento.getTipo().equals("DEBITO")) {
			JasperReport subReport =  JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Pagamento_OrdemPagamentoItens.jrxml"));
			map.put("ProductsSubReport", subReport);
			inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Pagamento_OrdemPagamento.jasper");
		}else {
			inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Pagamento_OrdemTransferencia.jasper");
		}
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}
	
	

	public byte[] imprimirOrdemTransferencia(Long id) throws Exception {
		TransferenciaContas transferencia = transferenciaService.findOne(id);
		Map<String, Object> map = new HashMap<>();
		ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +transferencia.getContaOrigem().getEmpresa().getId()+".jpg"));
		map.put("format", "pdf");
		map.put("logo", gto.getImage());
		map.put("id_transferencia", id);
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Transferencia_OrdemTransferencia.jasper");
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}
	
	

	public byte[] imprimirExtratoPorMovimentacao(Long id) throws Exception {
		MovimentacaoItem movimentacaoItem = movimentacaoItemService.findOne(id);
		Map<String, Object> map = new HashMap<>();
		ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +movimentacaoItem.getMovimentacao().getEmpresa().getId()+".jpg"));
		map.put("format", "pdf");
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		map.put("logo", gto.getImage());
		map.put("id_movimentacao", id);
		InputStream inputStream = null;
		JasperReport subReport =  JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Pagamento_DetalhesItens.jrxml"));
		JasperReport subReport1=  JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Recebimento_DetalhesItens.jrxml"));
		JasperReport subReport2 =  JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Transferencia_DetalhesItens.jrxml"));
		map.put("ProductsSubReport", subReport);
		map.put("ProductsSubReport1", subReport1);
		map.put("ProductsSubReport2", subReport2);

		inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Extrato_MovimentoDetalhado.jasper");
		
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}
	
	
	public byte[] imprimirDetalhesMovimentacao(Long id) throws Exception {
		Movimentacao movimentacao = movimentacaoService.findOne(id);
		Map<String, Object> map = new HashMap<>();
		ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +movimentacao.getEmpresa().getId()+".jpg"));
		map.put("format", "pdf");
		map.put("REPORT_LOCALE", new Locale("pt", "BR"));
		map.put("logo", gto.getImage());
		map.put("id_movimentacao", id);
		InputStream inputStream = null;
		JasperReport subReport =  JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_Movimentacao_Itens.jrxml"));
		map.put("ProductsSubReport", subReport);
		inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_MovimentacaoDetalhado.jasper");
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}	

}
