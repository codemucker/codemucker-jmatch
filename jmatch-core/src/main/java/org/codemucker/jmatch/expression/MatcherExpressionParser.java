package org.codemucker.jmatch.expression;

import java.util.HashMap;
import java.util.Map;

import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.expression.impl.MatchBuilderVisitor;
import org.codemucker.jmatch.expression.impl.MatcherModels;
import org.codemucker.jmatch.expression.parser.Parser;
import org.codemucker.jmatch.expression.parser.Rule;

public class MatcherExpressionParser {
	
	private static final String RULE_MATCH_EXPRESSION = "mexpr";
	private static final String RULE_MATCHERS = "matchers";
	
	private final MatcherModels models = new MatcherModels();
	private final Map<String,Object> vars = new HashMap<>();

	public <T> Matcher<T> parse(String expression,Class<? extends Matcher<T>> matcherClass) throws ParseException {
		return parse(expression, matcherClass, vars);
	}
	/**
	 * Parse the given expression using the provided matcher class to build from
	 * 
	 * @param expression (e.g. 'name=x && static && (abstract || numArgs=>1)')
	 * @param matcherClass
	 * @return
	 * @throws Exception
	 */
	public <T> Matcher<T> parse(String expression,Class<? extends Matcher<T>> matcherClass,Map<String,Object> vars) throws ParseException {
		try {
			Rule rule = Parser.parse(RULE_MATCH_EXPRESSION, expression);
			MatchBuilderVisitor visitor = new MatchBuilderVisitor(vars,models,matcherClass);
	
			rule.accept(visitor);
			return (Matcher<T>) visitor.getMatcher();
		}
		catch(Exception e){
			throw new ParseException("Error parsing expression '" + expression + "' for matcher " + matcherClass.getName(),e);
		}
	}
	
	public Matcher<?> parse(String expression) throws ParseException {
		return parse(expression, vars);
	}
	/**
	 * Parse the given expression using named matchers 
	 * @param expression (e.g. 'method[name=...]')
	 * @return
	 * @throws Exception
	 */
	public Matcher<?> parse(String expression,Map<String,Object> vars) throws ParseException {
		try {
			Rule rule = Parser.parse(RULE_MATCHERS, expression);
			MatchBuilderVisitor visitor = new MatchBuilderVisitor(vars,models);
	
			rule.accept(visitor);
			return (Matcher<?>) visitor.getMatcher();
		}
		catch(Exception e){
			throw new ParseException("Error parsing expression '" + expression + "'",e);
		}
	}
	
	public void addMapping(String matcherName,Class<? extends Matcher<?>> matcherClass){
		models.addMatcher(matcherName, matcherClass);
	}

}