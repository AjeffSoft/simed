package com.ajeff.simed.geral.service;

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

import com.ajeff.simed.geral.controller.EmpresaController;
import com.ajeff.simed.geral.model.Empresa;
import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.EmpresasRepository;
import com.ajeff.simed.geral.repository.UsuariosRepository;
import com.ajeff.simed.geral.repository.filter.EmpresaFilter;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.geral.service.exception.RegistroJaCadastradoException;

@Service
public class EmpresaService {
	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(EmpresaController.class);	

	@Autowired
	private EmpresasRepository repository;
	@Autowired
	private UsuariosRepository usuariosRepository;
	

	@Transactional
	public Empresa salvar(Empresa empresa) {
		
		testeRegistroJaCadastrado(empresa);
		
		if(empresa.isNovo()) {
			empresa.setSituacao(true);
		}
		
		return repository.save(empresa);
	}

	public Page<Empresa> filtrar(EmpresaFilter empresaFilter, Pageable pageable) {
		return repository.filtrar(empresaFilter, pageable);
	}

	public Empresa findOne(Long id) {
		return repository.findOne(id);
	}
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir a empresa. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}	

	private void testeRegistroJaCadastrado(Empresa empresa) {
		Optional<Empresa> optional = repository.findByNomeAndFantasiaOrCnpjOrSiglaIgnoreCase(empresa.getNome(), empresa.getFantasia(), empresa.getCnpj(), empresa.getSigla());
		if(optional.isPresent() && !optional.get().equals(empresa)) {
			throw new RegistroJaCadastradoException("Já existe uma empresa com esse nome/fantasia/sigla ou CNPJ cadastrada!");
		}
	}

	public List<Empresa> buscarTodos() {
		return repository.buscarTodasEmpresasOrdenadasPorNomeAsc();
	}

	public Empresa buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}
	
	public Long incrementarCodigoPagamento(Long empresa) {
		Empresa emp = repository.findOne(empresa);
		Long inc = emp.getCodigoPagamento() + 1;
		emp.setCodigoPagamento(inc);
		repository.save(emp);
		return inc;
	}	
	
	public List<Empresa> buscarEmpresaPorUsuario(Long id) {
		Usuario user = usuariosRepository.buscarEmpresaPorUsuario(id);
		List<Empresa> emps = new ArrayList<Empresa>();
		for (Empresa e: user.getEmpresas()) {
			emps.add(e);
		}
		return emps;
	}		
	


}
