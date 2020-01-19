package com.ajeff.simed.financeiro.service;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatoriosService {
	
	@Autowired
	DataSource dataSource;
	
	
	public byte[] imprimirHistoricoFornecedor(Long id) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("format", "pdf");
		map.put("id_fornecedor", id);
		
		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/rel_FORNECEDOR_Historico.jasper");
		
		Connection con = this.dataSource.getConnection();
		
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} finally {
			con.close();
		}
	}

}
