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
	}
	
	
	
	@Test
	@DisplayName("Deve salvar uma admissão válida")
	public void deveSalvarAdmissaoValida() {
		Cooperado adm = cooperado();
		Cooperado admSalva = cooperado();
		admSalva.setId(1L);
		Mockito.doNothing().when(medicoService).ativarDesativarCooperado(coop);
		Mockito.when(repository.save(adm)).thenReturn(admSalva);
		
		service.salvar(adm);
		
		Assertions.assertThat(admSalva.getId()).isNotNull();
		Assertions.assertThat(admSalva.getRegistro()).isEqualTo(adm.getRegistro());
		Assertions.assertThat(admSalva.getAtivo()).isTrue();
		Mockito.verify(repository, Mockito.times(1)).save(adm);
	}
	
	

	private Cooperado cooperado() {
		Cooperado adm = new Cooperado();
		Medico medico = Mockito.mock(Medico.class);
		adm.setMedico(medico);
		adm.setRegistro("001");
		adm.setCodigo("131001");
		adm.setAtivo(true);
		adm.setData(LocalDate.of(2020, 04, 05));
		return adm;
	}
	
}
