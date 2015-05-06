package org.codemucker.jmatch.expression;

public class ParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParseException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParseException(String message) {
		super(message);
	}

}