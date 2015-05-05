package org.codemucker.jmatch.expression;

import java.util.ArrayList;
import java.util.List;

import org.codemucker.jmatch.AList;
import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.AbstractMatcher;
import org.codemucker.jmatch.Expect;
import org.codemucker.jmatch.MatchDiagnostics;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.PropertyMatcher;
import org.junit.Test;

public class ExpressionParser2Test {

	@Test
	public void parseGroups() throws Exception{

		MyTestMatcher2.called.clear();
		
		//() || ( )
		Matcher<MyObj> matcher = new BuilderMatcherParser().parse(AMyObj.class, "(foo=a && bar=b) || (foo=c && ( bar=d || bar=e ))");

		Expect
			.that(new MyObj("a","b"))
			.is(matcher);
	
		Expect
			.that(new MyObj("c","d"))
			.is(matcher);
		
		Expect
			.that(new MyObj("c","e"))
			.is(matcher);

		Expect
			.that(new MyObj("a","d"))
			.isNot(matcher);
	
		Expect
			.that(new MyObj("c","b"))
			.isNot(matcher);

	}

	public static class MyObj {
		public String foo;
		public String bar;

		
		public MyObj(String foo, String bar) {
			super();
			this.foo = foo;
			this.bar = bar;
		}

		public String getFoo() {
			return foo;
		}

		public void setFoo(String foo) {
			this.foo = foo;
		}

		public String getBar() {
			return bar;
		}

		public void setBar(String bar) {
			this.bar = bar;
		}
	}
	
	public static class AMyObj extends PropertyMatcher<MyObj> {

		public AMyObj() {
			super(MyObj.class);
		}

		public AMyObj foo(String val){
			matchProperty("foo",String.class, AString.equalTo(val));
			return this;
		}
		
		public AMyObj bar(String val){
			matchProperty("bar",String.class, AString.equalTo(val));
			return this;
		}
		
	}
	@Test
	public void parseAndMethodsCalled() throws Exception{

		MyTestMatcher2.called.clear();
		new BuilderMatcherParser().parse(MyTestMatcher2.class, "foo=123 && foo=-456 && !foo && bar && !foobar && mystring='!@#$%^&*()--+=:;<>,.?/|\\~' && foo > 1 && bar < 2 && foobar > 3");
		Expect
			.that(MyTestMatcher2.called)
			.is(AList.inOrder()
				.withOnly("foo:int:123")//converts to int
				.and("foo:int:-456")//converts to -int
				.and("isNotFoo")//on negate call the 'notX' first
				.and("isBar")//flag calls isX
				.and("foobar:boolean:false")//on negate calls X(false) if no 'notX'
				.and("mystring:string:!@#$%^&*()--+=:;<>,.?/|\\~")
				.and("foo>:int:1")
				.and("bar<:int:2")
				.and("foobar>:int:3"))
				;
	}

	public static class MyTestMatcher2 extends AbstractMatcher<String> {

		static List<String> called = new ArrayList<String>();

		@Override
		protected boolean matchesSafely(String actual, MatchDiagnostics diag) {
			return false;
		}
		
		public void foo(String val){
			called.add("foo:string:" + val);
		}
		
		public void foo(int val){
			called.add("foo:int:" + val);
		}
		
		public void isNotFoo(){
			called.add("isNotFoo");
		}
		
		public void isFoo(boolean val){
			called.add("isFoo:boolean:" + val);
		}
		
		public void isBar(){
			called.add("isBar");
		}
		
		public void foobar(boolean val){
			called.add("foobar:boolean:" + val);
		}
		
		public void mystring(String val){
			called.add("mystring:string:" + val);
		}
		
		public void greaterThanFoo(int val){
			called.add("foo>:int:" + val);
		}
		
		public void isLessThanBar(int val){
			called.add("bar<:int:" + val);
		}
		
		public void foobarIsGreaterThan(int val){
			called.add("foobar>:int:" + val);
		}
	}
}
