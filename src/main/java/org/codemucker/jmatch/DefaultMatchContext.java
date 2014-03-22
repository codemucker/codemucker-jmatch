package org.codemucker.jmatch;

public class DefaultMatchContext extends DefaultDescription implements MatchDiagnostics {

	@Override
	public <T> boolean TryMatch(T actual, Matcher<T> matcher) {
		return matcher.matches(actual, this);
	}

	@Override
	public MatchDiagnostics Matched(String text, Object... args)
    {
        Matched(DefaultDescription.with().text(text,args));
        return this;
    }

	@Override
    public MatchDiagnostics MisMatched(String text, Object... args)
    {
        MisMatched(DefaultDescription.with().text(text,args));
        return this;
    }

    @Override
    public MatchDiagnostics Matched(SelfDescribing selfDescribing)
    {
        text("Match");
        child(selfDescribing);
        return this;
    }

    @Override
    public MatchDiagnostics MisMatched(SelfDescribing selfDescribing)
    {
        text("Mismatch!");
        child(selfDescribing);
        return this;
    }
    
}
