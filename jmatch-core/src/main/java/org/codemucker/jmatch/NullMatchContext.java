package org.codemucker.jmatch;

public class NullMatchContext extends NullDescription implements MatchDiagnostics {

	public static final MatchDiagnostics INSTANCE = new NullMatchContext();
	
	@Override
	public <T> boolean tryMatch(SelfDescribing parent,T actual, Matcher<T> matcher) {
		return matcher.matches(actual, this);
	}

	@Override
	public MatchDiagnostics matched(String text, Object... args) {
		return this;
	}

	@Override
	public MatchDiagnostics mismatched(String text, Object... args) {
		return this;
	}

	@Override
	public MatchDiagnostics matched(SelfDescribing selfDescribing) {
		return this;
	}

	@Override
	public MatchDiagnostics mismatched(SelfDescribing selfDescribing) {
		return this;
	}

    @Override
    public MatchDiagnostics newChild() {
        return this;
    }

    @Override
    public Description newDescription() {
        return NullDescription.INSTANCE;
    }
}
