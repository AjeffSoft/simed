package com.ajeff.simed.financeiro.service.imposto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ajeff.simed.financeiro.model.ContaPagar;
import com.ajeff.simed.financeiro.model.Imposto;
import com.ajeff.simed.financeiro.model.TabelaIRPF;
import com.ajeff.simed.financeiro.model.TabelaIRPJ;
import com.ajeff.simed.financeiro.model.enums.StatusContaPagar;
import com.ajeff.simed.financeiro.repository.TabelasIrpfRepository;
import com.ajeff.simed.financeiro.repository.TabelasIrpjRepository;
import com.ajeff.simed.financeiro.service.exception.RegistroNaoCadastradoException;
import com.ajeff.simed.util.DatasUtils;

@Service
public class ImpostoService {
	
	
	@Autowired
	private TabelasIrpjRepository tabelaIRPJRepository;
	@Autowired
	private TabelasIrpfRepository tabelaIRPFRepository;

	
	public Imposto novoImposto(ContaPagar contaPagar, String tipoImposto) {
		Imposto imp = new Imposto();
		imp.setApuracao(DatasUtils.setarParaUltimoDiaMes(contaPagar.getDataEmissao()));
		imp.setJuros(BigDecimal.ZERO);
		imp.setMulta(BigDecimal.ZERO);
		imp.setValorNF(contaPagar.getValor());
		imp.setNumeroNF(contaPagar.getNotaFiscal());
		imp.setStatus(StatusContaPagar.ABERTO);
		imp.setEmissaoNF(contaPagar.getDataEmissao());
		imp.setNome(tipoImposto);
		imp.setHistorico(imp.getNome() + " - " + contaPagar.getFornecedor().getFantasia() + " - " + imp.getNumeroNF());
		imp.setVencimento(setarDataVencimentoPorTipoImposto(imp.getApuracao(), tipoImposto));
		return imp;
	}
	
	
	
	/*
	 * Calculo da aliquota e valor do imposto PIS/COFINS/CSLL retido
	 */
	public BigDecimal valorPCCSRetido(BigDecimal valor, String tipo) {
		return CalculoImpostoPCCS.calculo(valor, aliquotaPCCS(), tipo);
	}
	
	private BigDecimal aliquotaPCCS() {
		BigDecimal aliquota = BigDecimal.ZERO;
		TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1l);
		aliquota = aliquota.add(tabela.getAliquotaCOFINS().add(tabela.getAliquotaCSLL().add(tabela.getAliquotaPIS())));
		return aliquota; 
	}
	
	
	/*
	 * Calculo do valor do imposto ISS retido
	 */
	public BigDecimal valorISSRetido(BigDecimal valor, BigDecimal issPorcentagem) {
		return CalculoImpostoISS.calculo(valor, issPorcentagem);
	}
	

	
	/*
	 * Calculo do valor do imposto INSS retido - 11%
	 */
	public BigDecimal valorINSSRetido(BigDecimal valor, String tipo) {
		return CalculoImpostoINSS.calculo(valor, tipo);
	}	
	

	
	/*
	 * Calculo da aliquota e do valor do imposto IRRF retido PF/PJ
	 */
	public BigDecimal valorIRRFRetido(BigDecimal valor, BigDecimal inss, BigDecimal dependente, String tipo) {

		if(tipo.equals("J")) {
			BigDecimal aliquota = aliquotaIRPJ();
			return CalculoImpostoIRRF.calculo(valor, aliquota);
		}else {
			BigDecimal base = valor.subtract(inss).subtract(dependente).setScale(2);
			TabelaIRPF tabela = retornaTabelaPFPorValor(base);
			return CalculoImpostoIRRF.calculo(base, tabela.getAliquota()).subtract(tabela.getDeducao());
		}
	}
	
	private BigDecimal aliquotaIRPJ() {
		TabelaIRPJ tabela = tabelaIRPJRepository.findOne(1l);
		if(tabela == null || tabela.getId() == null) {
			throw new RegistroNaoCadastradoException("Não localizado a tabela de aliquotas!");
		}
		return tabela.getAliquotaIR(); 
	}	
	
	private TabelaIRPF retornaTabelaPFPorValor(BigDecimal valor) {
		List<TabelaIRPF> tabelas = tabelaIRPFRepository.findAll();

		Predicate<TabelaIRPF> filtro = t -> {
			return valor.compareTo(t.getValorInicial()) >=0 && valor.compareTo(t.getValorFinal()) <=0; 
		};
		
		return tabelas.stream().filter(i -> filtro.test(i)).findFirst()
				.orElseThrow( () -> new RegistroNaoCadastradoException("Tabela de aliquotas não encontrada"));
	}
	

	
	
	/*
	 * Setar a data de vencimento conforme o tipo do imposto
	 */
	private LocalDate setarDataVencimentoPorTipoImposto(LocalDate data, String tipoImposto) {
		if(tipoImposto.equals("ISS")) {
			return DatasUtils.somarDiasNoInicioMesRetornandoDataUtil(data.plusMonths(1), 10);
		}else if (tipoImposto.equals("INSS")) {
			return DatasUtils.somarDiasNoInicioMesRetornandoDataUtil(data.plusMonths(1), 15);
		}else {
			return DatasUtils.somarDiasNoInicioMesRetornandoDataUtil(data.plusMonths(1), 20);
		}
	}













//	public Imposto findOne(Long id) {
//		return repository.findOne(id);
//	}
//
//	public Page<Imposto> filtrar(ImpostoFilter impostoFilter, Pageable pageable) {
//		return repository.filtrar(impostoFilter, pageable);
//	}

//	@Transactional
//	public void gerar(Imposto imposto) {
//		
//		
//		if(imposto.getMulta() == null) {
//			imposto.setMulta(BigDecimal.ZERO);
//		}
//		if(imposto.getJuros() == null) {
//			imposto.setJuros(BigDecimal.ZERO);
//		}
//		imposto.setStatus("GERADO");
//		imposto.setTotal(imposto.getValor().add(imposto.getJuros().add(imposto.getMulta())));
//		imposto.setContaPagarGerada(contaPagarRepository.save(criarContaPagarGerada(imposto)));
//		verificaDataVencimento(imposto);
//		repository.save(imposto);
//	}
//
//	private void verificaDataVencimento(Imposto imposto) {
//		if(imposto.getVencimento().isBefore(imposto.getApuracao())) {
//			throw new PagamentoNaoEfetuadoException("A data do vencimento é menor que a data de apuração");
//		}
//	}
//
//	private ContaPagar criarContaPagarGerada(Imposto imposto) {	
//		try {	
//			ContaPagar conta = new ContaPagar();	
//			conta.setDataEmissao(imposto.getApuracao());	
//			conta.setDocumento(imposto.getNome() +"-" + imposto.getContaPagar().getFornecedor().getSigla()+ "-"+imposto.getId());	
//			conta.setEmpresa(imposto.getContaPagar().getEmpresa());	
//			conta.setHistorico(imposto.getNome() +" RETIDO " + imposto.getContaPagar().getFornecedor().getNome()+ "- DOC nº: "+imposto.getNumeroNF());	
//			conta.setNotaFiscal(imposto.getNumeroNF());	
//			conta.setParcela(1);	
//			conta.setStatus("ABERTO");	
//			conta.setTotalParcela(1);	
//			conta.setValor(imposto.getTotal());	
//			conta.setVencimento(imposto.getVencimento());	
//			conta.setRecibo(true);
//			conta.setTemNota(true);
//			conta.setFornecedor(inserirFornecedor(imposto.getNome()));	
//			conta.setPlanoContaSecundaria(inserirPlanoContaSecundaria(imposto.getNome()));	
//			return conta;	
//		} catch (Exception e) {	
//			throw new PagamentoNaoEfetuadoException("Não foi possível incluir a conta a pagar do imposto selecionado");	
//		}	
//	}
//
//	private Fornecedor inserirFornecedor(String nome) {
//		Fornecedor forn = new Fornecedor();
//		if (nome.equals("IRRF") || nome.equals("PCCS")) {
//			forn.setId(54L); // RECEITA FEDERAL
//		} else if (nome.equals("INSS")) {
//			forn.setId(55L); // PREVIDENCIA SOCIAL
//		} else {
//			forn.setId(4L); // PREFEITURA DE IGUATU
//		}
//		return forn;
//	}
//
//	private PlanoContaSecundaria inserirPlanoContaSecundaria(String nome) {
//		PlanoContaSecundaria planoConta = new PlanoContaSecundaria();
//		if (nome.equals("IRRF")) {
//			planoConta.setId(58L); // IRRF RETIDO NOTAS PJ
//		} else if (nome.equals("PIS/COFINS/CSLL")) {
//			planoConta.setId(59L); // COFINS RETIDO NOTAS PJ
//		} else if (nome.equals("INSS")) {
//			planoConta.setId(55L); // PREVIDENCIA SOCIAL
//		} else {
//			planoConta.setId(58L); //
//		}
//		return planoConta;
//	}
//	
//	@Transactional
//	public void excluir(Long id) {
//		try {
//			repository.delete(id);
//			repository.flush();
//		} catch (PersistenceException e) {
//			throw new ImpossivelExcluirEntidade(
//					"Não foi possivel excluir o imposto. Exclua primeiro o(s) relacionamento(s) com outra(s) tabela(s)!");
//		}
//
//	}
//	
//	
//	@Transactional	
//	public void cancelarGerar(Imposto imposto) {	
//		try {	
//			ContaPagar conta = contaPagarRepository.findOne(imposto.getContaPagarGerada().getId());	
//			if(conta.getStatus().equals("ABERTO")) {	
//				imposto.setContaPagarGerada(null);	
//				contaPagarRepository.delete(conta);	
//				imposto.setJuros(BigDecimal.ZERO);	
//				imposto.setMulta(BigDecimal.ZERO);	
//				imposto.setTotal(imposto.getValor());	
//				imposto.setStatus("ABERTO");	
//				repository.save(imposto);	
//			}	
//		} catch (Exception e) {	
//			throw new ImpossivelExcluirEntidade("Não foi possivel cancelar o imposto gerado!!!"); 	
//		}	
//	}
//
//	public List<Imposto> findByContaPagar(ContaPagar contaPagar) {
//		return repository.findByContaPagar(contaPagar);
//	}
//
//
//


}
