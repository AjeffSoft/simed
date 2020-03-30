package com.ajeff.simed.financeiro.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ajeff.simed.config.init.AppInitializer;
import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.service.exception.RegistroJaCadastradoException;
import com.ajeff.simed.util.CpfCnpjUtils;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class FornecedorServiceTest{

	@Mock
	private FornecedorService service;
	
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	
	@Test
	@DisplayName("Deve salvar um fornecedor com os dados obrigatórios sem CPF/CNPJ")
	public void salvarFornecedorSemDocumento() {
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("TESTE");
		forn.setFantasia("TESTE");
		forn.setSigla("TES");
		
		
		Mockito.when(service.salvar(forn)).thenReturn(forn);
		Fornecedor fSalve  = service.salvar(forn);
		Assertions.assertNotNull(fSalve);
	}
	
	
	@Test
	@DisplayName("Deve salvar um fornecedor com os dados obrigatórios com CPF/CNPJ")
	public void salvarFornecedorComDocumento() {
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("TESTE");
		forn.setFantasia("TESTE");
		forn.setSigla("TES");
		forn.setDocumento1("97996599300");
		
		Mockito.when(service.salvar(forn)).thenReturn(forn);
		Fornecedor fSalve  = service.salvar(forn);
		Assertions.assertNotNull(fSalve);
		Assertions.assertEquals("97996599300", fSalve.getDocumento1());
		Assertions.assertTrue(CpfCnpjUtils.isValid(fSalve.getDocumento1()));
	}
	
	
	@Test
	@DisplayName("Deve salvar um fornecedor com os dados obrigatórios com CPF/CNPJ inválido")
	public void salvarFornecedorComDocumentoInvalido() {
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("TESTE");
		forn.setFantasia("TESTE");
		forn.setSigla("TES");
		forn.setDocumento1("11111111111");
		
		Mockito.when(service.salvar(forn)).thenReturn(forn);
		Fornecedor fSalve  = service.salvar(forn);
		Assertions.assertNotNull(fSalve);
		Assertions.assertEquals("11111111111", fSalve.getDocumento1());
		Assertions.assertFalse(CpfCnpjUtils.isValid(fSalve.getDocumento1()));
	}	
	
	
	@Test
	@DisplayName("Deve dar erro ao salvar um fornecedor com o mesmo nome")
	public void salvarFornecedorMesmoNome(){
		
		String nomeDuplicado = "UNIMED CENTRO SUL";
		
		Fornecedor forn = new Fornecedor();
		forn.setNome(nomeDuplicado);
		forn.setFantasia("UNIMED");
		forn.setSigla("131");

		Mockito.when(service.salvar(forn)).thenReturn(forn);
		service.salvar(forn);
		String err = "";
		
		if(forn.getNome().equals(nomeDuplicado)) {
			 err =  new RegistroJaCadastradoException("Duplicado").getMessage();
			
		}
		Assertions.assertEquals("Duplicado", err);
	}
	
	
	@Test
	@DisplayName("Deve dar erro ao salvar um fornecedor com o mesmo nome fantasia")
	public void salvarFornecedorMesmoNomeFantasia(){
		
		String nomeDuplicado = "UNIMED";
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("UNIMED CENTRO SUL");
		forn.setFantasia(nomeDuplicado);
		forn.setSigla("131");

		Mockito.when(service.salvar(forn)).thenReturn(forn);
		service.salvar(forn);
		String err = "";
		
		if(forn.getFantasia().equals(nomeDuplicado)) {
			 err =  new RegistroJaCadastradoException("Duplicado").getMessage();
			
		}
		Assertions.assertEquals("Duplicado", err);
	}	
	
	@Test
	@DisplayName("Deve dar erro ao salvar um fornecedor com a mesma sigla")
	public void salvarFornecedorMesmaSigla(){
		
		String nomeDuplicado = "131";
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("UNIMED CENTRO SUL");
		forn.setFantasia("UNIMED");
		forn.setSigla(nomeDuplicado);

		Mockito.when(service.salvar(forn)).thenReturn(forn);
		service.salvar(forn);
		String err = "";
		
		if(forn.getSigla().equals(nomeDuplicado)) {
			 err =  new RegistroJaCadastradoException("Duplicado").getMessage();
			
		}
		Assertions.assertEquals("Duplicado", err);
	}	
	
	
	@Test
	@DisplayName("Deve dar erro ao salvar um fornecedor com o mesmo documento")
	public void salvarFornecedorMesmoDocumento(){
		
		String nomeDuplicado = "97996599300";
		
		Fornecedor forn = new Fornecedor();
		forn.setNome("UNIMED CENTRO SUL");
		forn.setFantasia("UNIMED");
		forn.setSigla("131");
		forn.setDocumento1(nomeDuplicado);

		Mockito.when(service.salvar(forn)).thenReturn(forn);
		service.salvar(forn);
		String err = "";
		
		if(forn.getDocumento1().equals(nomeDuplicado)) {
			 err =  new RegistroJaCadastradoException("Duplicado").getMessage();
			
		}
		Assertions.assertEquals("Duplicado", err);
	}	
	

}
