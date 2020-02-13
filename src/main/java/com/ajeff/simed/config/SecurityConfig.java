package com.ajeff.simed.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.ajeff.simed.geral.security.AppUserDetailsService;

@EnableWebSecurity
@ComponentScan(basePackageClasses = AppUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService service;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception { 
		web.ignoring()
		.antMatchers("/layout/**")
		.antMatchers("/stylesheets/**")
		.antMatchers("/javascripts/**")
		.antMatchers("/images/**");	
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/financeiro/movimentacao/pesquisar").hasRole("FINANCEIRO_MOVIMENTACAO_PESQUISAR")
				.antMatchers("/financeiro/extrato/pesquisar").hasRole("FINANCEIRO_EXTRATO_PESQUISAR")
				.antMatchers("/financeiro/extrato/selecionarConta").hasRole("FINANCEIRO_EXTRATO_PESQUISAR")
				.antMatchers("/financeiro/contaPagar/pesquisar").hasRole("FINANCEIRO_CONTAPAGAR_PESQUISAR")
				.antMatchers("/financeiro/contaReceber/pesquisar").hasRole("FINANCEIRO_CONTARECEBER_PESQUISAR")
				.antMatchers("/financeiro/transferencia/pesquisar").hasRole("FINANCEIRO_TRANSFERENCIA_PESQUISAR")
				.antMatchers("/financeiro/planoConta/pesquisar").hasRole("FINANCEIRO_PLANOCONTA_PESQUISAR")
				.antMatchers("/financeiro/imposto/pesquisar").hasRole("FINANCEIRO_IMPOSTO_PESQUISAR")
				.antMatchers("/financeiro/fornecedor/pesquisar").hasRole("FINANCEIRO_FORNECEDOR_PESQUISAR")
				.antMatchers("/financeiro/cliente/pesquisar").hasRole("FINANCEIRO_FORNECEDOR_PESQUISAR")
				.antMatchers("/financeiro/contaPagar/autorizarConta").hasRole("FINANCEIRO_AUTORIZARCONTA_PESQUISAR")
				.antMatchers("/financeiro/pagamento/pesquisar").hasRole("FINANCEIRO_PAGAMENTO_PESQUISAR")
				.antMatchers("/financeiro/agencia/pesquisar").hasRole("FINANCEIRO_AGENCIA_PESQUISAR")
				.antMatchers("/financeiro/banco/pesquisar").hasRole("FINANCEIRO_BANCO_PESQUISAR")
				.antMatchers("/empresa/pesquisar").hasRole("GERAL_EMPRESA_PESQUISAR")
				.antMatchers("/grupo/pesquisar").hasRole("GERAL_GRUPO_PESQUISAR")
				.antMatchers("/permissao/pesquisar").hasRole("GERAL_PERMISSAO_PESQUISAR")
				.antMatchers("/usuario/pesquisar").hasRole("GERAL_USUARIO_PESQUISAR")
				.antMatchers("/financeiro/movimentacao/novo").hasRole("FINANCEIRO_MOVIMENTACAO_CADASTRAR")
				.antMatchers("/financeiro/contaPagar/nova").hasRole("FINANCEIRO_CONTAPAGAR_CADASTRAR")
				.antMatchers("/financeiro/contaReceber/nova").hasRole("FINANCEIRO_CONTARECEBER_CADASTRAR")
				.antMatchers("/financeiro/transferencia/novo").hasRole("FINANCEIRO_TRANSFERENCIA_CADASTRAR")
				.antMatchers("/financeiro/planoConta/novo").hasRole("FINANCEIRO_PLANOCONTA_CADASTRAR")
				.antMatchers("/financeiro/fornecedor/novo").hasRole("FINANCEIRO_FORNECEDOR_CADASTRAR")
				.antMatchers("/financeiro/cliente/novo").hasRole("FINANCEIRO_FORNECEDOR_CADASTRAR")
				.antMatchers("/financeiro/contaPagar/autorizarConta").hasRole("FINANCEIRO_AUTORIZARCONTA_CADASTRAR")
				.antMatchers("/financeiro/pagamento/novo/**").hasRole("FINANCEIRO_PAGAMENTO_CADASTRAR")
				.antMatchers("/financeiro/pagamento/contasAutorizadas").hasRole("FINANCEIRO_PAGAMENTO_CADASTRAR")
				.antMatchers("/financeiro/agencia/nova").hasRole("FINANCEIRO_AGENCIA_CADASTRAR")
				.antMatchers("/financeiro/banco/novo").hasRole("FINANCEIRO_BANCO_CADASTRAR")
				.antMatchers("/empresa/nova").hasRole("GERAL_EMPRESA_CADASTRAR")
				.antMatchers("/grupo/novo").hasRole("GERAL_GRUPO_CADASTRAR")
				.antMatchers("/permissao/novo").hasRole("GERAL_PERMISSAO_CADASTRAR")
				.antMatchers("/usuario/novo").hasRole("GERAL_USUARIO_CADASTRAR")
				.antMatchers("/financeiro/planoContaSecundaria/nova/**").hasRole("FINANCEIRO_PLANOCONTA_SUBCONTA")
				.antMatchers("/financeiro/recebimento/novo/**").hasRole("FINANCEIRO_CONTARECEBER_RECEBER")
				.antMatchers("/contaEmpresa/nova/**").hasRole("GERAL_EMPRESA_CONTAS_BANCARIAS")
				.antMatchers("/financeiro/movimentacao/excluir/**").hasRole("FINANCEIRO_MOVIMENTACAO_EXCLUIR")
				.antMatchers("/financeiro/contaPagar/excluir/**").hasRole("FINANCEIRO_CONTAPAGAR_EXCLUIR")
				.antMatchers("/financeiro/contaReceber/excluir/**").hasRole("FINANCEIRO_CONTARECEBER_EXCLUIR")
				.antMatchers("/financeiro/transferencia/excluir/**").hasRole("FINANCEIRO_TRANSFERENCIA_EXCLUIR")
				.antMatchers("/financeiro/planoConta/excluir/**").hasRole("FINANCEIRO_PLANOCONTA_EXCLUIR")
				.antMatchers("/financeiro/imposto/excluir/**").hasRole("FINANCEIRO_IMPOSTO_EXCLUIR")
				.antMatchers("/financeiro/fornecedor/excluir/**").hasRole("FINANCEIRO_FORNECEDOR_EXCLUIR")
				.antMatchers("/financeiro/cliente/excluir/**").hasRole("FINANCEIRO_FORNECEDOR_EXCLUIR")
				.antMatchers("/financeiro/pagamento/excluir/**").hasRole("FINANCEIRO_PAGAMENTO_EXCLUIR")
				.antMatchers("/financeiro/agencia/excluir/**").hasRole("FINANCEIRO_AGENCIA_EXCLUIR")
				.antMatchers("/financeiro/banco/excluir/**").hasRole("FINANCEIRO_BANCO_EXCLUIR")
				.antMatchers("/empresa/excluir/**").hasRole("GERAL_EMPRESA_EXCLUIR")
				.antMatchers("/grupo/excluir/**").hasRole("GERAL_GRUPO_EXCLUIR")
				.antMatchers("/permissao/excluir/**").hasRole("GERAL_PERMISSAO_EXCLUIR")
				.antMatchers("/usuario/excluir/**").hasRole("GERAL_USUARIO_EXCLUIR")			
				.antMatchers("/financeiro/planoContaSecundaria/excluir/**").hasRole("FINANCEIRO_PLANOCONTA_SUBCONTA")
				.antMatchers("/financeiro/recebimento/excluir/**").hasRole("FINANCEIRO_CONTARECEBER_RECEBER")
				.antMatchers("/contaEmpresa/excluir/**").hasRole("GERAL_EMPRESA_CONTAS_BANCARIAS")
				.antMatchers("/financeiro/contaPagar/alterar/**").hasRole("FINANCEIRO_CONTAPAGAR_ALTERAR")
				.antMatchers("/financeiro/contaReceber/alterar/**").hasRole("FINANCEIRO_CONTARECEBER_ALTERAR")
				.antMatchers("/financeiro/planoConta/alterar/**").hasRole("FINANCEIRO_PLANOCONTA_ALTERAR")
				.antMatchers("/financeiro/fornecedor/alterar/**").hasRole("FINANCEIRO_FORNECEDOR_ALTERAR")
				.antMatchers("/financeiro/cliente/alterar/**").hasRole("FINANCEIRO_FORNECEDOR_ALTERAR")
				.antMatchers("/financeiro/agencia/alterar/**").hasRole("FINANCEIRO_AGENCIA_ALTERAR")
				.antMatchers("/financeiro/banco/alterar/**").hasRole("FINANCEIRO_BANCO_ALTERAR")
				.antMatchers("/empresa/alterar/**").hasRole("GERAL_EMPRESA_ALTERAR")
				.antMatchers("/grupo/alterar/**").hasRole("GERAL_GRUPO_ALTERAR")
				.antMatchers("/permissao/alterar/**").hasRole("GERAL_PERMISSAO_ALTERAR")
				.antMatchers("/usuario/alterar/**").hasRole("GERAL_USUARIO_ALTERAR")		
				.antMatchers("/financeiro/planoContaSecundaria/alterar/**").hasRole("FINANCEIRO_PLANOCONTA_SUBCONTA")
				.antMatchers("/contaEmpresa/alterar/**").hasRole("GERAL_EMPRESA_CONTAS_BANCARIAS")
				.antMatchers("/financeiro/movimentacao/detalhe/**").hasRole("FINANCEIRO_MOVIMENTACAO_DETALHAR")
				.antMatchers("/financeiro/extrato/detalhe/**").hasRole("FINANCEIRO_EXTRATO_DETALHAR")
				.antMatchers("/financeiro/contaPagar/detalhe/**").hasRole("FINANCEIRO_CONTAPAGAR_DETALHAR")
				.antMatchers("/financeiro/contaReceber/detalhe/**").hasRole("FINANCEIRO_CONTARECEBER_DETALHAR")
				.antMatchers("/financeiro/transferencia/detalhe/**").hasRole("FINANCEIRO_TRANSFERENCIA_DETALHAR")
				.antMatchers("/financeiro/planoConta/detalhe/**").hasRole("FINANCEIRO_PLANOCONTA_DETALHAR")
				.antMatchers("/financeiro/imposto/detalhe/**").hasRole("FINANCEIRO_IMPOSTO_DETALHAR")
				.antMatchers("/financeiro/fornecedor/detalhe/**").hasRole("FINANCEIRO_FORNECEDOR_DETALHAR")
				.antMatchers("/financeiro/cliente/detalhe/**").hasRole("FINANCEIRO_FORNECEDOR_DETALHAR")
				.antMatchers("/financeiro/pagamento/detalhe/**").hasRole("FINANCEIRO_PAGAMENTO_DETALHAR")
				.antMatchers("/empresa/detalhe/**").hasRole("GERAL_EMPRESA_DETALHAR")
				.antMatchers("/usuario/detalhe/**").hasRole("GERAL_USUARIO_DETALHAR")			
				.antMatchers("/financeiro/planoContaSecundaria/detalhe/**").hasRole("FINANCEIRO_PLANOCONTA_SUBCONTA")
				.antMatchers("/financeiro/movimentacao/fecharMovimento/**").hasRole("FINANCEIRO_MOVIMENTACAO_BAIXAR")
				.antMatchers("/financeiro/contaPagar/pendencia/**").hasRole("FINANCEIRO_CONTAPAGAR_PENDENCIA")
				.antMatchers("/financeiro/imposto/gerar/**").hasRole("FINANCEIRO_IMPOSTO_GERAR")
				.antMatchers("/financeiro/imposto/cancelarGerar/**").hasRole("FINANCEIRO_IMPOSTO_CANCELAR_GERAR")
				.antMatchers("/financeiro/pagamento/pagar/**").hasRole("FINANCEIRO_PAGAMENTO_BAIXAR")
				.antMatchers("/financeiro/pagamento/cancelarConfirmacao/**").hasRole("FINANCEIRO_PAGAMENTO_CANCELAR_BAIXA")
				.antMatchers("/financeiro/pagamento/comprovante/**").hasRole("FINANCEIRO_PAGAMENTO_CADASTRAR")
				.antMatchers("/financeiro/pagamento/comprovante").hasRole("FINANCEIRO_PAGAMENTO_CADASTRAR")
				.antMatchers("/financeiro/transferencia/comprovante/**").hasRole("FINANCEIRO_TRANSFERENCIA_CADASTRAR")
				.antMatchers("/financeiro/transferencia/cancelarPagar/**").hasRole("FINANCEIRO_TRANSFERENCIA_BAIXAR")
				.antMatchers("/financeiro/transferencia/cancelarPagar/**").hasRole("FINANCEIRO_TRANSFERENCIA_CANCELAR_BAIXA")
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/login") 
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.and()
			.exceptionHandling()
				.accessDeniedPage("/403")
				.and()
				.csrf().disable();			
		}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}	