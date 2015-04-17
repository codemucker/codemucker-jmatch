package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class ALong { 

	public static Matcher<Long> equalTo(final Long require) {
		return new AbstractMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.longValue() == require.longValue();
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long equal to " + require);
			}
		};
	}

	public static Matcher<Long> greaterThan(final long require) {
		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found, MatchDiagnostics diag) {
				return found.longValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				//super.describeTo(desc);
				desc.text("a long > " + require);
			}
		};
	}

	public static Matcher<Long> greaterOrEqualTo(final long require) {
		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found, MatchDiagnostics diag) {
				return found.longValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				desc.text("a long >= " + require);
			}
		};
	}

	public static Matcher<Long> lessThan(final long require) {
		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found,MatchDiagnostics diag) {
				return found.longValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long < " + require);
			}
		};
	}

	public static Matcher<Long> lessOrEqualTo(final long require) {
		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found,MatchDiagnostics diag) {
				return found.longValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long <= " + require);
			}
		};
	}

	public static Matcher<Long> between(final long from, final long to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found,MatchDiagnostics diag) {
				long val = found.longValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Long> betweenExclusive(final long from, final long to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Long>() {
			@Override
			public boolean matchesSafely(Long found,MatchDiagnostics diag) {
				long val = found.longValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
