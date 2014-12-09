package org.codemucker.jmatch;

import javax.management.RuntimeErrorException;


public class Expect {

    private boolean debugEnabled = false;
    private boolean showMatches = true;

    /**
     * Call this to set various debug options when running tests
     * E.g.
     * <pre>
     * Expect
     *  .with().debugEnabled(true).showMatches(false)
     *  .that(...)
     *  </pre> 
     */
    public static MatchOptions with() {
        return new Expect().newMatchOptions();
    }

    /**
     * E.g.
     * <pre>
     * Expect
     *  .that(foo).is(AFoo.with().name("Bob"))
     *  </pre>  
     */
    public static <T> ExpectAsserter<T> that(T actual) {
        return new Expect().newExpectAsserter(actual);
    }

    /**
     * E.g.
     * <pre>
     * Expect
     *  .that(foo).is(true)
     *  </pre> 
     */
    public static BoolExpectAsserter that(Boolean actual) {
        return new Expect().newBoolExpectAsserter(actual);
    }

    private MatchOptions newMatchOptions() {
        return new MatchOptions();
    }

    private <T> ExpectAsserter<T> newExpectAsserter(T actual) {
        return new ExpectAsserter<T>(actual);
    }

    private  BoolExpectAsserter newBoolExpectAsserter(Boolean actual) {
        return new BoolExpectAsserter(actual);
    }
    
    /**
     * Enable setting of various debug options in matchers
     */
    public class MatchOptions {

        public MatchOptions debugEnabled(boolean enabled) {
            debugEnabled = enabled;
            return this;
        }

        public MatchOptions showMatches(boolean enabled) {
            showMatches = enabled;
            return this;
        }

        public <T> ExpectAsserter<T> that(T actual) {
            return newExpectAsserter(actual);
        }

        public BoolExpectAsserter that(Boolean actual) {
            return newBoolExpectAsserter(actual);
        }
    }

    public class ExpectAsserter<T> extends MatchOptions {

        private final T actual;

        private ExpectAsserter(T actual) {
            this.actual = actual;
        }

        public boolean equals(Object ctexpectDoNotCallMeYouGotTheWrongMethod){
        	throw new RuntimeException("You probably want to call 'isEqualTo' instead");
        }
        
        public void isEqualTo(T expect) {
            is(AnInstance.equalTo(expect));
        }

        public void isNotNull() {
            is(AnInstance.notNull());
        }
        
        public void isNot(Matcher<? super T> matcher) {
            is(Logical.not(matcher));
        }
        
        public void is(Matcher<? super T> matcher) {
            // try fast diagnostics first. If fails rerun with a collecting one
            if (!matcher.matches(actual, NullMatchContext.INSTANCE)) {
                MatchDiagnostics diag = new DefaultMatchContext(debugEnabled, showMatches);
                matcher.matches(actual, diag);

                Description desc = diag.newDescription();
                desc.text("Assertion failed!");
                desc.child("expected", matcher);
                desc.child("but was", actual);
                desc.child("diagnostics", diag);

                throw new AssertionError(desc.toString() + "\n");
            }
        }
    }

    public class BoolExpectAsserter extends ExpectAsserter<Boolean> {
        private BoolExpectAsserter(Boolean actual) {
            super(actual);
        }

        public void is(boolean expect) {
            super.is(ABool.equalTo(expect));
        }

    }

}
