package org.codemucker.jmatch;

import com.google.common.base.Predicate;

public class PredicateToMatcher<T> extends AbstractNotNullMatcher<T> {

	private final Predicate<T> predicate;

	public static <T> Matcher<T> from(Predicate<T> predicate){
		return new PredicateToMatcher<T>(predicate);
	}
	
	public PredicateToMatcher(Predicate<T> predicate) {
		this.predicate = predicate;
	}

	@Override
	protected boolean matchesSafely(T actual, MatchDiagnostics diag) {
		return predicate.apply(actual);
	}
	
}
