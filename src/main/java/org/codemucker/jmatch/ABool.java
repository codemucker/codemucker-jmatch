package org.codemucker.jmatch;

import org.codemucker.jmatch.Description;
import org.codemucker.jmatch.MatchDiagnostics;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.AbstractMatcher.AllowNulls;

public class ABool {

	public static final Matcher<Boolean> isTrue(){
		return equalTo(true);
	}
	
	public static final Matcher<Boolean> isFalse(){
		return equalTo(false);
	}
	
	public static final Matcher<Boolean> equalTo(final Boolean expect){
		return new AbstractMatcher<Boolean>(AllowNulls.YES) {
			@Override
			public boolean matchesSafely(Boolean found, MatchDiagnostics ctxt) {
				if( expect == null && found == null){
					return true;
				}
				if( expect == null || found == null){
					return false;
				}
				return expect.equals(found);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a bool equal to " + Boolean.valueOf(expect));
			}
		};
	}
}
