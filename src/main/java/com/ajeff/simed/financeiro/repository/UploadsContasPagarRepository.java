package com.ajeff.simed.financeiro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ajeff.simed.financeiro.model.UploadContaPagar;

public interface UploadsContasPagarRepository extends JpaRepository<UploadContaPagar, Long>{

	List<UploadContaPagar> findByContaPagarId(Long idContaPagar);

//	List<UploadContaPagar> findByContaReceberId(Long idContaReceber);
//
//	Optional<UploadContaPagar> findByTipoAndContaPagar(TipoUploadPagar tipo, ContaPagar contaPagar);
//
//	Optional<UploadContaPagar> findByUploadIgnoreCase(String upload);
//
//	@Query("select u from UploadContaPagar u where u.contaPagar.id = ?1")
//	List<UploadContaPagar> buscarTodosAnexosDaConta(Long id);
//
//	@Query("select u from UploadContaPagar u where u.contaReceber.id = ?1")
//	List<UploadContaPagar> buscarTodosAnexosDaContaReceber(Long id);
	

}
