package com.ajeff.simed.config;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ajeff.simed.financeiro.model.Fornecedor;
import com.ajeff.simed.financeiro.repository.FornecedoresRepository;
import com.ajeff.simed.geral.model.ContaEmpresa;
import com.ajeff.simed.geral.model.Usuario;
import com.ajeff.simed.geral.repository.UsuariosRepository;
import com.ajeff.simed.satisfacao.model.Pesquisa;
import com.ajeff.simed.satisfacao.repository.PesquisasRepository;

@Configuration
@EnableJpaRepositories(basePackageClasses = {UsuariosRepository.class, FornecedoresRepository.class, PesquisasRepository.class}, enableDefaultTransactions=false )
@ComponentScan("com.ajeff.simed")
@EnableTransactionManagement
public class JPAConfig {

	@Bean
	public DataSource dataSource() {
		JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
		dataSourceLookup.setResourceRef(true);
		return dataSourceLookup.getDataSource("jdbc/sinteDB");
	}
	
	//CONGIGURAÇÃO HIBERNATE
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(false);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQLDialect");
		return adapter;
	}
	
	//CRIAR O ENTITY MANAGER
	@Bean
	public EntityManagerFactory entityManagerFactory(DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setDataSource(dataSource);
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		factory.setPackagesToScan(Usuario.class.getPackage().getName(), 
				Fornecedor.class.getPackage().getName(),
				ContaEmpresa.class.getPackage().getName(),
				Pesquisa.class.getPackage().getName());
		//factory.setMappingResources("sql/consultas-nativas.xml");
		factory.afterPropertiesSet();
		return factory.getObject();
	}
	
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}