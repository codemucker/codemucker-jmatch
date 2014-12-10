package org.codemucker.jmatch;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

public class MatchAllMatcher<T> extends AbstractMatcher<T> {
    private final ImmutableCollection<Matcher<T>> matchers;

    public MatchAllMatcher(Iterable<Matcher<T>> matchers) {
    	this.matchers = ImmutableList.copyOf(matchers);		    
    }

    public MatchAllMatcher(Matcher<T>[] matchers) {
	    this.matchers = ImmutableList.copyOf(matchers);
    }

    @Override
    public boolean matchesSafely(T found, MatchDiagnostics ctxt) {
    	for(Matcher<T> matcher:matchers){
    		if(!matcher.matches(found)){
    			return false;
    		}
    	}
    	return true;
    }

    @Override
    public void describeTo(Description desc) {
        if(matchers.size() > 0){
            desc.values("matching all",matchers);
        } else {
            super.describeTo(desc);
        }
    }
}