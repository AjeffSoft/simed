package com.ajeff.simed.financeiro.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.PlanoConta;
import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.PlanosContaRepository;
import com.ajeff.simed.financeiro.repository.PlanosContaSecundariaRepository;
import com.ajeff.simed.financeiro.repository.filter.PlanoContaFilter;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class PlanoContaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PlanoContaService.class);	
	
	@Autowired
	private PlanosContaRepository repository;
	@Autowired
	private PlanosContaSecundariaRepository secundariaRepository;
	
	
	@Transactional
	public PlanoConta salvar(PlanoConta planoConta) {
		testeRegistroJaCadastrado(planoConta);
		
		if(!planoConta.isNovo()) {
			List<PlanoContaSecundaria> sec = secundariaRepository.findByPlanoContaId(planoConta.getId());
			List<PlanoContaSecundaria> ss = new ArrayList<>();
			
			for(PlanoContaSecundaria s : sec) {
				s.setTipo(planoConta.getTipo());
				ss.add(s);
			}
			
			secundariaRepository.save(ss);
		}
		
		return repository.save(planoConta);
	}

	public Page<PlanoConta> filtrar(PlanoContaFilter planoContaFilter, Pageable pageable) {
		return repository.filtrar(planoContaFilter, pageable);
	}

	public List<PlanoConta> findAll() {
		return repository.findAll();
	}
	
	public PlanoConta findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "o plano de conta";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(PlanoConta planoConta) {
		Optional<PlanoConta> optional = repository.findByNomeIgnoreCase(planoConta.getNome());
		if(optional.isPresent() && !optional.get().equals(planoConta)) {
			throw new RegistroJaCadastradoException("Já existe um plano de conta com esse nome cadastrado!");
		}
	}

	public List<PlanoConta> findAllOrderByNome() {
		return repository.findAllOrderByNome();
	}

	public List<PlanoConta> listarTodosPlanosContaDebito() {
		return repository.listarTodosPlanosContaDebito();
	}

	public List<PlanoConta> listarTodosPlanosContaCredito() {
		return repository.listarTodosPlanosContaCredito();
	}

	public List<PlanoConta> findByPlanoContaIdOrderByNome(Long idPlanoConta) {
		return repository.findByPlanoContaIdOrderByNome(idPlanoConta);
	}
}
