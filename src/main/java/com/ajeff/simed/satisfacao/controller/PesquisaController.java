package com.ajeff.simed.satisfacao.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.UsuarioService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.PesquisaItem;
import com.ajeff.simed.satisfacao.model.Questionario;
import com.ajeff.simed.satisfacao.model.Resposta;
import com.ajeff.simed.satisfacao.repository.PesquisasRepository;
import com.ajeff.simed.satisfacao.repository.filter.PesquisaFilter;
import com.ajeff.simed.satisfacao.service.PesquisaService;
import com.ajeff.simed.satisfacao.service.QuestionarioService;
import com.ajeff.simed.satisfacao.service.RespostaService;

@Controller
@RequestMapping("/satisfacao/pesquisa")
public class PesquisaController {

	@Autowired
	private PesquisaService service;
	@Autowired
	private PesquisasRepository repository;
	@Autowired
	private QuestionarioService questionarioService;
	@Autowired
	private RespostaService respostaService;
	@Autowired
	private EmpresaService empresaService;
	@Autowired
	private UsuarioService usuarioService;
	
	
	@GetMapping("/inicio")
	public ModelAndView abrir() {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/InicioPesquisa");
		return mv;
	}
	

	@GetMapping("/fim")
	public ModelAndView agradecimento() {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/FinalPesquisa");
		return mv;
	}	

	@PostMapping(value = "/itens", consumes = {MediaType.APPLICATION_JSON_VALUE})
	public void itens(@RequestBody Questionario questionario) {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>.");
	}	

	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(PesquisaFilter pesquisaFilter, BindingResult result, @PageableDefault(size=100) Pageable pageable,
										HttpServletRequest httpServletRequest, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/PesquisarPesquisas");
		mv.addObject("empresas", empresaService.buscarEmpresaPorUsuario(usuarioSistema.getUsuario().getId()));
		mv.addObject("usuarios", usuarioService.findByUsuarioAtendentes());

		PageWrapper<Pesquisa> paginaWrapper = new PageWrapper<>(service.filtrar(pesquisaFilter, pageable), httpServletRequest);
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
	
	
	@GetMapping("/detalhe/{id}")
	public ModelAndView detalhe(@PathVariable Long id, Pesquisa pesquisa) {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/DetalhePesquisa");
		pesquisa = service.findOne(id);
		mv.addObject("respostas", service.findByPesquisa(pesquisa));
		mv.addObject(pesquisa);
		return mv;
	}	
	
	
//	@GetMapping("/novo")
//	public ModelAndView novo(Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/CadastroPesquisa");
//		List<Questionario> questionarios = questionarioService.findByEmpresaAndAtivoTrue(usuarioSistema.getUsuario().getEmpresa());
//		List<Resposta> respostas = new ArrayList<Resposta>();
//		for (Questionario q : questionarios) {
//			respostas.clear();
//			q.setRespostas(respostaService.buscarQuestionarioComRespostas(q.getId()));
//		}
//		mv.addObject("questionarios", questionarios);
//		return mv;
//	}	
	
	
	@GetMapping("/novo")
	public ModelAndView novo(Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		ModelAndView mv = new ModelAndView("Satisfacao/pesquisa/CadastroPesquisa");
		List<Questionario> questionarios = questionarioService.findByEmpresaAndAtivoTrue(usuarioSistema.getUsuario().getEmpresa());
		List<Resposta> respostas = new ArrayList<Resposta>();

		List<PesquisaItem> itens = new ArrayList<>();

		for (Questionario q : questionarios) {
			q.setRespostas(respostaService.buscarQuestionarioComRespostas(q.getId()));
			respostas.clear();
			PesquisaItem pi = new PesquisaItem();
			pi.setQuestionario(q);
			itens.add(pi);
		}
		
		mv.addObject("itens", itens);
		return mv;
	}		
	
	
	



	
	
	//	@GetMapping("/novo")
//	public ModelAndView novo(Pesquisa pesquisaSatisfacao, Pergunta questionario, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//		ModelAndView mv = new ModelAndView("Satisfacao/Pesquisa");
////		List<Pergunta> questionarios = questionarioService.listarTodasAsPerguntas(pesquisaSatisfacao, usuarioSistema);
//		pesquisaSatisfacao.setUuid(UUID.randomUUID().toString());
////		mv.addObject("questionarios", questionarios);
//		return mv;
//	}
	
	
	
//	@PostMapping("/questao")
//	public @ResponseBody String adicionarQuestao(Long pergunta, String resposta, String uuid) {
//		Questionario questao = questaoService.findOne(pergunta);
//		tabelaQuestionarios.adicionarQuestao(uuid, questao, resposta);
//		return "questão adicionada!";
//	}

	

//	@PostMapping("/novo")
//	public ModelAndView salvar(@Valid Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
//
////		pesquisa.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisa.getUuid()));
////		service.salvar(pesquisa, usuarioSistema.getUsuario());
//		return new ModelAndView("redirect:/satisfacao/pesquisa/fim");
//	}
	
	
	@PostMapping("/novo")
	public ModelAndView salvar(@RequestParam(value = "idQuestao", required = true) List<Long> ids, @RequestParam(value = "idResposta", required = false) List<Long> idResp, @Valid Pesquisa pesquisa, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {

//		pesquisa.adicionarQuestionarios(tabelaQuestionarios.getQuestionarios(pesquisa.getUuid()));
		service.salvar(pesquisa, usuarioSistema.getUsuario(), ids, idResp);
		return new ModelAndView("redirect:/satisfacao/pesquisa/fim");
	}	
}
