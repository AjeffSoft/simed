package com.ajeff.simed.financeiro.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.PlanoContaSecundaria;
import com.ajeff.simed.financeiro.repository.PlanosContaSecundariaRepository;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class PlanoContaSecundariaService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(PlanoContaSecundariaService.class);	
	
	@Autowired
	private PlanosContaSecundariaRepository repository;
//	@Autowired
//	private PlanosContaRepository repositoryPlanoConta;
	
	
	@Transactional
	public PlanoContaSecundaria salvar(PlanoContaSecundaria planoContaSecundaria) {
		
//		//SETAR O TIPO (CREDITO/DEBITO) DA SUBCONTA DIRETAMENTE PELO PLANO DE CONTA SELECIONADO
//		PlanoConta planoConta = repositoryPlanoConta.findOne(subPlanoConta.getPlanoConta().getId());
//		subPlanoConta.setTipo(planoConta.getTipo());
//		
//		registroNovo(subPlanoConta);
//		
//		testeRegistroJaCadastrado(subPlanoConta);
		return repository.save(planoContaSecundaria);
	}

//	private void registroNovo(PlanoContaSecundaria subPlanoConta) {
//		if(subPlanoConta.isNovo()) {
//			subPlanoConta.setSituacao(true);
//		}
//	}

//	public Page<PlanoContaSecundaria> filtrar(PlanoContaSecundariaFilter subPlanoContaFilter, Pageable pageable) {
//		return repository.filtrar(subPlanoContaFilter, pageable);
//	}

	public List<PlanoContaSecundaria> findAll() {
		return repository.findAll();
	}
	
	public PlanoContaSecundaria findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		String tipo = "a conta secundária";

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	
	//BUSCAS DE SUBCONTA
	public List<PlanoContaSecundaria> findByPlanoContaId(Long idPlanoConta) {
		return repository.findByPlanoContaId(idPlanoConta);
	}
	public List<PlanoContaSecundaria> listarTodosSubPlanosContaDebito() {
		return repository.listarTodosPlanosContaSecundariaDebito();
	}
	public List<PlanoContaSecundaria> listarTodosSubPlanosContaCredito() {
		return repository.listarTodosPlanosContaSecundariaCredito();
	}


//	private void testeRegistroJaCadastrado(PlanoContaSecundaria subPlanoConta) {
//		Optional<PlanoContaSecundaria> optional = repository.findByNomeIgnoreCaseAndPlanoConta(subPlanoConta.getNome(), subPlanoConta.getPlanoConta());
//		if(optional.isPresent() && !optional.get().equals(subPlanoConta)) {
//			throw new RegistroJaCadastradoException("Já existe uma conta secundária com este nome para este plano de conta cadastrado!");
//		}
//	}
//

}
