package org.codemucker.match;

import org.codemucker.match.DefaultDescription;
import org.codemucker.match.Description;
import org.codemucker.match.Matcher;
import org.codemucker.match.NullMatchContext;

public abstract class AbstractMatcher<T> implements Matcher<T> {
	
	public enum AllowNulls {
		YES,NO
	}
	
	private final AllowNulls allowNulls;
	
	public AbstractMatcher(){
		allowNulls = AllowNulls.NO;
	}
	
	public AbstractMatcher(AllowNulls mode){
		this.allowNulls = mode;
	}
	
	@Override
	public final boolean matches(T actual) {
		return matches(actual, NullMatchContext.INSTANCE);
	}
	
	@Override
	public final boolean matches(T actual, MatchDiagnostics diag){
		if( actual == null && allowNulls == AllowNulls.NO){
			diag.MisMatched("expect not null");
			return false;
		}
		return matchesSafely(actual, diag);
	}

	protected abstract boolean matchesSafely(T actual, MatchDiagnostics diag);
		
	@Override
	public String toString(){
		Description d = new DefaultDescription();
		describeTo(d);
		return d.toString();
	}
	
	/**
	 * Subclasses should override this if they want to provide a
	 * description. Rmemeber to call this parent
	 */
	@Override
	public void describeTo(Description desc) {
		if(allowNulls == AllowNulls.NO){
			desc.text("not null");
		}
	}
}
