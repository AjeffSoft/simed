package com.ajeff.simed.util.impostos;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ajeff.simed.exceptions.ValorInformadoNegativoException;

public class CalculosImpostosTest {
	
	@BeforeEach
	public void setUp() {
	}

	//====================== TESTES PARA CALCULOS DE ISS =============================
	@Test
	public void testCalculoISS() {
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(1000), new BigDecimal(5));
		assertEquals(new BigDecimal(50).setScale(2, RoundingMode.HALF_UP), iss);
	}
	
	@Test
	public void testCalculoISSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(-1000), new BigDecimal(5));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSTaxaNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(1000), new BigDecimal(-5));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSAmbosValorTaxaNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(-1000), new BigDecimal(5));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("Um dos valores informados é inválido", e.getMessage());
		}
	}
	
	@Test
	public void testCalculoISSValorZerado(){
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(0), new BigDecimal(5));
		assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), iss);
	}
	
	@Test
	public void testCalculoISSTaxaZerado(){
		BigDecimal iss = CalculosImpostos.calculoISS(new BigDecimal(1000), new BigDecimal(0));
		assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), iss);
	}
	
	

	//====================== TESTES PARA CALCULOS DE INSS =============================
	
	@Test
	public void testCalculoINSS() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(1523.44));
		assertEquals(new BigDecimal(167.58).setScale(2, RoundingMode.HALF_UP), inss);
	}
	
	@Test
	public void testCalculoINSSValorAcimaTetoMaximo() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(5839.50));
		assertEquals(BigDecimal.ZERO, inss);
	}	
	
	@Test
	public void testCalculoINSSValorIgualTetoMaximo() {
		BigDecimal inss = CalculosImpostos.calculoINSS(new BigDecimal(5839.45));
		assertEquals(new BigDecimal(642.34).setScale(2, RoundingMode.HALF_UP), inss);
	}
	
	@Test
	public void testCalculoINSSValorZerado() {
		BigDecimal inss = CalculosImpostos.calculoINSS(BigDecimal.ZERO);
		assertEquals(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), inss);
	}		
	
	@Test
	public void testCalculoINSSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal iss = CalculosImpostos.calculoINSS(new BigDecimal(-1000));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("O valor principal esta negativo", e.getMessage());
		}
	}
	
	
	//====================== TESTES PARA CALCULOS DE PIS/COFINS/CSLL =============================
	
	@Test
	public void testCalculoPCCS() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(1000));
		assertEquals(new BigDecimal(46.50).setScale(2, RoundingMode.HALF_UP), pccs);
	}
	
	@Test
	public void testCalculoPCCSMenorPisoMinimo() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(214));
		assertEquals(BigDecimal.ZERO, pccs);
	}
	
	@Test
	public void testCalculoPCCSIgualPisoMinimo() {
		BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(215.1));
		assertEquals(new BigDecimal(10.00).setScale(2, RoundingMode.HALF_UP), pccs);
	}
	
	@Test
	public void testCalculoPCCSValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal pccs = CalculosImpostos.calculoPCCS(new BigDecimal(-1000));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("O valor principal esta negativo", e.getMessage());
		}
	}
	
	
	//====================== TESTES PARA CALCULOS DE IRPJ =============================
	
	@Test
	public void testCalculoIRPJ() {
		BigDecimal irpj = CalculosImpostos.calculoIRJuridico(new BigDecimal(1000));
		assertEquals(new BigDecimal(15).setScale(2, RoundingMode.HALF_UP), irpj);
	}
	
	@Test
	public void testCalculoIRPJMenorPisoMinimo() {
		BigDecimal irpj = CalculosImpostos.calculoIRJuridico(new BigDecimal(214));
		assertEquals(BigDecimal.ZERO, irpj);
	}
	
	@Test
	public void testCalculoIRPJIgualPisoMinimo() {
		BigDecimal pccs = CalculosImpostos.calculoIRJuridico(new BigDecimal(666.67));
		assertEquals(new BigDecimal(10.00).setScale(2, RoundingMode.HALF_UP), pccs);
	}
	
	@Test
	public void testCalculoIRPJValorNegativo(){
		try {
			@SuppressWarnings("unused")
			BigDecimal irpj = CalculosImpostos.calculoIRJuridico(new BigDecimal(-1000));
			fail();
		} catch (ValorInformadoNegativoException e) {
			assertEquals("O valor principal esta negativo", e.getMessage());
		}
	}
	
	//====================== TESTES PARA CALCULOS DE IRPJ =============================
	
	//TESTES PARA A ******FAIXA 1********** DO IMPOSTO DE RENDA
	
	//Teste com valor na faixa 1 sem retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFSemINSSSemDependenteEValorNaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2000), 0, false);
		assertEquals(new BigDecimal(7.20).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor abaixo da faixa 1 sem retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFSemINSSSemDependenteEValorAbaixoDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(1903.9), 0, false);
		assertEquals(BigDecimal.ZERO, irpf);
	}
	
	//Teste com valor na faixa 1 com retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorNaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2500), 0, true);
		assertEquals(new BigDecimal(24.07).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor abaixo da faixa 1 com retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorAbaixoDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(1903.9), 0, true);
		assertEquals(BigDecimal.ZERO, irpf);
	}
	
	//Teste com valor acima da faixa 1 e devido a retenção de INSS e sem descontos de dependentes foi para a faixa 1
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorAcimaDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2900), 0, true);
		assertEquals(new BigDecimal(50.77).setScale(2, RoundingMode.HALF_UP), irpf);
	}	
	
	//Teste com valor na faixa 1 sem retenção de INSS e com UM desconto de dependente
	@Test
	public void testCalculoIRPFSemINSSUmDependenteEValorNaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2500), 1, false);
		assertEquals(new BigDecimal(30.49).setScale(2, RoundingMode.HALF_UP), irpf);
	}

	//Teste com valor na faixa 1 sem retenção de INSS e com TRES descontos de dependentes e saindo da faixa 1
	@Test
	public void testCalculoIRPFSemINSSTresDependenteEValorNaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2400), 3, false);
		assertEquals(BigDecimal.ZERO, irpf);
	}
	
	//Teste com valor acima da faixa 1 e sem retenção de INSS e devido aos descontos de DOIS dependentes foi para a faixa 1
	@Test
	public void testCalculoIRPFSemINSSDoisDependenteEValorAcimaDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3000), 2, false);
		assertEquals(new BigDecimal(53.77).setScale(2, RoundingMode.HALF_UP), irpf);
	}	
	
	//Teste com valor na faixa 1 com retenção de INSS e com UM desconto de dependente
	@Test
	public void testCalculoIRPFComINSSUmDependenteEValorNaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2500), 1, true);
		assertEquals(new BigDecimal(9.86).setScale(2, RoundingMode.HALF_UP), irpf);
	}

	//Teste com valor na faixa 1 com retenção de INSS e com UM desconto de dependente e saindo da faixa 1
	@Test
	public void testCalculoIRPFComINSSUmDependenteESaindoDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(2200), 1, true);
		assertEquals(BigDecimal.ZERO, irpf);
	}
	
	//Teste com valor acima da faixa 1 e devido a retenção de INSS e descontos de DOIS dependentes foi para a faixa 1
	@Test
	public void testCalculoIRPFComINSSDoisDependenteEValorAcimaDaFaixa1() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3500), 2, true);
		assertEquals(new BigDecimal(62.40).setScale(2, RoundingMode.HALF_UP), irpf);
	}			

	//Teste com valor da retenção negativo
	@Test
	public void testCalculoIRPFNaFaixa1ComValorNegativo() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(1905), 15, false);
		assertEquals(BigDecimal.ZERO, irpf);
	}			
	
	
	//TESTES PARA A ******FAIXA 2******** DO IMPOSTO DE RENDA
	
	//Teste com valor na faixa 2 sem retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFSemINSSSemDependenteEValorNaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3000), 0, false);
		assertEquals(new BigDecimal(95.20).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor na faixa 2 com retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorNaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3500), 0, true);
		assertEquals(new BigDecimal(112.45).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor acima da faixa 2 e devido a retenção de INSS e sem descontos de dependentes foi para a faixa 2
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorAcimaDaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(4000), 0, true);
		assertEquals(new BigDecimal(179.20).setScale(2, RoundingMode.HALF_UP), irpf);
	}	
	
	//Teste com valor na faixa 2 sem retenção de INSS e com UM desconto de dependente
	@Test
	public void testCalculoIRPFSemINSSUmDependenteEValorNaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3500), 1, false);
		assertEquals(new BigDecimal(141.77).setScale(2, RoundingMode.HALF_UP), irpf);
	}

	//Teste com valor na faixa 2 sem retenção de INSS e com TRES descontos de dependentes e saindo da faixa 2 e indo para a faixa 1
	@Test
	public void testCalculoIRPFSemINSSTresDependenteEValorNaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3300), 3, false);
		assertEquals(new BigDecimal(62.06).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor na faixa 2 com retenção de INSS e com UM desconto de dependente
	@Test
	public void testCalculoIRPFComINSSUmDependenteEValorNaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3500), 1, true);
		assertEquals(new BigDecimal(84.02).setScale(2, RoundingMode.HALF_UP), irpf);
	}

	//Teste com valor na faixa 2 com retenção de INSS e com UM desconto de dependente e saindo da faixa 2 e indo para a faixa 1
	@Test
	public void testCalculoIRPFComINSSUmDependenteESaindoDaFaixa2() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(3200), 1, true);
		assertEquals(new BigDecimal(56.59).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor da retenção negativo
	@Test
	public void testCalculoIRPFNaFaixa2ComValorNegativo() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(1905), 20, false);
		assertEquals(BigDecimal.ZERO, irpf);
	}	
	
	
	//TESTES PARA A ******FAIXA 3******** DO IMPOSTO DE RENDA
	
	//Teste com valor na faixa 3 sem retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFSemINSSSemDependenteEValorNaFaixa3() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(4500), 0, false);
		assertEquals(new BigDecimal(376.37).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//Teste com valor na faixa 3 com retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFComINSSSemDependenteEValorNaFaixa3() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(4500), 0, true);
		assertEquals(new BigDecimal(265).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
	//TESTES PARA A ******FAIXA 4******** DO IMPOSTO DE RENDA
	
	//Teste com valor na faixa 4 sem retenção de INSS e sem descontos de dependentes
	@Test
	public void testCalculoIRPFSemINSSSemDependenteEValorNaFaixa4() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(5000), 0, false);
		assertEquals(new BigDecimal(505.64).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	

	//Teste com valor na faixa 4 com retenção de INSS e UM desconto de dependente e assim, saindo da faixa 4
	@Test
	public void testCalculoIRPFComINSSUmDependenteEValorNaFaixa4() {
		BigDecimal irpf = CalculosImpostos.calculoIRFisica(new BigDecimal(5000), 1, true);
		assertEquals(new BigDecimal(322.48).setScale(2, RoundingMode.HALF_UP), irpf);
	}
	
}
