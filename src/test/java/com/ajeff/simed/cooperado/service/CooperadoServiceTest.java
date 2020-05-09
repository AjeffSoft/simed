package com.ajeff.simed.cooperado.service;

import java.time.LocalDate;

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
import com.ajeff.simed.cooperado.model.Medico;
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
	private MedicoService medicoService;
	
	@Mock
	private CooperadosRepository repository;
	@Mock
	private Cidade c;
	@Mock
	private Estado es;
	@Mock
	private Endereco end;	
	@Mock
	private Medico coop;		
	
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
		
	
		coop = new Medico();
		coop.setAtivo(false);
		coop.setSigla("1234");
		coop.setDocumento1("97996599300");
		coop.setEndereco(end);
		coop.setNome("ANTONIO FREITAS");		
	}
	
	
	
	@Test
	@DisplayName("Deve salvar uma admissão válida")
	public void deveSalvarAdmissaoValida() {
		Cooperado adm = novaAdmissao();
		Cooperado admSalva = novaAdmissao();
		admSalva.setId(1L);
		Mockito.doNothing().when(medicoService).ativarDesativarCooperado(coop);
		Mockito.when(repository.save(adm)).thenReturn(admSalva);
		
		service.salvar(adm);
		
		Assertions.assertThat(admSalva.getId()).isNotNull();
		Assertions.assertThat(admSalva.getRegistro()).isEqualTo(adm.getRegistro());
		Assertions.assertThat(admSalva.getAtivo()).isTrue();
		Mockito.verify(repository, Mockito.times(1)).save(adm);
	}
	
	
//	@Test
//	@DisplayName("Deve ativar uma admissao e o cooperado")
//	public void deveDesativarAdmissaoValida() {
//		Cooperado adm = novaAdmissao();
//		adm.setId(1L);
//		adm.setAtivo(false);
//		coop.setAtivo(false);
//		coop.setId(1l);
////		Mockito.when(cooperadoService.ativarCooperado(coop)).thenReturn(true);
//		
//		service.ativarAdmissao(adm);
//		
//		Assertions.assertThat(adm.getId()).isNotNull();
//		Assertions.assertThat(adm.getAtivo()).isTrue();
//		Assertions.assertThat(coop.getAtivo()).isTrue();
//		Mockito.verify(repository, Mockito.times(1)).save(adm);
//	}	
//	

	private Cooperado novaAdmissao() {
		Cooperado adm = new Cooperado();
		adm.setMedico(coop);
		adm.setRegistro("001");
		adm.setCodigo("131001");
		adm.setAtivo(true);
		adm.setData(LocalDate.of(2020, 04, 05));
		return adm;
	}
	
}
