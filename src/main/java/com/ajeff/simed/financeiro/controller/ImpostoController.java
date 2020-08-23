package com.ajeff.simed.financeiro.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.service.FornecedorService;
import com.ajeff.simed.financeiro.service.contaPagar.ContaPagarService;
import com.ajeff.simed.financeiro.service.imposto.ImpostoService;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.util.CalculosComValores;

@Controller
@RequestMapping("/financeiro/imposto")
public class ImpostoController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ImpostoController.class);

	@Autowired
	private ImpostoService service;
	@Autowired
	private FornecedorService fornecedorService;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private ContaPagarService contaPagarService;
	

//	@GetMapping("/gerar/{id}")
//	public ModelAndView abreGerar(@PathVariable Long id, Imposto imposto, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("Financeiro/imposto/GerarImposto");
//		imposto = service.findOne(id);
//		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
//		mv.addObject(imposto);
//		return mv;
//	}
//	
//	@PutMapping("/cancelarGerar")
//	@ResponseStatus(HttpStatus.OK)
//	public void cancelarAutorizarPagamento(@RequestParam Long id) {
//		Imposto imposto = service.findOne(id);
//		service.cancelarGerar(imposto);
//	}	
//	
//
//	
//	@PostMapping("/gerar/{id}")
//	public ModelAndView gerarImposto(@PathVariable Long id, @Valid Imposto imposto, 
//			 @AuthenticationPrincipal UsuarioSistema usuarioSistema, BindingResult result, RedirectAttributes attributes) {
//		
//		if (result.hasErrors()) {
//			return abreGerar(id, imposto, usuarioSistema);
//		}
//		
//		try {
//			service.gerar(imposto);
//		} catch (PagamentoNaoEfetuadoException e) {
//			result.rejectValue("valor", e.getMessage(), e.getMessage());
//			return abreGerar(id, imposto, usuarioSistema);
//		} catch (VencimentoMenorEmissaoException e) {
//			result.rejectValue("vencimento", e.getMessage(), e.getMessage());
//			return abreGerar(id, imposto, usuarioSistema);
//		}
//		return new ModelAndView("redirect:/financeiro/imposto/pesquisar");
//	}
//
//	
//	@GetMapping("/pesquisar")
//	public ModelAndView pesquisar(ImpostoFilter impostoFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
//										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("Financeiro/imposto/PesquisarImpostos");
//		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
//		PageWrapper<Imposto> paginaWrapper = new PageWrapper<>(service.filtrar(impostoFilter, pageable), httpServletRequest);
//		mv.addObject("pagina", paginaWrapper);
//		return mv;
//	}
//
//	
//	@DeleteMapping("/excluir/{id}")
//	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long id){
//		try {
//			service.excluir(id);
//		} catch (ImpossivelExcluirEntidade e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//		return ResponseEntity.ok().build();
//	}
//	
//
//	@GetMapping("/detalhe/{id}")
//	public ModelAndView detalhe(@PathVariable Long id, Imposto imposto) {
//		ModelAndView mv = new ModelAndView("Financeiro/imposto/DetalheImposto");
//		imposto = service.findOne(id);
//		mv.addObject("contas", contaPagarService.findOne(imposto.getContaPagar().getId()));
//		mv.addObject(imposto);
//		return mv;
//	}
	
	@GetMapping(value = "/getISS", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getISS(String aliquota, String valor) {

		try {
			BigDecimal imposto = service.valorISSRetido(CalculosComValores.convertRealToDollar(valor), CalculosComValores.convertRealToDollar(aliquota));
			return imposto.toString();
		} catch (NumberFormatException e) {
			return "Informe o valor da conta para o calculo!";
		}
	}
	
	@GetMapping(value = "/getInss", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getInss(String valor, String idFornecedor) {
		try {
			Long idFornecedorConvert = Long.decode(idFornecedor);
			Fornecedor forn = fornecedorService.findOne(idFornecedorConvert);
			BigDecimal imposto = service.valorINSSRetido(CalculosComValores.convertRealToDollar(valor), forn.getTipo());
			return imposto.toString();
		} catch (NumberFormatException e) {
			return "Informe o valor da conta para o calculo!";
		}
	}
	
	@GetMapping(value = "/getPccs", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getPccs(String valor, String idFornecedor) {
		try {
			Long idFornecedorConvert = Long.decode(idFornecedor);
			Fornecedor forn = fornecedorService.findOne(idFornecedorConvert);
			BigDecimal imposto = service.valorPCCSRetido(CalculosComValores.convertRealToDollar(valor), forn.getTipo());
			return imposto.toString();
		} catch (NumberFormatException e) {
			return "Informe o valor da conta para o calculo!";
		}
	}

	@GetMapping(value = "/getIrrf", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String getIrrf(String valor, String idFornecedor, String inss) {
		BigDecimal valorInss = BigDecimal.ZERO;
		try {
			
			if(inss.equals("S")) {
				String inssRetido = getInss(valor, idFornecedor);
				valorInss = new BigDecimal(inssRetido);
			}
			
			Long idFornecedorConvert = Long.decode(idFornecedor);
			Fornecedor forn = fornecedorService.findOne(idFornecedorConvert);
			BigDecimal imposto = service.valorIRRFRetido(CalculosComValores.convertRealToDollar(valor), valorInss, BigDecimal.ZERO, forn.getTipo());
			return imposto.toString();
		} catch (NumberFormatException e) {
			return "Informe o valor da conta para o calculo!";
		}
	}
	
}
