package org.codemucker.jmatch.expression;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.codemucker.jmatch.AList;
import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.AbstractMatcher;
import org.codemucker.jmatch.Expect;
import org.codemucker.jmatch.Logical;
import org.codemucker.jmatch.MatchDiagnostics;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.PropertyMatcher;
import org.junit.Test;

public class MatcherExpressionParserTest {

	@Test
	public void datesAndTimes() throws Exception{
		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("mydate=2013.02.08 15:14:13.123 GMT-0200", TestMatcher.class);
		Expect.that(TestMatcher.called).is(AList.withOnly("mydate:date:2013.02.08 T 17:14:13.123 GMT"));
	}
	
	@Test
	public void singleQuotedChars() throws Exception{
		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("mystring='!\"@#$%^&*()--+=:;<>,.?/|\\~'", TestMatcher.class);
		Expect.that(TestMatcher.called).is(AList.withOnly("mystring:string:!\"@#$%^&*()--+=:;<>,.?/|\\~"));
	}
	
	@Test
	public void doubleQuotedChars() throws Exception{
		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("mystring=\"!'@#$%^&*()--+=:;<>,.?/|\\~\"", TestMatcher.class);
		Expect.that(TestMatcher.called).is(AList.withOnly("mystring:string:!'@#$%^&*()--+=:;<>,.?/|\\~"));
	}
	
	@Test
	public void concatString() throws Exception{
		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("mystring='\"' + abc + '\"'", TestMatcher.class);
		Expect.that(TestMatcher.called).is(AList.withOnly("mystring:string:\"abc\""));
	}
	
	@Test
	public void ranges() throws Exception{
		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("myrange=5..8", TestMatcher.class);
		Expect.that(TestMatcher.called).is(AList.withOnly(AString.matchingExpression("myrange:between:*exclusive*5*>*val*<*8")));
	}

	@Test
	public void comparisonAndFlagFilters() throws Exception{

		TestMatcher.called.clear();
		new MatcherExpressionParser().parse("foo=123 && foo=-456 && !foo && bar && !foobar && mystring='!@#$%^&*()--+=:;<>,.?/|\\~' && foo > 1 && bar < 2 && foobar > 3", TestMatcher.class);
		Expect
			.that(TestMatcher.called)
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

	public static class TestMatcher extends AbstractMatcher<String> {

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
		
		public void mydate(Date date){
			String s = getFormatter().format(date);
			called.add("mydate:date:" + s);
		}
		
		private DateFormat getFormatter(){
			SimpleDateFormat df = new SimpleDateFormat("YYY.MM.dd 'T' HH:mm:ss.SSS 'GMT'");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			return df;
		}
		
		public void mydate(String date){
			called.add("mydate:string:" + date);
		}
		
		public void myrange(Matcher<Integer> matcher){
			called.add("myrange:between:" + matcher);
		}
	}
	

	@Test
	public void groups() throws Exception{

		TestMatcher.called.clear();
		
		Matcher<GroupBean> matcher = new MatcherExpressionParser().parse("(foo=a && bar=b) || ( foo=c  && (bar = d || bar=e ) )", GroupBeanMatcher.class);

		Expect.that(new GroupBean("a", "b")).is(matcher);
		Expect.that(new GroupBean("c", "d")).is(matcher);
		Expect.that(new GroupBean("c", "e")).is(matcher);
		Expect.that(new GroupBean("a", "d")).isNot(matcher);
		Expect.that(new GroupBean("c", "b")).isNot(matcher);

	}
	
	@Test
	public void groupsDeeplyNested() throws Exception{

		TestMatcher.called.clear();
		
		Matcher<GroupBean> matcher = new MatcherExpressionParser().parse("(foo=a && bar=b) || ( (foo=c || foo=d)  && (bar = d || (bar=e && foo=d && (bar!=z && foo!=x)) ) )", GroupBeanMatcher.class);

		Expect.that(new GroupBean("a", "b")).is(matcher);
		Expect.that(new GroupBean("c", "d")).is(matcher);
		Expect.that(new GroupBean("a", "d")).isNot(matcher);
		Expect.that(new GroupBean("c", "b")).isNot(matcher);

		
		Expect.that(new GroupBean("d", "e")).is(matcher);
		Expect.that(new GroupBean("d", "d")).is(matcher);
		Expect.that(new GroupBean("c", "e")).isNot(matcher);
	}

	public static class GroupBean {
		public String foo;
		public String bar;

		
		public GroupBean(String foo, String bar) {
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
	
	public static class GroupBeanMatcher extends PropertyMatcher<GroupBean> {

		public GroupBeanMatcher() {
			super(GroupBean.class);
		}

		public GroupBeanMatcher foo(String val){
			matchProperty("foo",String.class, AString.equalTo(val));
			return this;
		}
		
		public GroupBeanMatcher notFoo(String val){
			matchProperty("foo",String.class,Logical.not(AString.equalTo(val)));
			return this;
		}
		
		public GroupBeanMatcher bar(String val){
			matchProperty("bar",String.class, AString.equalTo(val));
			return this;
		}
		
		public GroupBeanMatcher notBar(String val){
			matchProperty("bar",String.class,Logical.not(AString.equalTo(val)));
			return this;
		}
		
	}
	
}
