package com.ajeff.simed.geral.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.helper.usuario.UsuariosRepositoryQueries;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuario, Long>, UsuariosRepositoryQueries{

	Optional<Usuario> findByNome(String nome);

	Optional<Usuario> findByNomeAndAtivoTrue(String username);

	@Query("select u from Usuario u where u.nome like ?1")
	Usuario buscarUsuarioLogado(String name);

	@Query("select u from Usuario u where u.empresa <> null")
	List<Usuario> findByUsuarioAtendentes();

}