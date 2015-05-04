package org.codemucker.jmatch.expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.expression.parser.Displayer;
import org.codemucker.jmatch.expression.parser.Rule_AND;
import org.codemucker.jmatch.expression.parser.Rule_ANTEXPR;
import org.codemucker.jmatch.expression.parser.Rule_ATTNAME;
import org.codemucker.jmatch.expression.parser.Rule_EQ;
import org.codemucker.jmatch.expression.parser.Rule_EXPR;
import org.codemucker.jmatch.expression.parser.Rule_FILTER;
import org.codemucker.jmatch.expression.parser.Rule_GCLOSE;
import org.codemucker.jmatch.expression.parser.Rule_GOPEN;
import org.codemucker.jmatch.expression.parser.Rule_GREATER;
import org.codemucker.jmatch.expression.parser.Rule_GREATER_EQ;
import org.codemucker.jmatch.expression.parser.Rule_INTVAL;
import org.codemucker.jmatch.expression.parser.Rule_LESS;
import org.codemucker.jmatch.expression.parser.Rule_LESS_EQ;
import org.codemucker.jmatch.expression.parser.Rule_MTYPE;
import org.codemucker.jmatch.expression.parser.Rule_NEG;
import org.codemucker.jmatch.expression.parser.Rule_NOT;
import org.codemucker.jmatch.expression.parser.Rule_NOT_EQ;
import org.codemucker.jmatch.expression.parser.Rule_OR;
import org.codemucker.jmatch.expression.parser.Rule_QVAL;
import org.codemucker.jmatch.expression.parser.Rule_VARNAME;
import org.codemucker.jmatch.expression.parser.Terminal_NumericValue;
import org.codemucker.jmatch.expression.parser.Terminal_StringValue;

class BuildMatcherVisitor extends Displayer {

	private final Models models;

	private MatchOperator operator;
	private Object methodVal;

	private MatcherModel mapping;
	private Matcher<?> matcher;
	private boolean negate;
	private Stack<MatcherModel> mappingStack = new Stack<>();
	private Stack<Matcher<?>> matcherStack = new Stack<>();
	private Stack<Group> groups = new Stack<>();
	
	private Map<String, Object> vars = new HashMap<>();
	
	
	Addition addition = Addition.AND;

	private String methodName;
	
	private enum Addition {
		AND,OR
	}
	public BuildMatcherVisitor(Models models){
		this.models = models;
		newGroup();
	}
	
	public BuildMatcherVisitor(Models models,Class<? extends Matcher<?>> matcherClass){
		this.models = models;
		newGroup();
		pushMatcher(new MatcherModel(matcherClass));
	}
	
	private void newGroup(){
		if(!groups.isEmpty()){
		//	Group current = groups.peek();
		}
		groups.push(new Group());
	}

	public void addVar(String name,Object val){
		vars.put(name,val);
	}

	@Override
	public Object visit(Rule_MTYPE rule) {
		String matcherName = rule.spelling.toLowerCase();
		MatcherModel mapping = models.getModel(matcherName);
		if(mapping == null){
			throw new RuntimeException("no matcher for:" + matcherName);
		}
		
		pushMatcher(mapping);
		Object o = super.visit(rule);
		popMatcher();
		return o;
	}
	
	public Matcher<?> getMatcher(){
		return matcher;
	}
	
	private void pushMatcher(MatcherModel mapping){
		this.matcher = mapping.newMatcher();
		this.mapping = mapping;
		this.mappingStack.push(mapping);
		this.matcherStack.push(this.matcher);
	}

	private void popMatcher(){
		mapping = mappingStack.pop();
		matcher = matcherStack.pop();
	}
	
	@Override
	public Object visit(Rule_EXPR rule) {
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_FILTER rule) {
		resetFilter();
		Object o = super.visit(rule);
		if(methodVal != null){
			mapping.filter(matcher,methodName,operator, methodVal);
		} else {
			mapping.filter(matcher,methodName,negate);
		}
		resetFilter();
		return o;
	}
	
	@Override
	public Object visit(Rule_ATTNAME rule) {
		methodName = rule.spelling.toLowerCase();			
		return super.visit(rule);
	}
	
	private void resetFilter(){
		//log("reset-filter");
		methodVal = null;
		negate = false;
		operator = MatchOperator.EQ;
	}

	@Override
	public Object visit(Rule_VARNAME rule) {
		String varName = rule.spelling;
		if(!vars.containsKey(varName)){
			throw new RuntimeException("no variable name '" + varName + "'");
		}
		Object varVal = vars.get(varName);
		appendVal(varVal);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_INTVAL rule) {
		int val = Integer.parseInt(rule.spelling);
		appendVal(val);
		return super.visit(rule);
	};
	
	
	@Override
	public Object visit(Rule_ANTEXPR rule) {
		appendVal(rule.spelling);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_QVAL rule) {
		String val = rule.spelling;
		//remove quotes
		appendVal(val.substring(1, val.length()-1));
		return super.visit(rule);
	}

	private void appendVal(Object val){
		if (methodVal == null) {
			methodVal = val == null ? null : val;
		} else {
			methodVal = methodVal + "" + (val == null ? null : val);
		}
	}
	
	//[public && (static && name=get* || name=set)]
	
	@Override
	public Object visit(Rule_OR rule) {
		if(addition != Addition.OR){
			//add matcher to new group
			
		}
		
		//
		addition = Addition.OR;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_AND rule) {
		if(addition != Addition.AND){
			//add matcher to new group
			//matcher = 
		}
		addition = Addition.AND;
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_GOPEN rule) {
		//start group
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_GCLOSE rule) {
		//end group
		return super.visit(rule);
	}
	
	static class Group {
		private List<Matcher> matchers = new ArrayList<>();
		
		void and(Matcher<?> matcher){
			
		}

		void or(Matcher<?> matcher){
			
		}

	}

	static class OrList {
		
	}

	@Override
	public Object visit(Rule_NOT rule) {
		negate = true;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_NOT_EQ rule) {
		operator = MatchOperator.NOT_EQ;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_GREATER rule) {
		operator = MatchOperator.GREATER;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_GREATER_EQ rule) {
		operator = MatchOperator.GREATER_EQ;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_LESS rule) {
		operator = MatchOperator.LESS;
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_LESS_EQ rule) {
		operator = MatchOperator.LESS_EQ;
		return super.visit(rule);
	}
	
	
	@Override
	public Object visit(Rule_EQ rule) {
		operator = MatchOperator.EQ;
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_NEG rule) {
		return super.visit(rule);
	}

	@Override
	public Object visit(Terminal_StringValue value) {
		return null;//super.visit(value);
	}

	@Override
	public Object visit(Terminal_NumericValue value) {
		return null;//super.visit(value);
	}


}