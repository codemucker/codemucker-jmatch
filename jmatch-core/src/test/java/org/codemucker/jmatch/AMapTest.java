package org.codemucker.jmatch;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class AMapTest {

	@Test
	public void testValues(){
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
	public void testEmpty(){
		Map<String, String> have = new HashMap<>();
		
		Expect.that(have).is(AMap.ofStringString().withNothing());

		have.put("1", "one");

		Expect.that(have).isNot(AMap.ofStringString().withNothing());
	}
}
