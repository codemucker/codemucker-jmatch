package org.codemucker.jmatch;

import org.codemucker.jmatch.AbstractMatcher.AllowNulls;

public class Logical {

    /**
     * Always returns true for match. That is, it matches always
     */
	private static final Matcher<Object> MATCHER_ANY = new AbstractMatcher<Object>(AllowNulls.YES) {
		@Override
		public boolean matchesSafely(Object found,MatchDiagnostics ctxt) {
			return true;
		}
		
		public void describeTo(Description desc) {
			super.describeTo(desc);
			desc.text("anything");
		};
	};
	
	/**
	 * Always returns false for match. That is, it matches nothing
	 */
	private static final Matcher<Object> MATCHER_NONE = new AbstractMatcher<Object>(AllowNulls.YES) {
		@Override
		public boolean matchesSafely(Object found,MatchDiagnostics ctxt) {
			return false;
		}
		
		public void describeTo(Description desc) {
			desc.text("nothing");
		};
	};

	/**
     * Synonym for {@link #and(Matcher...)}
     */
	@SafeVarargs
    public static <T> Matcher<T> all(final Matcher<T>... matchers) {
    	return and(matchers);
    }

    public static <I extends Iterable<Matcher<T>>,T> Matcher<T> all(final I matchers) {
    	return and(matchers);
    }
   
    public static <I extends Iterable<Matcher<T>>,T> Matcher<T> and(final I matchers) {
    	return new MatchAllMatcher<T>(matchers);   
    }
    
    @SafeVarargs
	public static <T> Matcher<T> and(final Matcher<T>... matchers) {
    	return new MatchAllMatcher<T>(matchers);
    }

	/**
     * Synonym for {@link #or(Matcher...)}
     */
    @SafeVarargs
    public static <T> Matcher<T> either(final Matcher<T>... matchers) {
    	return or(matchers);
    }

    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> anyIfNull(Matcher<T> matcher) {
    	return matcher!=null?matcher:(Matcher<T>) MATCHER_ANY;
    }
    
    /**
     * Always returns true for match. That is, it matches everything
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> any() {
    	return (Matcher<T>) MATCHER_ANY;
    }
    
    /**
     * Always returns false for match. That is, it matches nothing
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T> none() {
    	return (Matcher<T>) MATCHER_NONE;
    }
    
    /**
     * Synonym for {@link #or(Matcher...)}
     */
    @SafeVarargs
    public static <T> Matcher<T> any(final Matcher<T>... matchers) {
    	return or(matchers);
    }
    
    public static <TList extends Iterable<Matcher<T>>,T> Matcher<T> any(final TList matchers) {
    	return or(matchers);
    }
    
    @SafeVarargs
	public static <T> Matcher<T> or(final Matcher<T>... matchers) {
    	return new MatchAnyMatcher<T>(matchers);
    }
	
	public static <TList extends Iterable<Matcher<T>>,T> Matcher<T> or(final TList matchers) {
    	return new MatchAnyMatcher<T>(matchers);
    }
	
	public static <T> Matcher<T> not(final Matcher<T> matcher) {
    	return new AbstractMatcher<T>(AllowNulls.YES) {
    		@Override
    		public boolean matchesSafely(T found, MatchDiagnostics ctxt) {
    			return !ctxt.tryMatch(this,found,matcher);
    		}
    		
    		@Override
    		public void describeTo(Description desc) {
    			//super.describeTo(desc);
    			desc.value("not", matcher);
    		};
    	};
    }
}
