package com.github.aha.poc.itext;

public class ITextException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ITextException(String message) {
		super(message);
	}

	public ITextException(String message, Throwable cause) {
		super(message, cause);
	}

}
