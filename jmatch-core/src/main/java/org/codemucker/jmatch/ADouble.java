package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class ADouble {

	public static Matcher<Double> equalTo(final Double require) {
		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.doubleValue() == require.doubleValue();
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long equal to " + require);
			}
		};
	}

	public static Matcher<Double> greaterThan(final Long require) {
		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found, MatchDiagnostics diag) {
				return found.doubleValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long > " + require);
			}
		};
	}

	public static Matcher<Double> greaterOrEqualTo(final Long require) {
		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found, MatchDiagnostics diag) {
				return found.doubleValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				desc.text("a long >= " + require);
			}
		};
	}

	public static Matcher<Double> lessThan(final Long require) {
		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found,MatchDiagnostics diag) {
				return found.doubleValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long < " + require);
			}
		};
	}

	public static Matcher<Double> lessOrEqualTo(final Long require) {
		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found,MatchDiagnostics diag) {
				return found.doubleValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long <= " + require);
			}
		};
	}

	public static Matcher<Double> between(final Long from, final Long to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found,MatchDiagnostics diag) {
				double val = found.doubleValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Double> betweenExclusive(final Long from, final Long to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Double>() {
			@Override
			public boolean matchesSafely(Double found,MatchDiagnostics diag) {
				double val = found.doubleValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a long between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
