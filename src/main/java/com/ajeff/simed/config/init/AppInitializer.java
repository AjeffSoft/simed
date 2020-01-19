package com.ajeff.simed.config.init;

import javax.servlet.Filter;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.ajeff.simed.config.JPAConfig;
import com.ajeff.simed.config.SecurityConfig;
import com.ajeff.simed.config.ServiceConfig;
import com.ajeff.simed.config.WebConfig;

public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer{

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[] { JPAConfig.class, ServiceConfig.class, SecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[] { WebConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
	
	/*Método para forçar o uso do UTF-8
	 * */
	@Override
	protected Filter[] getServletFilters() {
		FormContentFilter  httpPutFormContentFilter = new FormContentFilter ();
		return new Filter[] { httpPutFormContentFilter };
	}	
	
	@Override
	protected void customizeRegistration(Dynamic registration) {
		registration.setMultipartConfig(new MultipartConfigElement(""));
	}	

}
