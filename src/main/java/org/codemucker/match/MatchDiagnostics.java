package org.codemucker.match;

public interface MatchDiagnostics extends Description {
	
	<T> boolean TryMatch(T actual, Matcher<T> matcher);
	
	public MatchDiagnostics Matched(String text, Object... args);
	
    public MatchDiagnostics MisMatched(String text, Object... args);

    public MatchDiagnostics Matched(SelfDescribing selfDescribing);

    public MatchDiagnostics MisMatched(SelfDescribing selfDescribing);
    
}
