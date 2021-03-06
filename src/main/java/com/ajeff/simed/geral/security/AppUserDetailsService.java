package com.ajeff.simed.geral.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.UsuariosRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuariosRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String nome) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptional = repository.findByNomeAndAtivoTrue(nome);
		Usuario usuario = usuarioOptional.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorreto"));
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		List<String> permissoes = repository.permissoes(usuario);
		permissoes.forEach(p -> authorities.add(new SimpleGrantedAuthority("ROLE_" + p.toUpperCase())));
		
		return authorities;
	}

}