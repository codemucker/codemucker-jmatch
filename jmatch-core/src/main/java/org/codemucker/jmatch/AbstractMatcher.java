package org.codemucker.jmatch;

import org.codemucker.jmatch.DefaultDescription;
import org.codemucker.jmatch.Description;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.NullMatchContext;

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
			diag.mismatched("expect not null");
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
	 * description. Remember to call this parent
	 */
	@Override
	public void describeTo(Description desc) {
	    if( desc.isNull()){
	        return;
	    }
		if(allowNulls == AllowNulls.NO){
			desc.text("not null");
		}
	}
	
	protected boolean isAllowNull(){
	    return allowNulls == AllowNulls.YES;
	}
}
