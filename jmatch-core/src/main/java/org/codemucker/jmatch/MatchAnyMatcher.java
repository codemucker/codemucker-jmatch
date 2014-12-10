package org.codemucker.jmatch;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

public class MatchAnyMatcher<T> extends AbstractMatcher<T> {
    private final ImmutableCollection<Matcher<T>> matchers;

    public MatchAnyMatcher(Iterable<Matcher<T>> matchers) {
    	this.matchers = ImmutableList.copyOf(matchers);		    
    }

    public MatchAnyMatcher(Matcher<T>[] matchers) {
	    this.matchers = ImmutableList.copyOf(matchers);
    }

    @Override
    public boolean matchesSafely(T found, MatchDiagnostics ctxt) {
    	for(Matcher<T> matcher:matchers){
    		if(matcher.matches(found)){
    			return true;
    		}
    	}
    	return false;
    }

    @Override
    public void describeTo(Description desc) {
        if(matchers.size() > 0){
            desc.values("matching any",matchers);
        } else {
            super.describeTo(desc);
        }
    }
}