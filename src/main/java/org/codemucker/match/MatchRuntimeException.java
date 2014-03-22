package org.codemucker.match;

public class MatchRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 7020389725033454609L;

	public MatchRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public MatchRuntimeException(String msg) {
		super(msg);
	}
}
