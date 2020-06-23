package com.ajeff.simed.cooperado.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.CancelamentosRepository;
import com.ajeff.simed.cooperado.repository.filter.CancelamentoFilter;

@Service
public class CancelamentoService {

	
	@Autowired
	private CancelamentosRepository repository;
	@Autowired
	private CooperadoService cooperadoService;
	
	@Transactional
	public void descooperar(Cooperado cooperado) {
		cooperado.setAtivo(false);
		cooperadoService.ativarDesativarMedico(cooperado);
//		CalculosComDatas.emissaoMenorIgualVencimento(cooperado.getData(), cooperado.getDataCancelamento());
		repository.save(cooperado);
	}	
	

	public Cooperado findOne(Long id) {
		return repository.findOne(id);
	}
	
	public Page<Cooperado> filtrar(CancelamentoFilter cooperadoFilter, Pageable pageable) {
		return repository.filtrar(cooperadoFilter, pageable);
	}

}
