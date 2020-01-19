package com.ajeff.simed.config;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

import com.ajeff.simed.financeiro.service.FornecedorService;

@Configuration
public class TestContext {
	
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
 
        messageSource.setBasename("i18n/messages");
        messageSource.setUseCodeAsDefaultMessage(true);
 
        return messageSource;
    }
    
    @Bean
    public FornecedorService fornecedorService() {
    	return Mockito.mock(FornecedorService.class);
    }

}
