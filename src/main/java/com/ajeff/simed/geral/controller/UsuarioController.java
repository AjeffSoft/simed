package com.ajeff.simed.geral.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ajeff.simed.geral.controller.page.PageWrapper;
import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.UsuariosRepository;
import com.ajeff.simed.geral.repository.filter.UsuarioFilter;
import com.ajeff.simed.geral.service.EmpresaService;
import com.ajeff.simed.geral.service.GrupoService;
import com.ajeff.simed.geral.service.UsuarioService;
import com.ajeff.simed.geral.service.exception.CPFUsuarioExistenteException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.SenhaObrigatoriaUsuarioException;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	
	public static final Logger LOG = Logger.getLogger(UsuarioController.class);
	
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private UsuariosRepository usuarios;
	@Autowired
	private GrupoService grupoService;
	@Autowired
	private EmpresaService empresaService;

	
	@RequestMapping(value = "/novo", method = RequestMethod.GET)
	public ModelAndView novo(Usuario usuario) {
		ModelAndView mv = new ModelAndView("Geral/usuario/CadastroUsuario");
		mv.addObject("empresas", empresaService.buscarTodos());
		mv.addObject("grupos", grupoService.findAll());
		mv.addObject("empresas", empresaService.buscarTodos());
		return mv;
	}
	
	@RequestMapping(value = {"/novo", "{\\d+}"}, method = RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult result,RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return novo(usuario);
		}
		try{
			usuarioService.salvar(usuario);
		}catch(CPFUsuarioExistenteException e){
			result.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(usuario);
		}catch(SenhaObrigatoriaUsuarioException e){
			result.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}
		attributes.addFlashAttribute("mensagem", "Usuário salvo com sucesso");
		return new ModelAndView("redirect:/usuario/novo");
	}

	@DeleteMapping("/excluir/{id}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("id") Long id){
		try{
			usuarioService.excluir(id);
		} catch (ImpossivelExcluirEntidade e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}	
	
	
	@GetMapping("/pesquisar")
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter 
			, @PageableDefault(size = 100) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("Geral/usuario/PesquisarUsuarios");
		mv.addObject("grupos", grupoService.findAll());
		
		PageWrapper<Usuario> paginaWrapper = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", paginaWrapper);		
		return mv;
	}
	
	@GetMapping("/alterar/{id}")
	public ModelAndView alterar(@PathVariable("id") Long id){
//		Usuario user = empresaService.buscarEmpresaPorUsuario(id);
//		List<Empresa> empresas = usuarioService.buscarEmpresaPorUsuario(id);
//		user.setEmpresas(empresas);
		Usuario usuario = usuarioService.buscarUsuarioComGrupos(id);
//		Usuario user = usuarioService.buscarEmpresaPorUsuario(id);
		usuario.setEmpresas(empresaService.buscarEmpresaPorUsuario(id));
//		usuario.setEmpresas(empresas);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}
	
}