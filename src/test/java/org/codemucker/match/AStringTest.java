package org.codemucker.match;

import org.junit.Test;
import static org.codemucker.match.Assert.*;

public class AStringTest {

	@Test
	public void testAntPattern(){
		assertNotThat(null,is(AString.withAntPattern("")));
		assertThat("",is(AString.withAntPattern("")));
		
		assertNotThat(null,is(AString.withAntPattern("*a?")));
		assertNotThat("",is(AString.withAntPattern("*a?")));
		assertThat("a",is(AString.withAntPattern("*a?")));
		assertThat("aaa",is(AString.withAntPattern("*a?")));
		assertThat("xax",is(AString.withAntPattern("*a?")));
		assertThat("xxax",is(AString.withAntPattern("*a?")));
		assertThat("xxa",is(AString.withAntPattern("*a?")));
		assertNotThat("xxb",is(AString.withAntPattern("*a?")));
		
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
