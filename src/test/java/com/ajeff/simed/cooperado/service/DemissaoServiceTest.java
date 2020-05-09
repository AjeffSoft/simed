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
import com.ajeff.simed.cooperado.model.DemissaoCooperado;
import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.DemissoesRepository;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.DocumentoPF;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;
import com.ajeff.simed.geral.service.exception.DataAtualPosteriorDataReferenciaException;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class DemissaoServiceTest {
	
	@InjectMocks
	private DemissaoService service;
	
	@Mock
	private DemissoesRepository repository;
	@Mock
	private CooperadoService admissaoService;	
	@Mock
	private Cooperado adm;
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
		
		docPF = Mockito.mock(DocumentoPF.class);
		docPF.setCpf("97996599300");
		
		coop = new Medico();
		coop.setAtivo(false);
		coop.setCrm("1234");
		coop.setDocumento(docPF);
		coop.setEndereco(end);
		coop.setNome("ANTONIO FREITAS");
		
		adm = new Cooperado();
		adm.setCooperado(coop);
		adm.setRegistro("001");
		adm.setCodigo("131001");
		adm.setAtivo(true);
		adm.setData(LocalDate.of(2020, 04, 05));		
	}
	
	
	
	@Test
	@DisplayName("Deve salvar uma demissao válida")
	public void deveSalvarDemissaoValida() {
		DemissaoCooperado dem = novaDemissao();
		DemissaoCooperado demSalva = novaDemissao();
		demSalva.setId(1L);
		Mockito.when(repository.save(dem)).thenReturn(demSalva);
		
		service.salvar(dem);
		
		Assertions.assertThat(demSalva.getId()).isNotNull();
		Assertions.assertThat(demSalva.getMotivo()).isEqualTo(dem.getMotivo());
		Assertions.assertThat(demSalva.getAdmissao().getCooperado().getAtivo()).isFalse();
		Mockito.verify(repository, Mockito.times(1)).save(dem);
	}
	
	
	@Test
	@DisplayName("Deve dar erro caso a data da demissão seja anterior a data da admissão")
	public void deveDarErroQuandoDataDemissaoMenorDataAdmissao() {
		DemissaoCooperado dem = novaDemissao();
		dem.setData(LocalDate.of(2020, 4, 1));
		DemissaoCooperado demSalva = novaDemissao();
		demSalva.setId(1L);
		Mockito.when(repository.save(dem)).thenReturn(demSalva);
		
		Throwable exception = Assertions.catchThrowable(() -> service.salvar(dem));
		Assertions.assertThat(exception)
			.isInstanceOf(DataAtualPosteriorDataReferenciaException.class)
			.hasMessage("A data de demissão deve ser anterior a data de admissão");
		
		Mockito.verify(repository, Mockito.never()).save(dem);
	}	
	

	private DemissaoCooperado novaDemissao() {
		DemissaoCooperado dem = new DemissaoCooperado();
		dem.setAdmissao(adm);
		dem.setMotivo("DESISTIU");
		dem.setData(LocalDate.of(2020, 04, 05));
		return dem;
	}
	
}
