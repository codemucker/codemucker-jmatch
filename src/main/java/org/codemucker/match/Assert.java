package org.codemucker.match;

public class Assert {

	public static <T> Matcher<T> isEqualTo(T expect){
		return AnInstance.equalTo(expect);
	}
	
	public static <T> void that(T actual,Matcher<T> matcher){
		assertThat(actual,matcher);
	}
	
	public static void assertThat(Boolean actual){
		assertThat(actual,isTrue());
	}
	
	public static <T> void assertThat(T actual,Matcher<T> matcher){
		Expect.that(actual).is(matcher);
	}
	
	public static Matcher<Boolean> isTrue(){
		return ABool.equalTo(true);
	}
	
	public static Matcher<Boolean> isFalse(){
		return ABool.equalTo(false);
	}
	
	public static <T> T is(T obj){
		return obj;
	}
	
	public static <T> T isA(T obj){
		return obj;
	}
	
	public static <T> Matcher<T> isNot(Matcher<T> matcher){
		return Logical.not(matcher);
	}
	
	public static <T> Matcher<T> not(Matcher<T> matcher){
		return Logical.not(matcher);
	}
	
	public void fail(String msg,Object... args){
		fail(String.format(msg, args));
	}

	public void fail(String msg){
		throw new AssertionError(msg == null ? "" : msg);
	}
}
