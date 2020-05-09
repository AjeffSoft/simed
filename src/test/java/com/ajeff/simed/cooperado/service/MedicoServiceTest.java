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
import com.ajeff.simed.cooperado.model.Medico;
import com.ajeff.simed.cooperado.repository.MedicosRepository;
import com.ajeff.simed.geral.model.Cidade;
import com.ajeff.simed.geral.model.Endereco;
import com.ajeff.simed.geral.model.Estado;
import com.ajeff.simed.geral.service.PessoaService;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@ContextConfiguration(classes = {AppInitializer.class}) 
public class MedicoServiceTest {
	
	@InjectMocks
	private MedicoService service;
	
	@Mock
	private MedicosRepository repository;
	@Mock
	private PessoaService pessoaService;
	@Mock
	private Cidade c;
	@Mock
	private Estado es;
	@Mock
	private Endereco end;	
	
	
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
	}
	
	
	
	@Test
	@DisplayName("Deve salvar um cooperado válido")
	public void deveSalvarCooperadoValido() {
		Medico coop = novoCooperado();
		Medico coopSalvo = novoCooperado();
		coopSalvo.setId(1L);
		
		Mockito.when(repository.save(coop)).thenReturn(coopSalvo);
		Mockito.doNothing().when(pessoaService).testeRegistroJaCadastrado(coop);
		
		service.salvar(coop);
		
		Assertions.assertThat(coopSalvo.getId()).isNotNull();
		Assertions.assertThat(coopSalvo.getNome()).isEqualTo(coop.getNome());
		Assertions.assertThat(coopSalvo.getAtivo()).isFalse();
		Mockito.verify(repository, Mockito.times(1)).save(coop);
	}
	
	private Medico novoCooperado() {
		Medico coop = new Medico();
		coop.setAtivo(false);
		coop.setSigla("1234");
		coop.setDocumento1("467.984.440-08");
		coop.setEndereco(end);
		coop.setNome("ANTONIO FREITAS");
		return coop;
	}
	
}
