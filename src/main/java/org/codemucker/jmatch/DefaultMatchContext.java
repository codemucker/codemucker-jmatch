package org.codemucker.jmatch;

import java.util.Map;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;

public class DefaultMatchContext extends DefaultDescription implements MatchDiagnostics {

    private final boolean debugEnabled;
    private final boolean showMatches;

    public DefaultMatchContext(){
        this(false,true);
    }
    
    public DefaultMatchContext(boolean debugEnabled, boolean showMatches){
        this.debugEnabled = debugEnabled;
        this.showMatches = showMatches;
    }

    @Override
    public <T> boolean tryMatch(SelfDescribing parent,T actual, Matcher<T> matcher) {
        return internalTryMatch(parent,actual, matcher, null);
    }

    private <T> boolean internalTryMatch(SelfDescribing parent,T actual, Matcher<T> childMatcher, SelfDescribing selfDescribing) {
        // so we can print child diagnostics after
        DefaultMatchContext childDiag = newChild();
        boolean matched = childMatcher.matches(actual, childDiag);

        if(matched){
            if(showMatches){
                text("Match passed");
            }
        } else {
            text("Match FAILED");
        }
        
        DefaultDescription desc = DefaultDescription.with();
        if (!matched) {
            desc.child("on", actual);
            if(debugEnabled){
                desc .value("using matcher", childMatcher.getClass().getName());
            }
             desc
                 .child("expect", childMatcher)
                 .child(childDiag);
        } else {
            if(showMatches){
                desc.value(childMatcher);
            }
        }
        child(desc);
        return matched;
    }

    @Override
    public MatchDiagnostics matched(String text, Object... args) {
        matched(DefaultDescription.with().text(text, args));
        return this;
    }

    @Override
    public MatchDiagnostics mismatched(String text, Object... args) {
        mismatched(DefaultDescription.with().text(text, args));
        return this;
    }

    @Override
    public MatchDiagnostics matched(SelfDescribing selfDescribing) {
        if(showMatches){
            text("Match passed");
            child(selfDescribing);
        }
        return this;
    }

    @Override
    public MatchDiagnostics mismatched(SelfDescribing selfDescribing) {
        text("Match FAILED");
        child(selfDescribing);
        return this;
    }

    @Override
    public DefaultMatchContext newChild() {
        return new DefaultMatchContext();
    }

}
