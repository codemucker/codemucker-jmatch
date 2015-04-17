package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class AChar { 

	public static Matcher<Character> equalTo(final Character require) {
		return new AbstractMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.charValue() == require.charValue();
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a char equal to " + require);
			}
		};
	}

	public static Matcher<Character> greaterThan(final char require) {
		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found, MatchDiagnostics diag) {
				return found.charValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				//super.describeTo(desc);
				desc.text("a char > " + require);
			}
		};
	}

	public static Matcher<Character> greaterOrEqualTo(final char require) {
		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found, MatchDiagnostics diag) {
				return found.charValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				desc.text("a char >= " + require);
			}
		};
	}

	public static Matcher<Character> lessThan(final char require) {
		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found,MatchDiagnostics diag) {
				return found.charValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a char < " + require);
			}
		};
	}

	public static Matcher<Character> lessOrEqualTo(final char require) {
		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found,MatchDiagnostics diag) {
				return found.charValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a char <= " + require);
			}
		};
	}

	public static Matcher<Character> between(final char from, final char to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found,MatchDiagnostics diag) {
				char val = found.charValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a char between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Character> betweenExclusive(final char from, final char to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Character>() {
			@Override
			public boolean matchesSafely(Character found,MatchDiagnostics diag) {
				char val = found.charValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a char between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
