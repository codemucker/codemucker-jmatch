package org.codemucker.jmatch.expression;

import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.expression.parser.Parser;
import org.codemucker.jmatch.expression.parser.Rule;

public class BuilderMatcherParser {
	
	private Models models = new Models();

	public <T> Matcher<T> parse(Class<? extends Matcher<T>> matcherClass,String expression) throws Exception {
		Rule rule = Parser.parse("EXPR", expression);
		BuildMatcherVisitor visitor = new BuildMatcherVisitor(models,matcherClass);

		rule.accept(visitor);
		return (Matcher<T>) visitor.getMatcher();
	}
	
	public void addMapping(String matcherName,Class<? extends Matcher<?>> matcherClass){
		models.addMapping(matcherName, matcherClass);
	}

}