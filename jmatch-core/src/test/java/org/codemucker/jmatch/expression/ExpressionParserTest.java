package org.codemucker.jmatch.expression;

import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.Description;
import org.codemucker.jmatch.Expect;
import org.codemucker.jmatch.MatchDiagnostics;
import org.codemucker.jmatch.Matcher;
import org.junit.Test;

public class ExpressionParserTest {

	@Test
	public void smokeTest() {

		Matcher<String> m = ExpressionParser.parse(
				"alice && bob && !( NOT nick || ( 'sam && jim' OR (kate AND joe ) )) && sue",
				new MyMatcherBuilderCallback());
		
		String actual = m.toString();
		String expect = "" +
		"matching all:" +
		"	alice,"+
		"    bob,"+
		"   not "+
		"      matching any:"+
		"            not "+
		"               nick"+
		"           ,"+
		"           matching any:"+
		"               sam && jim,"+
		"               matching all:"+
		"                   kate,"+
		"                   joe"+
		"   , sue";
		    
		Expect.that(actual).is(AString.equalToIgnoreWhiteSpace(expect));
	}
	
	@Test
	public void bug_startsWithGroup_throwsStackEmptyExceptionTest(){
		ExpressionParser.parse("(MyGroup) && SomethingElse", new StringMatcherBuilderCallback());
	}
	
	@Test
	public void bug_startsWithGroup_missingFirstGroupTest(){
		String actual = ExpressionParser.parse("(MyGroup | MyGroup2 ) && SomethingElse", new MyMatcherBuilderCallback()).toString();
		String expect = "" +
				"matching all:" +
				"	MyGroup | MyGroup2,"+
				"    SomethingElse";
				    
		Expect.that(actual).is(AString.equalToIgnoreWhiteSpace(expect));
	}
	
	private static class MyMatcherBuilderCallback extends AbstractMatchBuilderCallback<String> {

		@Override
		protected Matcher<String> newMatcher(String expression) {
			return new MyMatcher(expression);
		}
		
		@Override
		protected void onToken(String msg) {
			System.out.println(msg);
		}
	}

	private static class MyMatcher implements Matcher<String> {

		private final String expect;

		MyMatcher(String expect) {
			this.expect = expect;
		}

		@Override
		public void describeTo(Description desc) {
			desc.value(expect);
		}

		@Override
		public boolean matches(String actual, MatchDiagnostics ctxt) {
			throw new RuntimeException("Operation not supported");
		}

		@Override
		public boolean matches(String actual) {
			throw new RuntimeException("Operation not supported");
		}

	}

}
