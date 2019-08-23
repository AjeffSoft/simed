package com.ajeff.simed.financeiro.service.exception;

public class ImpossivelExcluirEntidade extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ImpossivelExcluirEntidade(String msg) {
		super(msg);
	}

}
