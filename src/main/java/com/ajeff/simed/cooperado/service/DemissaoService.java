package com.ajeff.simed.cooperado.service;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.repository.DemissoesRepository;
import com.ajeff.simed.cooperado.repository.filter.DemissaoFilter;
import com.ajeff.simed.geral.service.exception.DataAtualPosteriorDataReferenciaException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class DemissaoService {


	@Autowired
	private DemissoesRepository repository;
	@Autowired
	private CooperadoService admissaoService;
	
	
	@Transactional
	public void salvar(DemissaoCooperado demissao) {
		admissaoService.desativarAdmissao(demissao.getAdmissao());
		testeDataDemissaoMaiorAdmissao(demissao);
		repository.save(demissao);
	}
	
	private void testeDataDemissaoMaiorAdmissao(DemissaoCooperado demissao) {
		if(demissao.getData().isBefore(demissao.getAdmissao().getData())) {
			throw new DataAtualPosteriorDataReferenciaException("A data de demissão deve ser anterior a data de admissão");
		}
	}
	
	public DemissaoCooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<DemissaoCooperado> filtrar(DemissaoFilter demissaoFilter, Pageable pageable) {
		return repository.filtrar(demissaoFilter, pageable);
	}
	
	@Transactional
	public void excluir(Long id) {
		DemissaoCooperado demissao = repository.findOne(id);
		
		try {
			admissaoService.ativarAdmissao(demissao.getAdmissao());
			repository.delete(demissao);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a demissão!"); 
		}
		
	}

}