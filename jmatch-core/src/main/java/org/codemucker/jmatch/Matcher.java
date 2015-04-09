package org.codemucker.jmatch;

import org.codemucker.lang.annotation.ThreadSafe;

/**
 * An object that is used to validate a given object matches a set of criteria.
 * 
 * <p>Implementations should be thread-safe once fully constructed</p>
 * 
 */
@ThreadSafe(caveats="only after being constructed")
public interface Matcher<T> extends SelfDescribing {

	public boolean matches(T actual,MatchDiagnostics ctxt);
	public boolean matches(T actual);
}
