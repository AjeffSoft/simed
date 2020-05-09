package com.ajeff.simed.geral.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ajeff.simed.financeiro.service.exception.CpfCnpjInvalidoException;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.geral.model.Pessoa;
import com.ajeff.simed.geral.repository.PessoasRepository;
import com.ajeff.simed.geral.service.exception.ImpossivelExcluirEntidade;
import com.ajeff.simed.util.CpfCnpjUtils;

@Service
public class PessoaService {
	
	@Autowired
	private PessoasRepository repository;

	public void testeRegistroJaCadastrado(Pessoa pessoa) {
		verificaDocumento1JaCadastrado(pessoa);
		verificaNomeFantasiaSiglaJaCadastrado(pessoa);
	}
	

	public static void testeCpfCnpjValido(Pessoa pessoa) {
		if(!pessoa.getDocumento1().equals("")) {
			String docSemMascara = removerMascaraDocumento(pessoa.getDocumento1());

			Boolean valido = CpfCnpjUtils.isValid(docSemMascara);
			if(!valido) {
				throw new CpfCnpjInvalidoException("CNPJ/CPF inválido, favor verifique!");
			}
		}
	}		


	private static String removerMascaraDocumento(String documento1) {
		return documento1.replaceAll("\\D", "");
	}
	
	
	private void verificaDocumento1JaCadastrado(Pessoa pessoa) {
		Optional<Pessoa> optional = repository.findByDocumento1(pessoa.getDocumento1());
		
		if(optional.isPresent() && !optional.get().equals(pessoa)) {
			throw new RegistroJaCadastradoException("CNPJ/CPF já cadastrado!");
		}
	}
	

	private void verificaNomeFantasiaSiglaJaCadastrado(Pessoa pessoa) {
		Optional<Pessoa> optional = repository.findByNomeAndFantasiaAndSiglaIgnoreCase(pessoa.getNome(), pessoa.getFantasia(), pessoa.getSigla());
		
		if(optional.isPresent() && !optional.get().equals(pessoa)) {
			throw new RegistroJaCadastradoException("Nome/Fantasia/Sigla já cadastrado!");
		}
	}

	
	@Transactional
	public void excluir(Long id) {

		try {
			repository.delete(id);
			repository.flush();
		} catch (PersistenceException e) {
			throw new ImpossivelExcluirEntidade("Não foi possivel excluir o registro selecionado!!"); 
		}
	}
	
	
	public Boolean ativarDesativarPessoa(Pessoa pessoa) {
		if(pessoa.getAtivo()) {
			return false;
		}else {
			return true;
		}
	}
	

	public Pessoa buscarComCidadeEstado(Long id) {
		return repository.buscarComCidadeEstado(id);
	}

	public List<Pessoa> findByNomeOrderByNome() {
		return repository.findAllByOrderByNome();
	}

	public List<Pessoa> findByAtivoFalseOrderByNome() {
		return repository.findByAtivoFalseOrderByNome();
	}

	public List<Pessoa> findByAtivoTrueOrderByNome() {
		return repository.findByAtivoTrueOrderByNome();
	}

	public Pessoa findOne(Long id) {
		return repository.findOne(id);
	}	

}
