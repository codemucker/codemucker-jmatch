package org.codemucker.jmatch;

public interface MatchDiagnostics extends Description {
	
	public <T> boolean tryMatch(SelfDescribing parent,T actual, Matcher<T> matcher);
	
	public MatchDiagnostics matched(String text, Object... args);
	
    public MatchDiagnostics mismatched(String text, Object... args);

    public MatchDiagnostics matched(SelfDescribing selfDescribing);

    public MatchDiagnostics mismatched(SelfDescribing selfDescribing);
    
    public MatchDiagnostics newChild();
    
}
