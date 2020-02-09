package com.ajeff.simed.financeiro.service.exception;

public class MovimentacaoNaoPossivelFecharException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MovimentacaoNaoPossivelFecharException (String msg){
		super (msg);
	}

}
