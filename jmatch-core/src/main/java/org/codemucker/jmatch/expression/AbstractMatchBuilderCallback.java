package org.codemucker.jmatch.expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.codemucker.jmatch.Logical;
import org.codemucker.jmatch.Matcher;

public abstract class AbstractMatchBuilderCallback<T> implements ExpressionParser.ParseCallback<Matcher<T>> {

	private final Stack<Grouping> groups = new Stack<>();
	private boolean nextIsNeg;
	private Matcher<T> matcher;

	@Override
	public void onStart() {
		onToken("START");
		groups.clear();
		nextIsNeg = false;
		matcher = null;
		groups.push(new Grouping(OP.UNKNOWN, false));
	}

	public Matcher<T> build() {
		return matcher;
	}

	@Override
	public void onEnd() {
		onToken("END");
		while (!groups.isEmpty()) {
			Grouping group = groups.pop();
			matcher = group.toMatcher();
			if (!groups.isEmpty()) {
				groups.peek().add(matcher);
			}
		}
	}

	
	@Override
	public void onExpression(String expression) {
		onToken("<" + expression + ">");
		add(newMatcher(expression,nextIsNeg));
	}
	
	/**
	 * Calls {@link #newMatcher(String)} and wraps it in a {@link Logical#not(Matcher)} if is negated. Override if providing custom negation of an expression (for efficiency)
	 * 
	 * @param negate
	 * @param expression
	 * @return
	 */
	protected Matcher<T> newMatcher(String expression,boolean negate){
		matcher = newMatcher(expression);
		if(nextIsNeg){
			matcher = Logical.not(matcher );
		}
		nextIsNeg = false;
		return matcher;
	}

	/**
	 * Subclasses should override this to create a matcher based on the given expression. Called by {@link #newMatcher(boolean, String)}
	 * 
	 * @param expression
	 * @return
	 */
	protected abstract Matcher<T> newMatcher(String expression);

	@Override
	public void onNegate() {
		onToken("!");
		failIfPreviousNegate();
		nextIsNeg = true;
	}

	@Override
	public void onOR() {
		onToken(" OR ");
		failIfPreviousNegate();
		setGroupOp(OP.OR);
	}

	@Override
	public void onAND() {
		onToken(" AND ");
		failIfPreviousNegate();
		setGroupOp(OP.AND);
	}

	private void setGroupOp(AbstractMatchBuilderCallback.OP op) {
		Grouping g = getOrCreateGrouping();
		if (g.op == OP.UNKNOWN) {
			g.op = op;
		} else if (g.op != op) {
			throw new ExpressionParser.ParseException("Unexpected '" + op.tokens	+ "', mixing logical operators without groupings is ambiguous (previous operator ' " + g.op.tokens + "'). Use grouping to remove ambiguity");
		}
	}
	
	private void failIfPreviousNegate(){
		if(nextIsNeg){
			throw new ExpressionParser.ParseException("Unexpected token after '!'");
		}
	}

	@Override
	public void onStartGroup() {
		onToken(" ( ");
		groups.push(new Grouping(OP.UNKNOWN, nextIsNeg));
		nextIsNeg = false;
	}

	@Override
	public void onEndGroup() {
		onToken(" ) ");
		failIfPreviousNegate();
		Grouping group = groups.pop();
		if(groups.isEmpty()){
			matcher = group.toMatcher();
		} else {
			groups.peek().add(group.toMatcher());
		}
	}

	private void add(Matcher<T> matcher) {
		nextIsNeg = false;
		getOrCreateGrouping().add(matcher);
	}
	
	private Grouping getOrCreateGrouping(){
		if (groups.isEmpty()) {
			groups.push(new Grouping(OP.UNKNOWN, false));
		}
		return groups.peek();
	}

	protected void onToken(String msg) {
		//System.out.println(msg);
	}

	private class Grouping {

		private AbstractMatchBuilderCallback.OP op;
		private boolean negate;
		private List<Matcher<T>> matchers = new ArrayList<>();

		public Grouping(AbstractMatchBuilderCallback.OP op, boolean negate) {
			super();
			this.op = op;
			this.negate = negate;
		}

		void add(Matcher<T> matcher) {
			matchers.add(matcher);
		}

		Matcher<T> toMatcher() {
			Matcher<T> matcher;
			if (matchers.size() == 0) {
				matcher = Logical.none();
			} else if (matchers.size() == 1) {
				matcher = matchers.get(0);
			} else {
				if (op == OP.AND) {
					matcher = Logical.all(matchers);
				} else if (op == OP.OR) {
					matcher = Logical.any(matchers);
				} else {
					throw new ExpressionParser.ParseException(
							"Don't know what grouping to convert to ");
				}
			}

			if (negate) {
				matcher = Logical.not(matcher);
			}
			return matcher;
		}
	}

	private static enum OP {

		UNKNOWN(null),OR("||"), AND("&&");

		public final String tokens;

		OP(String tokens) {
			this.tokens = tokens;
		}

	}

}