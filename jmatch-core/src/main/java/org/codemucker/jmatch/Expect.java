package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Expect {

	private boolean debugEnabled = false;
	private boolean showMatches = true;

	/**
	 * Call this to set various debug options when running tests E.g.
	 * 
	 * <pre>
	 * Expect
	 *  .with().debugEnabled(true).showMatches(false)
	 *  .that(...)
	 * </pre>
	 */
	public static MatchOptions with() {
		return new Expect().newMatchOptions();
	}

	/**
	 * E.g.
	 * 
	 * <pre>
	 * Expect.that(foo).is(AFoo.with().name(&quot;Bob&quot;))
	 * </pre>
	 */
	public static <T> ExpectAsserter<T> that(T actual) {
		return new Expect().newExpectAsserter(actual);
	}

	public static <T> ExpectAsserter<Iterable<T>> that(T[] actual) {
		if (actual == null) {
			return null;
		}
		Iterable<T> iter = new ArrayToIterable<T>(actual);
		return new Expect().newExpectAsserter(iter);
	}

	private static class ArrayToIterable<T> implements Iterable<T>, SelfDescribing {

		private final T[] array;
		private final List<T> list;

		public ArrayToIterable(T[] array) {
			super();
			this.array = array;
			if (array == null) {
				this.list = null;
			} else {
				this.list = new ArrayList<>(array.length);
				for (T item : array) {
					list.add(item);
				}
			}
		}

		@Override
		public Iterator<T> iterator() {
			return list.iterator();
		}

		@Override
		public String toString() {
			return Arrays.toString(array);
		}

		@Override
		public int hashCode() {
			return array.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return array.equals(obj);
		}

		@Override
		public void describeTo(Description desc) {
			if(array == null){
				desc.text("A null array");
			} else {
				desc.text("An array of length " + array.length + ", with items:");
				
				for(int i = 0;i < array.length;i++){
					desc.value("[" + i + "] =",array[i]);
				}
			}
		}

	}

	/**
	 * E.g.
	 * 
	 * <pre>
	 * Expect.that(foo).is(true)
	 * </pre>
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

	private BoolExpectAsserter newBoolExpectAsserter(Boolean actual) {
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

		public boolean equals(Object ctexpectDoNotCallMeYouGotTheWrongMethod) {
			throw new RuntimeException(
					"You probably want to call 'isEqualTo' instead");
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
				MatchDiagnostics diag = new DefaultMatchContext(debugEnabled,
						showMatches);
				matcher.matches(actual, diag);

				Description desc = diag.newDescription();
				desc.text("Assertion failed!");
				desc.child("expected", matcher);
				desc.child("but was", was(actual));
				desc.child("diagnostics", diag);

				throw new AssertionError(desc.toString() + "\n");
			}
		}
		
		private Object was(Object obj){
			if(obj != null && !(obj instanceof SelfDescribing)){
				if(obj.getClass().isArray()){
					Object[] arr = (Object[])obj;
					StringBuilder sb = new StringBuilder();
					sb.append("An array of length " + arr.length + ", with items:");
					for(int i = 0;i < arr.length;i++){
						sb.append("\n[" + i + "]=").append(arr[i]);
					}
					return sb.toString();
				} else if ( obj instanceof Collection ){
					Collection<?> col = (Collection<?>)obj;
					StringBuilder sb = new StringBuilder();
					int i = 0;
					sb.append("A collection of length " + col.size() + ", with items:");
					for(Iterator<?> iter = col.iterator();iter.hasNext();i++){
						sb.append("\n[" + i + "]=").append(iter.next());
					}
					return sb.toString();
				}
			}
			return obj;
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
