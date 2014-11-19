package org.codemucker.jmatch;

import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.AnInt;
import org.codemucker.jmatch.PropertyMatcher;
import org.junit.Test;


public class DynamicMatcherTest {

	@Test
	public void Match(){
		PropertyMatcher<TestBean> matcher = new PropertyMatcher<>(TestBean.class);
		
		matcher.matchProperty("stringProp", String.class, AString.equalTo("mystring"));
		matcher.matchProperty("intProp", int.class, AnInt.equalTo(5));
		
		TestBean bean = new TestBean();
		bean.setStringProp("mystring");
		bean.setIntProp(5);
		
		org.junit.Assert.assertTrue(matcher.matches(bean));
	}
	
	@Test
	public void DontMatch(){
		PropertyMatcher<TestBean> matcher = new PropertyMatcher<>(TestBean.class);
		
		matcher.matchProperty("stringProp", String.class, AString.equalTo("otherstring"));
		
		TestBean bean = new TestBean();
		bean.setStringProp("mystring");
		
		org.junit.Assert.assertFalse(matcher.matches(bean));
	}
	
	class TestBean {
		private String stringProp;
		private int intProp;
		
		public String getStringProp() {
			return stringProp;
		}
		public void setStringProp(String stringProp) {
			this.stringProp = stringProp;
		}
		public int getIntProp() {
			return intProp;
		}
		public void setIntProp(int intProp) {
			this.intProp = intProp;
		}
	}
}
