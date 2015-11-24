package com.pstc.framework.exception;

@SuppressWarnings("serial")
public class PageNavigationException extends RuntimeException {

	public PageNavigationException(String pageName) {
		super("Failed to navigate to page -" + pageName);
	}

	public PageNavigationException(String message, Throwable cause) {
		super("Failed to navigate to page -" + message, cause);
	}

}
