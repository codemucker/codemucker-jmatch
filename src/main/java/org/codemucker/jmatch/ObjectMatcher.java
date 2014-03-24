package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.List;

public class ObjectMatcher<T> extends AbstractMatcher<T> {

	private final List<Matcher<T>> matchers = new ArrayList<>();

	public ObjectMatcher(){
		super(AllowNulls.NO);
	}

	@Override
	public boolean matchesSafely(T actual, MatchDiagnostics ctxt) {
		//TODO:could turn straight into array
		for(Matcher<T> matcher : matchers){
			if(!ctxt.TryMatch(actual, matcher)){
				return false;
			}
		}
		return true;
	}
	
	protected void withMatcher(Matcher<T> matcher){
		matchers.add(matcher);
	}
}
