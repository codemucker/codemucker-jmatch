package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;

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

	/**
	 * Match on the given types predicate
	 * 
	 * E.g
	 * 
	 * AFoo.with().type(f=>f.getName()=="Alice" || f.getName()=="Bob")
	 * @param predicate
	 * @return
	 */
	protected void predicate(Predicate<T> predicate){
		addMatcher(PredicateToMatcher.from(predicate));
	}
	
	protected void addMatcher(Matcher<T> matcher){
		matchers.add(matcher);
	}
	
}
