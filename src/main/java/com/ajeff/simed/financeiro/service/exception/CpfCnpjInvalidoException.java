package com.ajeff.simed.financeiro.service.exception;

public class CpfCnpjInvalidoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CpfCnpjInvalidoException(String msg){
		super(msg);
	}

}
