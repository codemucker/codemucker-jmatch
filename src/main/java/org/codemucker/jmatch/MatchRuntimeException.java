package org.codemucker.jmatch;

public class MatchRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MatchRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MatchRuntimeException(String msg) {
		super(msg);
	}
}
