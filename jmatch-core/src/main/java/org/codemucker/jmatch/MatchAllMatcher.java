package org.codemucker.jmatch;

import com.google.common.collect.ImmutableList;

public class MatchAllMatcher<T> extends AbstractMatcher<T> {

	@SuppressWarnings("rawtypes")
	private static final Matcher[] EMPTY_MATCHERS = new Matcher<?>[0];
	
    private final Matcher<T>[] matchers;

    @SuppressWarnings("unchecked")
	public MatchAllMatcher(Iterable<Matcher<T>> matchers) {  
    	this.matchers = ImmutableList.copyOf(matchers).toArray(EMPTY_MATCHERS);	    
    }

    @SuppressWarnings("unchecked")
	public MatchAllMatcher(Matcher<T>[] matchers) {
    	this.matchers = ImmutableList.copyOf(matchers).toArray(EMPTY_MATCHERS);	 
    }

    @Override
    public boolean matchesSafely(T found, MatchDiagnostics ctxt) {
    	for(int i = 0; i < matchers.length;i++){
    		if(!matchers[i].matches(found)){
    			return false;
    		}
    	}
    	return true;
    }

    @Override
    public void describeTo(Description desc) {
        if(matchers.length > 0){
            desc.values("matching all",matchers);
        } else {
            super.describeTo(desc);
        }
    }
}