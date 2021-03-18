package com.app.bookeepy.exceptions;

public class BookNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2581227292783040179L;

	public BookNotFoundException(String message) {
		super(message);
	}

}
