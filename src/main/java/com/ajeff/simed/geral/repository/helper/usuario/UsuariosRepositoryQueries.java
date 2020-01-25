package com.ajeff.simed.geral.repository.helper.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.filter.UsuarioFilter;

public interface UsuariosRepositoryQueries {

	public Optional<Usuario> porNomeEAtivo(String nome);
	
	public List<String> permissoes(Usuario usuario);
	
	public Page<Usuario> filtrar(UsuarioFilter filtro, Pageable pageable);
	
	public Usuario buscarUsuarioComGrupos(Long id);
	
	public Usuario buscarEmpresaPorUsuario(Long id);

	
}