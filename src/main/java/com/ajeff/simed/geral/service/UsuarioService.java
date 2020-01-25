package com.ajeff.simed.geral.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.UsuariosRepository;
import com.ajeff.simed.geral.service.exception.CPFUsuarioExistenteException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.SenhaObrigatoriaUsuarioException;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
public class UsuarioService {

	@Autowired
	private UsuariosRepository repository;
	
	@Transactional(readOnly = false)
	public void delete (Long id){
		repository.delete(id);
	}
	
	@Transactional
	public void salvar(Usuario usuario){
		Optional<Usuario> usuarioExistente = repository.findByNome(usuario.getNome());
		
		if(usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)){
			throw new CPFUsuarioExistenteException("Nome de usuário já cadastrado!!!");
		}
		
		if (usuario.isNovo() && StringUtils.isEmpty(usuario.getSenha())){
			throw new SenhaObrigatoriaUsuarioException("Senha é obrigatória para novo usuário");
		}
		
		if (usuario.isNovo() || !StringUtils.isEmpty(usuario.getSenha())) {
			String hash = new BCryptPasswordEncoder().encode(usuario.getSenha());
			usuario.setSenha(hash);
		}else if(StringUtils.isEmpty(usuario.getSenha())){
			usuario.setSenha(usuarioExistente.get().getSenha());
		}
		usuario.setConfirmacaoSenha(usuario.getSenha());
		repository.save(usuario);
	}
	
	
	@Transactional(readOnly=false)
	public void save (Usuario usuario){
		
		String hash = new BCryptPasswordEncoder().encode(usuario.getSenha());
		usuario.setSenha(hash);
		repository.save(usuario);
	}

	public Usuario findById(Long id){
		return repository.findOne(id);
	}
	
	public List<Usuario> findTodos() {
		return repository.findAll();
	}

	public Optional<Usuario> findByNomeAndAtivo(String username) {
		return repository.findByNomeAndAtivoTrue(username);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		}catch (PersistenceException e){
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o registro."); 
		}
	}

	public Usuario buscarUsuarioComGrupos(Long id) {
		return repository.buscarUsuarioComGrupos(id);
	}
}
