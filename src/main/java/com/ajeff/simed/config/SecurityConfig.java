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
//				.antMatchers("/contaEmpresa/nova/**").hasRole("CADASTRAR_CONTAEMPRESA")
//				.antMatchers("/contaEmpresa/alterar/**").hasRole("ALTERAR_CONTAEMPRESA")
//				.antMatchers("/contaEmpresa/excluir/**").hasRole("EXCLUIR_CONTAEMPRESA")
//				.antMatchers("/empresa/nova").hasRole("CADASTRAR_EMPRESA")
//				.antMatchers("/empresa/pesquisar").hasRole("PESQUISAR_EMPRESA")
//				.antMatchers("/empresa/alterar/**").hasRole("ALTERAR_EMPRESA")
//				.antMatchers("/empresa/excluir/**").hasRole("EXCLUIR_EMPRESA")
//				.antMatchers("/grupo/novo").hasRole("CADASTRAR_GRUPO")
//				.antMatchers("/grupo/pesquisar").hasRole("PESQUISAR_GRUPO")
//				.antMatchers("/grupo/alterar/**").hasRole("ALTERAR_GRUPO")
//				.antMatchers("/grupo/excluir/**").hasRole("EXCLUIR_GRUPO")
//				.antMatchers("/permissao/novo").hasRole("CADASTRAR_PERMISSAO")
//				.antMatchers("/permissao/pesquisar").hasRole("PESQUISAR_PERMISSAO")
//				.antMatchers("/permissao/alterar/**").hasRole("ALTERAR_PERMISSAO")
//				.antMatchers("/permissao/excluir/**").hasRole("EXCLUIR_PERMISSAO")
//				.antMatchers("/usuario/novo").hasRole("CADASTRAR_USUARIO")
//				.antMatchers("/usuario").hasRole("PESQUISAR_USUARIO")
//				.antMatchers("/usuario/alterar/**").hasRole("ALTERAR_USUARIO")
//				.antMatchers("/usuario/excluir/**").hasRole("EXCLUIR_USUARIO")
//				.antMatchers("/financeiro/agencia/nova").hasRole("CADASTRAR_AGENCIA")
//				.antMatchers("/financeiro/agencia/pesquisar").hasRole("PESQUISAR_AGENCIA")
//				.antMatchers("/financeiro/agencia/alterar/**").hasRole("ALTERAR_AGENCIA")
//				.antMatchers("/financeiro/agencia/excluir/**").hasRole("EXCLUIR_AGENCIA")
//				.antMatchers("/financeiro/banco/novo").hasRole("CADASTRAR_BANCO")
//				.antMatchers("/financeiro/banco/pesquisar").hasRole("PESQUISAR_BANCO")
//				.antMatchers("/financeiro/banco/alterar/**").hasRole("ALTERAR_BANCO")
//				.antMatchers("/financeiro/banco/excluir/**").hasRole("EXCLUIR_BANCO")
//				.antMatchers("/financeiro/cliente/novo").hasRole("CADASTRAR_CLIENTE")
//				.antMatchers("/financeiro/cliente/pesquisar").hasRole("PESQUISAR_CLIENTE")
//				.antMatchers("/financeiro/cliente/alterar/**").hasRole("ALTERAR_CLIENTE")
//				.antMatchers("/financeiro/cliente/excluir/**").hasRole("EXCLUIR_CLIENTE")
//				.antMatchers("/financeiro/contaPagar/nova").hasRole("CADASTRAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/pesquisar").hasRole("PESQUISAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/autorizarConta").hasRole("AUTORIZAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/autorizar").hasRole("AUTORIZAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/cancelarAutorizar").hasRole("AUTORIZAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/relatorio").hasRole("RELATORIO_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/alterar/**").hasRole("ALTERAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/excluir/**").hasRole("EXCLUIR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/detalheConta/**").hasRole("VISUALIZAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaPagar/imprimirFicha/**").hasRole("VISUALIZAR_CONTAPAGAR")
//				.antMatchers("/financeiro/contaReceber/nova").hasRole("CADASTRAR_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/pesquisar").hasRole("PESQUISAR_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/receber/**").hasRole("RECEBER_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/cancelarRecebimento").hasRole("RECEBER_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/relatorio").hasRole("RELATORIO_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/alterar/**").hasRole("ALTERAR_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/excluir/**").hasRole("EXCLUIR_CONTARECEBER")
//				.antMatchers("/financeiro/contaReceber/detalheConta/**").hasRole("VISUALIZAR_CONTARECEBER")
//				.antMatchers("/financeiro/evento").hasRole("CADASTRAR_EVENTO")
//				.antMatchers("/financeiro/extrato/novo/**").hasRole("CADASTRAR_EXTRATO")
//				.antMatchers("/financeiro/extrato/pesquisar").hasRole("PESQUISAR_EXTRATO")
//				.antMatchers("/financeiro/extrato/selecionarConta").hasRole("PESQUISAR_EXTRATO")
//				.antMatchers("/financeiro/fornecedor/novo").hasRole("CADASTRAR_FORNECEDOR")
//				.antMatchers("/financeiro/fornecedor/pesquisar").hasRole("PESQUISAR_FORNECEDOR")
//				.antMatchers("/financeiro/fornecedor/alterar/**").hasRole("ALTERAR_FORNECEDOR")
//				.antMatchers("/financeiro/fornecedor/excluir/**").hasRole("EXCLUIR_FORNECEDOR")
//				.antMatchers("/financeiro/fornecedor/historico/**").hasRole("VISUALIZAR_FORNECEDOR")
//				.antMatchers("/financeiro/imposto/novo").hasRole("CADASTRAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/pesquisar").hasRole("PESQUISAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/alterar/**").hasRole("ALTERAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/excluir/**").hasRole("EXCLUIR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/detalheImposto/**").hasRole("VISUALIZAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/abreGerar/**").hasRole("GERAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/imprimir/darf/**").hasRole("GERAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/gerar/**").hasRole("GERAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/cancelarGerar/**").hasRole("GERAR_IMPOSTO")
//				.antMatchers("/financeiro/imposto/relatorio").hasRole("RELATORIO_IMPOSTO")
//				.antMatchers("/financeiro/pagamento/novo").hasRole("CADASTRAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/contasAutorizadas").hasRole("CADASTRAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/comprovante").hasRole("CADASTRAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/pesquisar").hasRole("PESQUISAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/alterar/**").hasRole("ALTERAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/excluir/**").hasRole("EXCLUIR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/imprimirOrdemPagamento/**").hasRole("IMPRIMIRORDEM_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/cancelarPagar").hasRole("CONFIRMAR_PAGAMENTO")
//				.antMatchers("/financeiro/pagamento/relatorio").hasRole("RELATORIO_PAGAMENTO")
//				.antMatchers("/financeiro/planoConta/novo").hasRole("CADASTRAR_PLANOCONTA")
//				.antMatchers("/financeiro/planoConta/pesquisar").hasRole("PESQUISAR_PLANOCONTA")
//				.antMatchers("/financeiro/planoConta/alterar/**").hasRole("ALTERAR_PLANOCONTA")
//				.antMatchers("/financeiro/planoConta/excluir/**").hasRole("EXCLUIR_PLANOCONTA")
//				.antMatchers("/financeiro/planoConta/historico/**").hasRole("VISUALIZAR_PLANOCONTA")
//				.antMatchers("/financeiro/planoContaSecundaria/novo").hasRole("CADASTRAR_CONTASECUNDARIA")
//				.antMatchers("/financeiro/planoContaSecundaria/pesquisar").hasRole("PESQUISAR_CONTASECUNDARIA")
//				.antMatchers("/financeiro/planoContaSecundaria/alterar/**").hasRole("ALTERAR_CONTASECUNDARIA")
//				.antMatchers("/financeiro/planoContaSecundaria/excluir/**").hasRole("EXCLUIR_CONTASECUNDARIA")
//				.antMatchers("/financeiro/planoContaSecundaria/historico/**").hasRole("VISUALIZAR_CONTASECUNDARIA")
//				.antMatchers("/financeiro/transferencia/novo").hasRole("CADASTRAR_TRANSFERENCIA")
//				.antMatchers("/financeiro/transferencia/comprovante/**").hasRole("CADASTRAR_TRANSFERENCIA")
//				.antMatchers("/financeiro/transferencia/imprimirOrdemTransferencia/**").hasRole("CADASTRAR_TRANSFERENCIA")
//				.antMatchers("/financeiro/transferencia/pesquisar").hasRole("PESQUISAR_TRANSFERENCIA")
//				.antMatchers("/financeiro/transferencia/cancelarPagar/**").hasRole("CONFIRMAR_TRANSFERENCIA")
//				.antMatchers("/financeiro/transferencia/excluir/**").hasRole("EXCLUIR_TRANSFERENCIA")
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