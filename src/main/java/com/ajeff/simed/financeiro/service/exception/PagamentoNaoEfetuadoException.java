package com.ajeff.simed.financeiro.service.exception;

public class PagamentoNaoEfetuadoException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PagamentoNaoEfetuadoException(String msg) {
		super(msg);
	}

}
