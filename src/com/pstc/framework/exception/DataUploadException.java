package com.pstc.framework.exception;

@SuppressWarnings("serial")
public class DataUploadException extends RuntimeException {

	public DataUploadException(String message) {
		super(message);
	}

	public DataUploadException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataUploadException(Throwable cause) {
		super(cause);
	}

	public DataUploadException() {
		super("Unable to upload Data");
	}

}
