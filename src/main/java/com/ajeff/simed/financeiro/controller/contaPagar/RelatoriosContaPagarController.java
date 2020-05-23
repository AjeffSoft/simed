package com.ajeff.simed.financeiro.controller.contaPagar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ajeff.simed.financeiro.dto.PeriodoRelatorio;
import com.ajeff.simed.financeiro.service.contaPagar.RelatoriosContaPagarService;

@Controller
@RequestMapping("/relatorios/financeiro/contaPagar")
public class RelatoriosContaPagarController {

	@Autowired
	private RelatoriosContaPagarService service;
	
	
	@GetMapping
	public ResponseEntity<byte[]> imprimirPendenciasContasPagar(PeriodoRelatorio periodoRelatorio) throws Exception {
		byte[] relatorio = service.imprimirPendenciasContasPagar(periodoRelatorio);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}	
}
