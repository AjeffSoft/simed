package com.ajeff.simed.financeiro.service.contaPagar;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.dto.PeriodoRelatorio;
import com.ajeff.simed.util.CalculosComDatas;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

@Service
public class RelatoriosContaPagarService {
	
	
	@Autowired
	DataSource dataSource;
	
	
	public byte[] imprimirPendenciasContasPagar(PeriodoRelatorio periodoRelatorio) throws Exception {
		Map<String, Object> map = new HashMap<>();
	
		setarValoresPadrao(periodoRelatorio);		

		if(periodoRelatorio.getEmpresa() != null) {
			map.put("id_empresa", periodoRelatorio.getEmpresa().getId());
		}
		if(periodoRelatorio.getPessoa() != null) {
			map.put("id_fornecedor", periodoRelatorio.getPessoa().getId());
		}
		if(!periodoRelatorio.getStatus().isEmpty()) {
			map.put("status", periodoRelatorio.getStatus());
		}
		map.put("vencimento_inicio", CalculosComDatas.convertLocalDateInDate(periodoRelatorio.getDataInicio()));			
		map.put("vencimento_final", CalculosComDatas.convertLocalDateInDate(periodoRelatorio.getDataFim()));
		map.put("emissao_inicio", CalculosComDatas.convertLocalDateInDate(periodoRelatorio.getEmissaoInicio()));			
		map.put("emissao_final", CalculosComDatas.convertLocalDateInDate(periodoRelatorio.getEmissaoFim()));
		map.put("valor_inicio", periodoRelatorio.getValorInicio());
		map.put("valor_final", periodoRelatorio.getValorFim());
		map.put("format", "pdf");

		InputStream inputStream = this.getClass().getResourceAsStream("/relatorios/rel_Financeiro_ContaPagar_Pendencias.jasper");
		Connection con = this.dataSource.getConnection();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, con);
			return JasperExportManager.exportReportToPdf(jasperPrint);
		}catch (Exception e) {
			e.getMessage();
			return null;
		} finally {
			con.close();
		}
	}


	private void setarValoresPadrao(PeriodoRelatorio periodoRelatorio) {
		if(periodoRelatorio.getDataInicio() == null) {
			periodoRelatorio.setDataInicio(LocalDate.of(1900, 1, 1));
		}	

		if(periodoRelatorio.getDataFim() == null) {
			periodoRelatorio.setDataFim(LocalDate.of(2500, 12, 31));
		}
		
		if(periodoRelatorio.getEmissaoInicio() == null) {
			periodoRelatorio.setEmissaoInicio(LocalDate.of(1900, 1, 1));
		}
		
		if(periodoRelatorio.getEmissaoFim() == null) {
			periodoRelatorio.setEmissaoFim(LocalDate.of(2500, 12, 31));
		}
		
		if(periodoRelatorio.getValorInicio() == null){
			periodoRelatorio.setValorInicio(BigDecimal.ZERO);
		}
		
		if(periodoRelatorio.getValorFim() == null){
			periodoRelatorio.setValorFim(BigDecimal.valueOf(1000000000));
		}
	}

}

