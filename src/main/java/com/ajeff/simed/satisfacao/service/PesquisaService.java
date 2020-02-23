package com.ajeff.simed.satisfacao.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.model.PesquisaItem;
import com.ajeff.simed.satisfacao.repository.PesquisasItensRepository;
import com.ajeff.simed.satisfacao.repository.PesquisasRepository;
import com.ajeff.simed.satisfacao.repository.filter.PesquisaFilter;

@Service
public class PesquisaService {

	
	@Autowired
	private PesquisasRepository repository;
	@Autowired
	private PesquisasItensRepository pesquisaItemRepository;	
	
	@Transactional
	public Pesquisa salvar(Pesquisa pesquisa, Usuario usuario) {
		pesquisa.setData(LocalDateTime.now());
		pesquisa.setUsuario(usuario);
		pesquisa.setFechado(true);
//		testeRegistroJaCadastrado(pergunta);
		return repository.save(pesquisa);
	}


	public Page<Pesquisa> filtrar(PesquisaFilter pesquisaFilter, Pageable pageable) {
		return repository.filtrar(pesquisaFilter, pageable);
	}


	@Transactional
	public void excluir(Long id) {
		
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a pesquisa. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}


	public Pesquisa findOne(Long id) {
		return repository.findOne(id);
	}


	public List<PesquisaItem> findByPesquisa(Pesquisa pesquisa) {
		return pesquisaItemRepository.findByPesquisa(pesquisa);
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
