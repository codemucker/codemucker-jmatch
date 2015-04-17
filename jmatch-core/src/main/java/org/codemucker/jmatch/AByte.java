package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

public class AByte { 

	public static Matcher<Byte> equalTo(final Byte require) {
		return new AbstractMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.byteValue() == require.byteValue();
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte equal to " + require);
			}
		};
	}

	public static Matcher<Byte> greaterThan(final byte require) {
		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found, MatchDiagnostics diag) {
				return found.byteValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				//super.describeTo(desc);
				desc.text("a byte > " + require);
			}
		};
	}

	public static Matcher<Byte> greaterOrEqualTo(final byte require) {
		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found, MatchDiagnostics diag) {
				return found.byteValue() >= require;
			}
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte >= " + require);
			}
		};
	}

	public static Matcher<Byte> lessThan(final byte require) {
		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found,MatchDiagnostics diag) {
				return found.byteValue() > require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte < " + require);
			}
		};
	}

	public static Matcher<Byte> lessOrEqualTo(final byte require) {
		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found,MatchDiagnostics diag) {
				return found.byteValue() <= require;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte <= " + require);
			}
		};
	}

	public static Matcher<Byte> between(final byte from, final byte to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found,MatchDiagnostics diag) {
				byte val = found.byteValue();
				return val >= from && val <= to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Byte> betweenExclusive(final byte from, final byte to) {
		checkArgument(from <= to, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Byte>() {
			@Override
			public boolean matchesSafely(Byte found,MatchDiagnostics diag) {
				byte val = found.byteValue();
				return val > from && val < to;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a byte between (exclusive) %d > val < %d", from, to);
			}
		};
	}
}
