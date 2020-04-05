package com.ajeff.simed.cooperado.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ajeff.simed.config.init.AppInitializer;
import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.CooperadosRepository;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.DocumentoPF;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class CooperadoServiceTest {
	
	@InjectMocks
	private CooperadoService service;
	
	@Mock
	private CooperadosRepository repository;
	@Mock
	private Cooperado cooperado;
	@Mock
	private Cidade c;
	@Mock
	private Estado es;
	@Mock
	private Endereco end;	
	@Mock
	private DocumentoPF docPF;	
	
	
	@BeforeAll
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		
		es = Mockito.mock(Estado.class);
		es.setNome("ESTADO TESTE");
		es.setUf("TE");
		
		c = Mockito.mock(Cidade.class);
		c.setNome("CIDADE TESTE");
		c.setUf("TE");
		c.setCapital(true);
		c.setEstado(es);
		
		end = Mockito.mock(Endereco.class);
		end.setBairro("BAIRRO TESTE");
		end.setCep("63500000");
		end.setCidade(c);
		
		docPF = Mockito.mock(DocumentoPF.class);
		docPF.setCpf("97996599300");

	}
	
	
	
	@Test
	@DisplayName("Deve salvar um cooperado válido")
	public void deveSalvarCooperadoValido() {
		Cooperado coop = novoCooperado();
		Cooperado coopSalvo = novoCooperado();
		coopSalvo.setId(1L);
		
		Mockito.when(repository.save(coop)).thenReturn(coopSalvo);
		
		service.salvar(coop);
		
		Assertions.assertThat(coopSalvo.getId()).isNotNull();
		Assertions.assertThat(coopSalvo.getNome()).isEqualTo(coop.getNome());
		Assertions.assertThat(coopSalvo.getAtivo()).isFalse();
		Mockito.verify(repository, Mockito.times(1)).save(coop);
	}
	
	
	@Test
	@DisplayName("Deve ativar um cooperado desativado")
	public void deveAtivarCooperadoDesativado() {
		Cooperado coopSalvo = novoCooperado();
		coopSalvo.setId(1L);
		coopSalvo.setAtivo(false);
		
		service.ativarCooperado(coopSalvo);
		
		Assertions.assertThat(coopSalvo.getAtivo()).isTrue();
	}
	
	
	@Test
	@DisplayName("Deve desativar um cooperado ativado")
	public void deveDesativarCooperadoAtivado() {
		Cooperado coopSalvo = novoCooperado();
		coopSalvo.setId(1L);
		coopSalvo.setAtivo(true);
		
		service.desativarCooperado(coopSalvo);
		
		Assertions.assertThat(coopSalvo.getAtivo()).isFalse();
	}	
	

	private Cooperado novoCooperado() {
		Cooperado coop = new Cooperado();
		coop.setAtivo(false);
		coop.setCrm("1234");
		coop.setDocumento(docPF);
		coop.setEndereco(end);
		coop.setNome("ANTONIO FREITAS");
		return coop;
	}
	
}
