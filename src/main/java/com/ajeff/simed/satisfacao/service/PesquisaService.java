package com.ajeff.simed.satisfacao.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.security.UsuarioSistema;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.PesquisasRepository;

@Service
public class PesquisaService {

	
	@Autowired
	private PesquisasRepository repository;
	
	
	@Transactional
	public Pesquisa salvar(Pesquisa pesquisa, Usuario usuario) {
		pesquisa.setData(LocalDateTime.now());
		pesquisa.setUsuario(usuario);
		pesquisa.setFechado(true);
//		testeRegistroJaCadastrado(pergunta);
		return repository.save(pesquisa);
	}




//	public Page<Pergunta> filtrar(PerguntaFilter perguntaFilter, Pageable pageable) {
//		return repository.filtrar(perguntaFilter, pageable);
//	}
//
//	public List<Pergunta> findAll() {
//		return repository.findAll();
//	}
//	
//	public Pergunta findOne(Long id) {
//		return repository.findOne(id);
//	}
//	
//	@Transactional
//	public void excluir(Long id) {
//		try {
//			repository.delete(id);
//			repository.flush();
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a pergunta. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//		}
//		
//	}	
//
//	private void testeRegistroJaCadastrado(Pergunta pergunta) {
//		Optional<Pergunta> optional = repository.findByNomeIgnoreCase(pergunta.getNome());
//		if(optional.isPresent() && !optional.get().equals(pergunta)) {
//			throw new RegistroJaCadastradoException("Já existe uma pergunta com esse nome cadastrada!");
//		}
//	}
//
//	public List<Pergunta> findAllOrderByNome() {
//		return repository.findAllOrderByNome();
//	}

	
}
