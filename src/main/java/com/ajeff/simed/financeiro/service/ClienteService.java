package com.ajeff.simed.financeiro.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.ClientesRepository;
import com.ajeff.simed.financeiro.repository.filter.FornecedorFilter;
import com.ajeff.simed.financeiro.service.exception.CpfCnpjInvalidoException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.CpfCnpjUtils;

@Service
public class ClienteService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ClienteService.class);	
	
	@Autowired
	private ClientesRepository repository;
	
	
	@Transactional
	public Fornecedor salvar(Fornecedor cliente) {
		try {
			cliente.setClifor(false);
			testeRegistroJaCadastrado(cliente);
			
			if(!cliente.getDocumento1().equals("")) {
				String docSemMascara = removerMascaraDocumento(cliente.getDocumento1());
				testeValidarCpfCnpj(docSemMascara);
			}
			return repository.save(cliente);
		} catch (NonUniqueResultException e) {
			throw new RegistroJaCadastradoException("Mais de um registro com o mesmo documento para validação!");
		}
	}
	
	
	public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
		return repository.filtrar(fornecedorFilter, pageable);
	}
	
	
	@Transactional
	public void excluir(Long id) {
		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o cliente. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
		
	}

	
	public Fornecedor findOne(Long id) {
		return repository.findOne(id);
	}
	
	
	private void testeRegistroJaCadastrado(Fornecedor cliente) {
		Optional<Fornecedor> optional = repository.findByNomeOrSiglaIgnoreCase(cliente.getNome(), cliente.getSigla());
		Optional<Fornecedor> documento = repository.findByDocumento1(cliente.getDocumento1());
		
		if(optional.isPresent() && !optional.get().equals(cliente)) {
			throw new RegistroJaCadastradoException("Nome e/ou sigla já cadastrado!");
		}
		
		if(documento.isPresent() && !documento.get().equals(cliente) && !documento.get().getDocumento1().equals("")) {
			throw new RegistroJaCadastradoException("CPF/CNPJ já cadastrado!");
		}			
	}	

	

	public Fornecedor buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}


	public List<Fornecedor> findByNomeClienteContainingIgnoreCase(String nome) {
		return repository.findByNomeClienteContainingIgnoreCase(nome);
	}


	public List<Fornecedor> listarTodosClientes() {
		return repository.listarTodosClientes();
	}
	
	private String removerMascaraDocumento(String documento1) {
		return documento1.replaceAll("\\D", "");
	}


	private void testeValidarCpfCnpj(String documento1) {
		Boolean valido = CpfCnpjUtils.isValid(documento1);
		if(!valido) {
			throw new CpfCnpjInvalidoException("CNPJ/CPF inválido, favor verifique!");
		}
	}	
}
