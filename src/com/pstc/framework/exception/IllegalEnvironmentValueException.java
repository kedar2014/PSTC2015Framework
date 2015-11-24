package com.pstc.framework.exception;

@SuppressWarnings("serial")
public class IllegalEnvironmentValueException extends RuntimeException {

	public IllegalEnvironmentValueException(String message) {
		super(message);
	}

	public IllegalEnvironmentValueException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalEnvironmentValueException() {
		super("Illegal Platform Value");
	}

}
