package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Date;

public class ADate { 

	public static Matcher<Date> equalTo(final Date require) {
		final long requireTime = require==null?-1:require.getTime();
		return new AbstractMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found,MatchDiagnostics diag) {
				if(found == null){
					return require == null;
				}
				if(require == null){
					return false;
				}
				return found.getTime() == requireTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date equal to " + require);
			}
		};
	}

	public static Matcher<Date> greaterThan(final Date require) {
		final long requireTime = require.getTime();
		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found, MatchDiagnostics diag) {
				return found.getTime() > requireTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date > " + require);
			}
		};
	}

	public static Matcher<Date> greaterOrEqualTo(final Date require) {
		final long requireTime = require.getTime();
		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found, MatchDiagnostics diag) {
				return found.getTime() >= requireTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date >= " + require);
			}
		};
	}

	public static Matcher<Date> lessThan(final Date require) {
		final long requireTime = require.getTime();
		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found,MatchDiagnostics diag) {
				return found.getTime() > requireTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date < " + require);
			}
		};
	}

	public static Matcher<Date> lessOrEqualTo(final Date require) {
		final long requireTime = require.getTime();
		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found,MatchDiagnostics diag) {
				return found.getTime() <= requireTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date <= " + require);
			}
		};
	}

	public static Matcher<Date> between(final Date from, final Date to) {
		final long fromTime = from.getTime();
		final long toTime = to.getTime();		
		checkArgument(fromTime <= toTime, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found,MatchDiagnostics diag) {
				long have = found.getTime();
				return have >= fromTime && have <= toTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date between (inclusive) %d >= val <= %d", from, to);
			}
		};
	}
	
	public static Matcher<Date> betweenExclusive(final Date from, final Date to) {
		final long fromTime = from.getTime();
		final long toTime = to.getTime();		
		checkArgument(fromTime <= toTime, "Expect 'from' to be <= 'to'");

		return new AbstractNotNullMatcher<Date>() {
			@Override
			public boolean matchesSafely(Date found,MatchDiagnostics diag) {
				long have = found.getTime();
				return have > fromTime && have < toTime;
			}
			
			@Override
			public void describeTo(Description desc) {
				desc.text("a Date between (exclusive) %d > val < %d", from.getTime(), to.getTime());
			}
		};
	}
}
