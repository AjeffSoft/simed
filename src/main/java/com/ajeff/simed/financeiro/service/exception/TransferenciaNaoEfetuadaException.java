package com.ajeff.simed.financeiro.service.exception;

public class TransferenciaNaoEfetuadaException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public TransferenciaNaoEfetuadaException(String msg) {
		super(msg);
	}

}
