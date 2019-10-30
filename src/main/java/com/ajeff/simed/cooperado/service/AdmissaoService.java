package com.ajeff.simed.cooperado.service;

import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.repository.AdmissoesRepository;
import com.ajeff.simed.cooperado.repository.filter.AdmissaoFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class AdmissaoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AdmissaoService.class);	
	
	@Autowired
	private AdmissoesRepository repository;
	@Autowired
	private CooperadoService cooperadoService;
	
	
	@Transactional
	public void salvar(AdmissaoCooperado admissao) {
		if (admissao.isNovo()) {
			cooperadoService.ativarCooperado(admissao.getCooperado());
		}
		
		testeRegistroJaCadastrado(admissao);		
		repository.save(admissao);
	}
	
	@Transactional
	public void ativarAdmissao(AdmissaoCooperado admissao) {
		admissao.setAtivo(true);		
		repository.save(admissao);
	}
	
	@Transactional
	public void desativarAdmissao(AdmissaoCooperado admissao) {
		admissao.setAtivo(false);		
		repository.save(admissao);
	}

	public AdmissaoCooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<AdmissaoCooperado> filtrar(AdmissaoFilter admissaoFilter, Pageable pageable) {
		return repository.filtrar(admissaoFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "a admissão";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}


	private void testeRegistroJaCadastrado(AdmissaoCooperado admissao) {
		Optional<AdmissaoCooperado> optional = repository.findByRegistro(admissao.getRegistro());
		
		if(optional.isPresent() && !optional.get().equals(admissao)) {
			throw new RegistroJaCadastradoException("Registro de admissão já cadastrado!");
		}
	}

}
