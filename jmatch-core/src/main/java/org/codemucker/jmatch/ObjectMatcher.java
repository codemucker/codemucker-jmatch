package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.List;

import org.codemucker.jmatch.expression.MatcherExpressionParser;

import com.google.common.base.Predicate;

/***
 * Matcher to collect a list of matchers for a given object type. Passes if no matchers fail
 *
 * @param <T>
 */
public class ObjectMatcher<T> extends AbstractMatcher<T> {

	private final List<Matcher<T>> matchers = new ArrayList<>();
	private final Class<T> expectType;
	private static final MatcherExpressionParser parser = new MatcherExpressionParser();
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public ObjectMatcher(Class expectType){
		super(AllowNulls.NO);
		this.expectType = (Class<T>) expectType;
	}
	
	protected Class<T> getExpectType(){
	    return expectType;
	}

	@Override
	public boolean matchesSafely(T actual, MatchDiagnostics ctxt) {
		//TODO:could turn straight into array
		for(Matcher<T> matcher : matchers){
			if(!ctxt.tryMatch(this, actual, matcher)){
				return false;
			}
		}
		return true;
	}
	
	
	public void withExpression(String expression){
		Class<? extends Matcher<T>> matcherClass = (Class<? extends Matcher<T>>) getClass();
		Matcher<T> matcher = parser.parse(expression, matcherClass);
		addMatcher(matcher);
	}
	
	/**
	 * Match on the given types predicate
	 * 
	 * E.g
	 * 
	 * AFoo.with().type(f=>f.getName()=="Alice" || f.getName()=="Bob")
	 * @param predicate
	 * @return
	 */
	protected void predicate(Predicate<T> predicate){
		addMatcher(PredicateToMatcher.from(predicate));
	}
	
	protected void addMatcher(Matcher<T> matcher){
		matchers.add(matcher);
	}
	
	@Override
	public void describeTo(Description desc) {
	    if(desc.isNull()){
	        return;
	    }
	   // super.describeTo(desc);
	    desc.text((isAllowNull()?"nullable":"non null") + " type " + expectType.getName());
	    for(Matcher<T> matcher : matchers){
	        desc.value("with",matcher);
	    }
	}
	
	/**
	 * Return all the currently collected matchers
	 * 
	 * @return
	 */
	protected Iterable<Matcher<T>> getMatchers(){
		return matchers;
	}
}
