package com.ajeff.simed.financeiro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ajeff.simed.financeiro.service.RelatoriosService;

@Controller
@RequestMapping("/relatorios/financeiro")
public class RelatoriosController {

	@Autowired
	private RelatoriosService service;
	
	
	@GetMapping("/fornecedor/historico/{id}")
	public ResponseEntity<byte[]> imprimirHistoricoFornecedor(@PathVariable Long id) throws Exception {
		byte[] relatorio = service.imprimirHistoricoFornecedor(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@GetMapping("/pagamento/ordemPagamento/{id}")
	public ResponseEntity<byte[]> imprimirOrdemPagamento(@PathVariable Long id) throws Exception {
		byte[] relatorio = service.imprimirOrdemPagamento(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@GetMapping("/transferencia/ordemTransferencia/{id}")
	public ResponseEntity<byte[]> imprimirOrdemTransferencia(@PathVariable Long id) throws Exception {
		byte[] relatorio = service.imprimirOrdemTransferencia(id);
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}	
}
