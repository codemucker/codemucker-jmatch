package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class AnInt { //extends Logical {

	public static Matcher<Integer> equalTo(final int require) {
		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found,MatchDiagnostics diag) {
				return found.intValue() == require;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int equal to " + require);
			}
		};
	}

	public static Matcher<Integer> greaterThan(final int require) {
		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found, MatchDiagnostics diag) {
				return found.intValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int > " + require);
			}
		};
	}

	public static Matcher<Integer> greaterOrEqualTo(final int require) {
		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found, MatchDiagnostics diag) {
				return found.intValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int >= " + require);
			}
		};
	}

	public static Matcher<Integer> lessThan(final int require) {
		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found,MatchDiagnostics diag) {
				return found.intValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int < " + require);
			}
		};
	}

	public static Matcher<Integer> lessOrEqualTo(final int require) {
		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found,MatchDiagnostics diag) {
				return found.intValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int <= " + require);
			}
		};
	}

	public static Matcher<Integer> between(final int from, final int to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found,MatchDiagnostics diag) {
				int val = found.intValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Integer> betweenExclusive(final int from, final int to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Integer>() {
			@Override
			public boolean matchesSafely(Integer found,MatchDiagnostics diag) {
				int val = found.intValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an int between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
