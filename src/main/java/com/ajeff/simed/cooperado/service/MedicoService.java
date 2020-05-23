package com.ajeff.simed.cooperado.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.MedicosRepository;
import com.ajeff.simed.cooperado.repository.filter.MedicoFilter;
import com.ajeff.simed.geral.service.PessoaService;

@Service
public class MedicoService {

	
	@Autowired
	private MedicosRepository repository;
	@Autowired
	private PessoaService pessoaService;	
	
	@Transactional
	public void salvar(Medico medico) {
		pessoaService.testeRegistroJaCadastrado(medico);
		PessoaService.testeCpfCnpjValido(medico);
		if (medico.isNovo()) {
			medico.setAtivo(false);
		}
		repository.save(medico);
	}
	
	
	@Transactional
	public void ativarDesativarCooperado(Medico medico) {
		medico.setAtivo(pessoaService.ativarDesativarPessoa(medico));
		repository.save(medico);
	}
	
	
	public Page<Medico> filtrar(MedicoFilter medicoFilter, Pageable pageable) {
		return repository.filtrar(medicoFilter, pageable);
	}


	public List<Medico> findByAtivoFalseOrderByNome() {
		return repository.findByAtivoFalseOrderByNome();
	}


}
