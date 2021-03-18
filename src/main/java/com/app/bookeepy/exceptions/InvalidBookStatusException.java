package com.app.bookeepy.exceptions;

public class InvalidBookStatusException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8320726545738014423L;

	public InvalidBookStatusException() {
		super("Error: The specified status is invalid. It must be Wished or Owned!");
	}

}
