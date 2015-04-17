package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class AFloat { 

	public static Matcher<Float> equalTo(final Float require) {
		return new AbstractMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.floatValue() == require.floatValue();
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a float equal to " + require);
			}
		};
	}

	public static Matcher<Float> greaterThan(final float require) {
		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found, MatchDiagnostics diag) {
				return found.floatValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				//super.describeTo(desc);
				desc.text("a float > " + require);
			}
		};
	}

	public static Matcher<Float> greaterOrEqualTo(final float require) {
		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found, MatchDiagnostics diag) {
				return found.floatValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				desc.text("a float >= " + require);
			}
		};
	}

	public static Matcher<Float> lessThan(final float require) {
		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found,MatchDiagnostics diag) {
				return found.floatValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a float < " + require);
			}
		};
	}

	public static Matcher<Float> lessOrEqualTo(final float require) {
		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found,MatchDiagnostics diag) {
				return found.floatValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a float <= " + require);
			}
		};
	}

	public static Matcher<Float> between(final float from, final float to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found,MatchDiagnostics diag) {
				float val = found.floatValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a float between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Float> betweenExclusive(final float from, final float to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Float>() {
			@Override
			public boolean matchesSafely(Float found,MatchDiagnostics diag) {
				float val = found.floatValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a float between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
