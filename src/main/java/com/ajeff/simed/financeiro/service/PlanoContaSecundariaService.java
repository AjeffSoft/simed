package com.ajeff.simed.financeiro.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.PlanosContaSecundariaRepository;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class PlanoContaSecundariaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PlanoContaSecundariaService.class);	
	
	@Autowired
	private PlanosContaSecundariaRepository repository;
	
	
	@Transactional
	public PlanoContaSecundaria salvar(PlanoContaSecundaria planoContaSecundaria) {
		
		testeRegistroJaCadastrado(planoContaSecundaria);
		return repository.save(planoContaSecundaria);
	}

	public List<PlanoContaSecundaria> findAll() {
		return repository.findAll();
	}
	
	public PlanoContaSecundaria findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a conta secundária. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	
	public List<PlanoContaSecundaria> findByPlanoContaId(Long idPlanoConta) {
		return repository.findByPlanoContaIdAndSituacaoTrue(idPlanoConta);
	}
	public List<PlanoContaSecundaria> listarTodosSubPlanosContaDebito() {
		return repository.listarTodosPlanosContaSecundariaDebito();
	}
	public List<PlanoContaSecundaria> listarTodosSubPlanosContaCredito() {
		return repository.listarTodosPlanosContaSecundariaCredito();
	}


	private void testeRegistroJaCadastrado(PlanoContaSecundaria subPlanoConta) {
		Optional<PlanoContaSecundaria> optional = repository.findByNomeAndPlanoContaTipoIgnoreCase(subPlanoConta.getNome(), subPlanoConta.getPlanoConta().getTipo());
		if(optional.isPresent() && !optional.get().equals(subPlanoConta)) {
			throw new RegistroJaCadastradoException("Já existe uma conta com este nome e do tipo "+subPlanoConta.getPlanoConta().getTipo() +" cadastrado!");
		}
	}

	public List<PlanoContaSecundaria> findAllForIdPlanoConta(Long idPlanoConta) {
		return repository.findByPlanoContaId(idPlanoConta);
	}


}
