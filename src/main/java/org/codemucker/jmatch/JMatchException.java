package org.codemucker.jmatch;

/**
 * Throw to indicate some issue with matching. NOT to be used for a failing match ,should be thrown only
 * when there is some internal error
 */
public class JMatchException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JMatchException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public JMatchException(String msg) {
		super(msg);
	}
}
