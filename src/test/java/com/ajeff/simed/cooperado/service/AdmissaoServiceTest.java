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
import com.ajeff.simed.cooperado.model.AdmissaoCooperado;
import com.ajeff.simed.cooperado.model.Cooperado;
import com.ajeff.simed.cooperado.repository.AdmissoesRepository;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.DocumentoPF;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class AdmissaoServiceTest {
	
	@InjectMocks
	private AdmissaoService service;
	@Mock
	private CooperadoService cooperadoService;
	
	@Mock
	private AdmissoesRepository repository;
	@Mock
	private Cidade c;
	@Mock
	private Estado es;
	@Mock
	private Endereco end;	
	@Mock
	private DocumentoPF docPF;	
	@Mock
	private Cooperado coop;		
	
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
		
		coop = new Cooperado();
		coop.setAtivo(false);
		coop.setCrm("1234");
		coop.setDocumento(docPF);
		coop.setEndereco(end);
		coop.setNome("ANTONIO FREITAS");		
	}
	
	
	
	@Test
	@DisplayName("Deve salvar uma admissão válida")
	public void deveSalvarAdmissaoValida() {
		AdmissaoCooperado adm = novaAdmissao();
		AdmissaoCooperado admSalva = novaAdmissao();
		admSalva.setId(1L);
		Mockito.when(cooperadoService.ativarCooperado(coop)).thenReturn(true);
		Mockito.when(repository.save(adm)).thenReturn(admSalva);
		
		service.salvar(adm);
		
		Assertions.assertThat(admSalva.getId()).isNotNull();
		Assertions.assertThat(admSalva.getRegistro()).isEqualTo(adm.getRegistro());
		Assertions.assertThat(admSalva.getAtivo()).isTrue();
		Mockito.verify(repository, Mockito.times(1)).save(adm);
	}
	

	private AdmissaoCooperado novaAdmissao() {
		AdmissaoCooperado adm = new AdmissaoCooperado();
		adm.setCooperado(coop);
		adm.setRegistro("001");
		adm.setCodigo("131001");
		adm.setAtivo(true);
		adm.setData(LocalDate.of(2020, 04, 05));
		return adm;
	}
	
}
