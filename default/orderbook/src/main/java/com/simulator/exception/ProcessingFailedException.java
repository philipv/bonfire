package com.simulator.exception;

public class ProcessingFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessingFailedException() {
		super();
	}

	public ProcessingFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProcessingFailedException(String message) {
		super(message);
	}

	public ProcessingFailedException(Throwable cause) {
		super(cause);
	}

}
