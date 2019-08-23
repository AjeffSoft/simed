package com.ajeff.simed.geral.thymeleaf;

import java.util.HashSet;
import java.util.Set;

import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.standard.StandardDialect;

import com.ajeff.simed.geral.thymeleaf.processor.ClassForErrorAttributeTagProcessor;
import com.ajeff.simed.geral.thymeleaf.processor.MenuAttributeTagProcessor;
import com.ajeff.simed.geral.thymeleaf.processor.MessageElementTagProcessor;
import com.ajeff.simed.geral.thymeleaf.processor.OrderElementTagProcessor;
import com.ajeff.simed.geral.thymeleaf.processor.PaginationElementTagProcessor;

public class SimedDialect extends AbstractProcessorDialect {

	public SimedDialect() {
		super("Simed", "simed", StandardDialect.PROCESSOR_PRECEDENCE);
	}
	
	@Override
	public Set<IProcessor> getProcessors(String dialectPrefix) {
		final Set<IProcessor> processadores = new HashSet<>();
		processadores.add(new ClassForErrorAttributeTagProcessor(dialectPrefix));
		processadores.add(new MessageElementTagProcessor(dialectPrefix));
		processadores.add(new OrderElementTagProcessor(dialectPrefix));
		processadores.add(new PaginationElementTagProcessor(dialectPrefix));
		processadores.add(new MenuAttributeTagProcessor(dialectPrefix));
		return processadores;
	}

}