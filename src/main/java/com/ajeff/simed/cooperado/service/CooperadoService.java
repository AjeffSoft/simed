package com.ajeff.simed.cooperado.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.CooperadosRepository;

@Service
public class CooperadoService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(CooperadoService.class);	
	
	@Autowired
	private CooperadosRepository repository;
	
	
	@Transactional
	public void salvar(Cooperado cooperado) {
//		testeRegistroJaCadastrado(fornecedor);		
		repository.save(cooperado);
	}


//	public Page<Fornecedor> filtrar(FornecedorFilter fornecedorFilter, Pageable pageable) {
//		return repository.filtrar(fornecedorFilter, pageable);
//	}
	
//	@Transactional
//	public void excluir(Long id) {
//		String tipo = "o fornecedor";
//
//		try {
//			repository.delete(id);
//			repository.flush();
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
//		}
//		
//	}

	
	public Cooperado findOne(Long id) {
		return repository.findOne(id);
	}		

//	private void testeRegistroJaCadastrado(Fornecedor fornecedor) {
//		Optional<Fornecedor> optional = repository.findByNomeOrSiglaIgnoreCase(fornecedor.getNome(), fornecedor.getSigla());
//		
//		if(optional.isPresent() && !optional.get().equals(fornecedor)) {
//			throw new RegistroJaCadastradoException("Nome e/ou sigla já cadastrado!");
//		}
//	}


//	public List<Fornecedor> findByNomeContainingIgnoreCase(String nome) {
//		return repository.findByNomeContainingIgnoreCase(nome);
//	}


//	public Fornecedor buscarComCidadeEstado(Long id) {
//		return repository.buscarComCidadeEstado(id);
//	}


//	public List<Fornecedor> listarTodosFornecedores() {
//		return repository.listarTodosFornecedores();
//	}

}
