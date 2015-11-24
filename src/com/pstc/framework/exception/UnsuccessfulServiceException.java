package com.pstc.framework.exception;

@SuppressWarnings("serial")
public class UnsuccessfulServiceException extends RuntimeException {

	public UnsuccessfulServiceException(String message, Exception cause) {

		super(message + " kindly refer attached screenshot for more clearification \n\n Stacke Trace::\n", cause);
	}

	public UnsuccessfulServiceException(String message) {
		super(message + " kindly refer attached screenshot for more clearification \n\n Stacke Trace::\n");
	}

	public UnsuccessfulServiceException() {
		super("Service  Failled" + " kindly refer attached screenshot for more clearification");
	}

}
