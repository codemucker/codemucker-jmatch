package org.codemucker.jmatch;

import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.DefaultDescription;
import org.codemucker.jmatch.DefaultMatchContext;
import org.codemucker.jmatch.MatchDiagnostics;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.NullMatchContext;
import org.junit.Test;

import static org.codemucker.jmatch.Assert.*;

public class AStringTest {

	@Test
	public void testAntPattern(){
		assertNotThat(null,is(AString.matchingAntPattern("")));
		assertThat("",is(AString.matchingAntPattern("")));
		
		assertNotThat(null,is(AString.matchingAntPattern("*a?")));
		assertNotThat("",is(AString.matchingAntPattern("*a?")));
		assertThat("a",is(AString.matchingAntPattern("*a?")));
		assertThat("aaa",is(AString.matchingAntPattern("*a?")));
		assertThat("xax",is(AString.matchingAntPattern("*a?")));
		assertThat("xxax",is(AString.matchingAntPattern("*a?")));
		assertThat("xxa",is(AString.matchingAntPattern("*a?")));
		assertNotThat("xxb",is(AString.matchingAntPattern("*a?")));
		
	}
	
	public static <T> void assertNotThat(T actual,Matcher<T> matcher){
		//try fast diagnostics first. If fails rerun with a collecting one
		if(matcher.matches(actual, NullMatchContext.INSTANCE)){
			MatchDiagnostics diag = new DefaultMatchContext();
			matcher.matches(actual,diag);
			
			DefaultDescription desc = new DefaultDescription();
			desc.text("Expect that Assertion fails but didn't!");
			desc.value("matcher", matcher);
			desc.value("actual", actual);
			desc.value("diagnostics", diag);
			
			throw new AssertionError(desc.toString() + "\n");
		}
	}
}
