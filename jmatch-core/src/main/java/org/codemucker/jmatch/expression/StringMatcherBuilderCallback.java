package org.codemucker.jmatch.expression;

import org.codemucker.jmatch.AString;
import org.codemucker.jmatch.Matcher;

public class StringMatcherBuilderCallback extends AbstractMatchBuilderCallback<String> {

	@Override
	protected Matcher<String> newMatcher(String expression) {
		return AString.matchingAntPattern(expression);
	}
	
}