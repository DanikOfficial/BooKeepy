package com.app.bookeepy.exceptions;

public class BookAlreadyExistsException extends RuntimeException{
   /**
	 * 
	 */
	private static final long serialVersionUID = -826245097791338147L;

public  BookAlreadyExistsException(String message) {
        super(message);
    }
}
