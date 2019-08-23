package com.ajeff.simed.financeiro.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.swing.ImageIcon;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.financeiro.model.TransferenciaContas;
import com.ajeff.simed.financeiro.repository.filter.TransferenciaFilter;
import com.ajeff.simed.financeiro.service.TransferenciaService;
import com.ajeff.simed.financeiro.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.financeiro.service.exception.PeriodoMovimentacaoException;
import com.ajeff.simed.financeiro.service.exception.TransferenciaNaoEfetuadaException;
import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.ContaEmpresaService;
import com.ajeff.simed.geral.service.UsuarioService;

@Controller
@RequestMapping("/financeiro/transferencia")
public class TransferenciaController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TransferenciaController.class);
	

	@Autowired
	private TransferenciaService service;
	@Autowired
	private ContaEmpresaService contaEmpresaService;
	@Autowired
	private UsuarioService usuarioService;

	
	
	@GetMapping("/novo")
	private ModelAndView nova(TransferenciaContas transferenciaContas, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/transferencia/CadastroTransferencia");
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("contasOrigem", contaEmpresaService.listarTodosOrdenadoPorNome());
		mv.addObject("contasDestino", contaEmpresaService.listarTodosOrdenadoPorNome());
		return mv;
	}
	
	
	@PostMapping(value = {"/novo", "{\\d}"})
	public ModelAndView salvar(@Valid TransferenciaContas transferenciaContas, BindingResult result, RedirectAttributes attributes,
									@AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		if(result.hasErrors()) {
			return nova(transferenciaContas, usuarioSistema);
		}
		
		try {
			service.salvar(transferenciaContas);
		} catch (PeriodoMovimentacaoException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return nova(transferenciaContas, usuarioSistema);
		} catch (TransferenciaNaoEfetuadaException e) {
			result.rejectValue("data", e.getMessage(), e.getMessage());
			return nova(transferenciaContas, usuarioSistema);
		}
		attributes.addFlashAttribute("mensagem", "Transferencia nº " + transferenciaContas.getId() + " salva com sucesso");
		return new ModelAndView("redirect:/financeiro/transferencia/comprovante/" + transferenciaContas.getId());
	}


	//ABRE O COMPROVANTE DA TRANSFERENCIA
	@GetMapping("/comprovante/{id}")
	public ModelAndView comprovante(@PathVariable Long id, TransferenciaContas transferenciaContas) {
		ModelAndView mv = new ModelAndView("Financeiro/transferencia/ComprovanteTransferencia");
		transferenciaContas = service.findOne(id);
		mv.addObject(transferenciaContas);
		return mv;
	}

	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(TransferenciaFilter transferenciaFilter, BindingResult result, @PageableDefault(size=50) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Financeiro/transferencia/PesquisarTransferencias");
		mv.addObject("contasOrigem", contaEmpresaService.listarTodosOrdenadoPorNome());
		mv.addObject("contasDestino", contaEmpresaService.listarTodosOrdenadoPorNome());
		mv.addObject("empresas", usuarioService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		PageWrapper<TransferenciaContas> paginaWrapper = new PageWrapper<>(service.filtrar(transferenciaFilter, pageable), httpServletRequest);
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}

	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable Long id){

		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	
	@PutMapping("/cancelarPagar")
	@ResponseStatus(HttpStatus.OK)
	public void cancelarPagar(@RequestParam Long id) {
		TransferenciaContas transferencia = service.findOne(id);
		service.cancelarOuPagar(transferencia);
	}	
	
	
	@GetMapping("/imprimirOrdemTransferencia/{id}")
	public ModelAndView imprimirOrdemPagamento(@PathVariable Long id) {
		TransferenciaContas transferencia = service.findOne(id);
		Map<String, Object> map = new HashMap<>();
		ImageIcon gto = new ImageIcon(getClass().getResource("/relatorios/logo" +transferencia.getEmpresa().getId()+".jpg"));
		map.put("format", "pdf");
		map.put("logo", gto.getImage());
		map.put("id_transferencia", id);
		return new ModelAndView("rel_TRANSFERENCIA_OrdemTransferencia", map);
	}
}
