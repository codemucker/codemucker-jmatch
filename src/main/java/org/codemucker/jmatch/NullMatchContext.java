package org.codemucker.jmatch;

public class NullMatchContext extends NullDescription implements MatchDiagnostics {

	public static final MatchDiagnostics INSTANCE = new NullMatchContext();
	
	@Override
	public <T> boolean TryMatch(T actual, Matcher<T> matcher) {
		return matcher.matches(actual, this);
	}

	@Override
	public MatchDiagnostics Matched(String text, Object... args) {
		return this;
	}

	@Override
	public MatchDiagnostics MisMatched(String text, Object... args) {
		return this;
	}

	@Override
	public MatchDiagnostics Matched(SelfDescribing selfDescribing) {
		return this;
	}

	@Override
	public MatchDiagnostics MisMatched(SelfDescribing selfDescribing) {
		return this;
	}
}
