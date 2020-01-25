package com.ajeff.simed.geral.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.repository.filter.EmpresaFilter;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.EstadoService;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {
	
	@Autowired
	private EmpresaService service;
	@Autowired
	private EstadoService estadoService;
	
	@GetMapping("/nova")
	public ModelAndView nova(Empresa empresa){
		ModelAndView mv = new ModelAndView("Geral/empresa/CadastroEmpresa");
		mv.addObject("estados", estadoService.findTodosOrderByNome());
		return mv;
	}
	
	@PostMapping(value = {"/nova", "{\\d+}"})
	public ModelAndView cadastrar(@Valid Empresa empresa, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()){
			return nova(empresa);
		}
		try{
			service.salvar(empresa);
		}catch (RegistroJaCadastradoException e) {
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(empresa);
		}
		attributes.addFlashAttribute("mensagem", "Empresa " + empresa.getNome() + " salva com sucesso");
		return new ModelAndView("redirect:/empresa/nova");
	}
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(EmpresaFilter empresaFilter, BindingResult result, 
			@PageableDefault(size=100) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView mv = new ModelAndView("Geral/empresa/PesquisarEmpresas");

		PageWrapper<Empresa> paginaWrapper = new PageWrapper<>(service.filtrar(empresaFilter, pageable),httpServletRequest);		
		mv.addObject("pagina", paginaWrapper);
		return mv;
	}
	
	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Long id){
		try {
			service.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(value = "/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id, Empresa empresa){
		empresa = service.buscarComCidadeEstado(id);
		ModelAndView mv = nova(empresa);
		mv.addObject(empresa);
		return mv;
	}

}
