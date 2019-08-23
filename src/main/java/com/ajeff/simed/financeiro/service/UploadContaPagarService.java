package com.ajeff.simed.financeiro.service;

import java.util.List;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.model.UploadContaPagar;
import com.ajeff.simed.financeiro.repository.UploadsContasPagarRepository;
import com.ajeff.simed.financeiro.storage.UploadStorage;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;

@Service
public class UploadContaPagarService {

	@Autowired
	private UploadsContasPagarRepository repository;
//	@Autowired
//	private ApplicationEventPublisher publisher;
	@Autowired
	private UploadStorage storage;
	

	@Transactional
	public UploadContaPagar salvar(UploadContaPagar uploadContaPagar){
		return repository.save(uploadContaPagar);
	}


	public List<UploadContaPagar> findByContaPagarId(Long idContaPagar) {
		return repository.findByContaPagarId(idContaPagar);
	}
	
//	public List<UploadContaPagar> buscarTodosAnexosDaConta(Long id) {
//		return repository.buscarTodosAnexosDaConta(id);
//	}
//
//	public List<UploadContaPagar> buscarTodosAnexosDaContaReceber(Long id) {
//		return repository.buscarTodosAnexosDaContaReceber(id);
//	}
	
	@Transactional
	public void excluir(UploadContaPagar uploadPagar) {
		String tipo = "o arquivo";

		try {
			String upload = uploadPagar.getUpload();
			repository.delete(uploadPagar);
			repository.flush();
			storage.excluirArquivo(upload);
		} catch (PersistenceException e) {
	throw new ImpossivelExcluirEntidade("Não foi possivel excluir " + tipo +". Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!"); 
		}
	}

	
//	private void testeRegistroJaCadastrado(UploadContaPagar uploadContaPagar) {
//		Optional<UploadContaPagar> optional = repository.findByTipoAndContaPagar(uploadContaPagar.getTipo(), uploadContaPagar.getContaPagar());
//		
//		if(optional.isPresent() && !optional.get().equals(uploadContaPagar)){
//			throw new TipoEContaPagarJaCadastradoException("Já existem um anexo com esse tipo cadastrado para esta conta!");
//		}
//	}

	public UploadContaPagar findOne(Long id) {
		return repository.findOne(id);
	}

}
