package org.codemucker.jmatch;


public class Expect {

	public static <T> ExpectAsserter<T> that(T actual){
		return new ExpectAsserter<T>(actual);
	}
	
	public static BoolExpectAsserter that(Boolean actual){
		return new BoolExpectAsserter(actual);
	}
	
	public static class ExpectAsserter<T>
	{
		private final T actual;
		
		private ExpectAsserter(T actual){
			this.actual = actual;
		}
		
		public void isEqualTo(T expect){
			is(AnInstance.equalTo(expect));
		}
		
		public void is(Matcher<? super T> matcher){
			//try fast diagnostics first. If fails rerun with a collecting one
			if(!matcher.matches(actual, NullMatchContext.INSTANCE)){
				MatchDiagnostics diag = new DefaultMatchContext();
				matcher.matches(actual,diag);
				
				DefaultDescription desc = new DefaultDescription();
				desc.text("Assertion failed!");
				desc.value("expected", matcher);
				desc.value("but was", actual);
				desc.value("diagnostics", diag);
				
				throw new AssertionError(desc.toString() + "\n");
			}
		}
	}
	
	public static class BoolExpectAsserter extends ExpectAsserter<Boolean>
	{
		private BoolExpectAsserter(boolean actual){
			super(actual);
		}
		
		public void is(boolean expect){
			super.is(ABool.equalTo(expect));
		}
	
	}
}
