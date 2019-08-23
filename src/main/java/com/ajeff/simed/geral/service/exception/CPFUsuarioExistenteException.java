package com.ajeff.simed.geral.service.exception;

public class CPFUsuarioExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CPFUsuarioExistenteException(String message) {
		super(message);
	}
}
