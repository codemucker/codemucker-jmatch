package org.codemucker.jmatch;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.junit.Test;

public class AMapTest {

	@Test
	public void withOnly(){
		Map<String, String> have = new HashMap<>();
		have.put("1", "one");
		have.put("2", "two");
		
		Expect.that(have).is(AMap.inAnyOrder().withOnly("1", "one").and("2", "two"));
		Expect.that(have).is(AMap.inAnyOrder().withOnly("1", "one").andValue("two"));
		Expect.that(have).is(AMap.inAnyOrder().withOnly("1", "one").andKey("2"));
		Expect.that(have).is(AMap.inAnyOrder().withOnly("1",AString.equalToAnything()).andKey("2"));
		
		Expect.that(have).isNot(AMap.withOnly("1", "one"));
		Expect.that(have).isNot(AMap.withOnly("2", "two"));
		Expect.that(have).isNot(AMap.inAnyOrder().withOnly("1", "two").and("2", "one"));

	}
	
	@Test
	public void inOrderWithOnly(){
		Map<String, String> have = new TreeMap<>();
		have.put("1", "one");
		have.put("2", "two");
		have.put("3", "three");
		
		Expect.that(have).is(AMap.inOrder().withOnly("1", "one").and("2", "two").and("3", "three"));
		Expect.that(have).is(AMap.ofStringString().inOrder().withOnly().andKey("1").and("2", "two").and("3", "three"));
		
		Expect.that(have).is(AMap.inOrder().withOnly("1", "one").andKey("2").andValue("three"));
		
		Expect.that(have).isNot(AMap.inOrder().withOnly("1", "one").and("3", "three").and("2", "two"));
	}
	
	@Test
	public void inOrderWithAtLeast(){
		Map<String, String> have = new TreeMap<>();
		have.put("1", "one");
		have.put("2", "two");
		have.put("3", "three");
		
		Expect.that(have).is(AMap.inOrder().withAtLeast("1", "one").and("3", "three"));
		Expect.that(have).is(AMap.inOrder().withAtLeast("1", "one").andKey("3"));
		
		Expect.that(have).isNot(AMap.inOrder().withAtLeast("3", "three").and("1", "one"));
		
	}
	
			
	
	@Test
	public void testEmpty(){
		Map<String, String> have = new HashMap<>();
		
		Expect.that(have).is(AMap.ofStringString().withNothing());

		have.put("1", "one");

		Expect.that(have).isNot(AMap.ofStringString().withNothing());
	}
}
