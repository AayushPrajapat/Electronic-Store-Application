package com.lcwd.electronic.store.exceptions;

public class BadApiRequest extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BadApiRequest() {
		super("Bad Request !!");
	}
	
	public BadApiRequest(String message) {
		super(message);
	}

}
