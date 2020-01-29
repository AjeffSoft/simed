package com.ajeff.simed.financeiro.service.exception;

public class MovimentacaoFechadaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MovimentacaoFechadaException (String msg){
		super (msg);
	}

}
