package org.codemucker.jmatch.expression.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.codemucker.jmatch.AnInt;
import org.codemucker.jmatch.Logical;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.expression.ParseException;
import org.codemucker.jmatch.expression.parser.Displayer;
import org.codemucker.jmatch.expression.parser.Rule_AND;
import org.codemucker.jmatch.expression.parser.Rule_EQ;
import org.codemucker.jmatch.expression.parser.Rule_GREATER;
import org.codemucker.jmatch.expression.parser.Rule_GREATER_EQ;
import org.codemucker.jmatch.expression.parser.Rule_LESS;
import org.codemucker.jmatch.expression.parser.Rule_LESS_EQ;
import org.codemucker.jmatch.expression.parser.Rule_NEG;
import org.codemucker.jmatch.expression.parser.Rule_NOT;
import org.codemucker.jmatch.expression.parser.Rule_NOT_EQ;
import org.codemucker.jmatch.expression.parser.Rule_NUM;
import org.codemucker.jmatch.expression.parser.Rule_OR;
import org.codemucker.jmatch.expression.parser.Rule_afilter;
import org.codemucker.jmatch.expression.parser.Rule_antexpr;
import org.codemucker.jmatch.expression.parser.Rule_attname;
import org.codemucker.jmatch.expression.parser.Rule_attvalexpr;
import org.codemucker.jmatch.expression.parser.Rule_datetime;
import org.codemucker.jmatch.expression.parser.Rule_gclose;
import org.codemucker.jmatch.expression.parser.Rule_gopen;
import org.codemucker.jmatch.expression.parser.Rule_mexpr;
import org.codemucker.jmatch.expression.parser.Rule_mtype;
import org.codemucker.jmatch.expression.parser.Rule_qval;
import org.codemucker.jmatch.expression.parser.Rule_range;
import org.codemucker.jmatch.expression.parser.Rule_rfrom;
import org.codemucker.jmatch.expression.parser.Rule_rto;
import org.codemucker.jmatch.expression.parser.Rule_varname;
import org.codemucker.jmatch.expression.parser.Terminal_NumericValue;
import org.codemucker.jmatch.expression.parser.Terminal_StringValue;

public class MatchBuilderVisitor extends Displayer {

	private static final DateFormats DATE_FORMATS = new DateFormats();
	
	private final MatcherModels models;

	private boolean negate;
	private final Stack<Grouping> groups = new Stack<>();
	
	private Stack<MatcherModel> mappingStack = new Stack<>();
	//private Stack<Matcher> matcherStack = new Stack<>();
	private MatcherModel currentModel;
	
	private String filterName;
	private FilterOperator filterOperator;
	private Object accumulatedFilterVal;
	
	private Matcher builtMatcher;
	
	private final Map<String, Object> vars;
	
	private enum GroupOp {
		UNKNOWN,AND,OR
	}
	
	public MatchBuilderVisitor(Map<String, Object> vars,MatcherModels models){
		this.vars = vars;
		this.models = models;
	}
	
	public MatchBuilderVisitor(Map<String, Object> vars,MatcherModels models,Class<? extends Matcher<?>> matcherClass){
		this.vars = vars;
		this.models = models;
		pushModel(models.getModelByClass(matcherClass));
	}

	public Matcher<?> getMatcher(){
		return builtMatcher;
	}

	public void addVar(String name,Object val){
		vars.put(name,val);
	}

	@Override
	public Object visit(Rule_mtype rule) {
		String matcherName = rule.spelling.toLowerCase();
		MatcherModel mapping = models.getModelByName(matcherName);
		if(mapping == null){
			throw new RuntimeException("no matcher for:" + matcherName);
		}
		
		pushModel(mapping);
		Object o = super.visit(rule);
		popModel();
		return o;
	}

	private void pushModel(MatcherModel mapping){
		this.currentModel = mappingStack.push(mapping);
	}

	private void popModel(){
		currentModel = mappingStack.pop();
	}
	
	@Override
	public Object visit(Rule_mexpr rule) {
		startGrouping(currentModel);
		Object obj = super.visit(rule);
		endGrouping();
		return obj;
	}

	@Override
	public Object visit(Rule_gopen rule) {
		startGrouping(currentModel);
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_gclose rule) {
		//end group
		endGrouping();
		return super.visit(rule);
	}

	private void startGrouping(MatcherModel model){
		groups.push(new Grouping(model,GroupOp.UNKNOWN, getAndClearNegate()));
	}
	
	private void endGrouping(){
		Grouping group = groups.pop();
		if(groups.isEmpty()){
			builtMatcher = group.toMatcher();
		} else {
			//add group we were in to the previous group
			groups.peek().add(group.toMatcher());
		}
	}
	@Override
	public Object visit(Rule_afilter rule) {
		resetFilter();
		
		Object o = super.visit(rule);
		//find the method to invoke to create the filter
		MethodFoundCallback callback = new MethodFoundCallback();
		if(accumulatedFilterVal != null){
			if(accumulatedFilterVal instanceof Range){
				Range range = (Range)accumulatedFilterVal;
				//HACK:need to handle all numbers, not just ints
				currentModel.findMethod(callback,filterName, filterOperator, AnInt.betweenExclusive(range.from.intValue(), range.to.intValue()));
			} else {
				currentModel.findMethod(callback,filterName, filterOperator, accumulatedFilterVal);
			}
		} else {
			currentModel.findMethod(callback,filterName,negate);
		}
		negate = false;
		//TODO:::::::::::  fix the mess below!!!!!::::::::::::::::::::
		//method[(name=get|name=is)]
		if(callback.isStaticMethod()){//we're creating a new matcher, so AND/OR it with the current matcher
			
			Object obj = callback.invokeMethod(null);
			if(!(obj instanceof Matcher)){
				throw new RuntimeException("Expect to return a matcher for statc method:" + callback.describeMethod());
			}
			Matcher newMatcher = (Matcher)obj;
			GroupOp currentOp = getOrCreateGrouping().op;
			startGrouping(currentModel);
			setGroupOp(currentOp);
			getOrCreateGrouping().currentMatcher = newMatcher;
			endGrouping();
		} else { //we're adding to existing matcher
			//just adding to current matcher
			
			GroupOp currentOp = getOrCreateGrouping().op;
			if(currentOp == GroupOp.AND){
				callback.invokeMethod(getOrCreateGrouping().getMatcher());
			} else {
				//have to start a group for it
				startGrouping(currentModel);
				setGroupOp(currentOp);
				callback.invokeMethod(getOrCreateGrouping().getMatcher());
				endGrouping();
			}
		}
		resetFilter();
		return o;
	}
	
	private void resetFilter(){
		//log("reset-filter");
		accumulatedFilterVal = null;
		negate = false;
		filterOperator = FilterOperator.EQ;
	}

	@Override
	public Object visit(Rule_attname rule) {
		filterName = rule.spelling.toLowerCase();			
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_attvalexpr rule) {
		
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_varname rule) {
		String varName = rule.spelling;
		if(!vars.containsKey(varName)){
			throw new RuntimeException("no variable name '" + varName + "'");
		}
		Object varVal = vars.get(varName);
		appendFilterVal(varVal);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_NUM rule) {
		appendFilterVal(parseNumber(rule.spelling));
		return super.visit(rule);
	};

	@Override
	public Object visit(Rule_datetime rule) {
		//force yyy/MM/dd separators to '.'
		
		Date d = DATE_FORMATS.parse(rule.spelling);
		if( d == null){
			throw new RuntimeException("Could not parse date '" + rule.spelling + "'. No matching format found. Tried " + DATE_FORMATS);
		}
		appendFilterVal(d);
		
		return null; //no point recursing
		//return super.visit(rule);
	}

	@Override
	public Object visit(Rule_range rule) {
		accumulatedFilterVal = new Range();
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_rfrom rule) {
		((Range)accumulatedFilterVal).from = parseNumber(rule.spelling);
		return null;
	}

	@Override
	public Object visit(Rule_rto rule) {
		((Range)accumulatedFilterVal).to = parseNumber(rule.spelling);
		return null;
	}
	
	private Number parseNumber(String s){
		if( s.length() > 0){
			char endsWith = Character.toLowerCase(s.charAt(s.length()-1));
			switch (endsWith) {
			case 'f':
				return Float.parseFloat(s);
			case 'l':
				return Long.parseLong(s);
			case 'd':
				return Double.parseDouble(s);
			default:
				return Integer.parseInt(s);
			}
		}
		return 0;
	}
	
	static class Range {
		Number from;
		Number to;
	}
	
	@Override
	public Object visit(Rule_antexpr rule) {
		appendFilterVal(rule.spelling);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_qval rule) {
		String val = rule.spelling;
		//remove quotes
		appendFilterVal(val.substring(1, val.length()-1));
		return super.visit(rule);
	}

	private void appendFilterVal(Object val){
		if (accumulatedFilterVal == null) {
			accumulatedFilterVal = val == null ? null : val;
		} else {
			accumulatedFilterVal = accumulatedFilterVal + "" + (val == null ? null : val);
		}
	}
	
	@Override
	public Object visit(Rule_OR rule) {
		builtMatcher = null;
		setGroupOp(GroupOp.OR);
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_AND rule) {
		setGroupOp(GroupOp.AND);
		return super.visit(rule);
	}

	private void setGroupOp(GroupOp op) {
		Grouping g = getOrCreateGrouping();
		if (g.op == GroupOp.UNKNOWN) {
			g.op = op;
		} else if (g.op != op) {
			throw new ParseException("Unexpected '" + op.name()	+ "', mixing logical operators without groupings is ambiguous. Use grouping to remove ambiguity");
		}
	}

	private Grouping getOrCreateGrouping(){
		if (groups.isEmpty()) {
			groups.push(new Grouping(currentModel,GroupOp.UNKNOWN, getAndClearNegate()));
		}
		return groups.peek();
	}
	
	private boolean getAndClearNegate(){
		try{
			return negate;
		} finally {
			negate = false;
		}
	}

	@Override
	public Object visit(Rule_NOT rule) {
		negate = true;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_NOT_EQ rule) {
		filterOperator = FilterOperator.NOT_EQ;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_GREATER rule) {
		filterOperator = FilterOperator.GREATER;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_GREATER_EQ rule) {
		filterOperator = FilterOperator.GREATER_EQ;
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_LESS rule) {
		filterOperator = FilterOperator.LESS;
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_LESS_EQ rule) {
		filterOperator = FilterOperator.LESS_EQ;
		return super.visit(rule);
	}
	
	
	@Override
	public Object visit(Rule_EQ rule) {
		filterOperator = FilterOperator.EQ;
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
	
	
	class MethodFoundCallback implements IMethodFoundCallback {
		
		Method method;
		Object[] args;
		
		MethodFoundCallback(){}
		
		public void foundFilterMethod(Method m, Object[] args){
			this.method = m;
			this.args = args;
		}

		private void log(String msg){
			System.out.println(getClass().getSimpleName() + ":" + msg);
		}

		public Object invokeMethod(Matcher matcher){
			log("invoking:" + describeMethod());
			try {
				return method.invoke(matcher, args);
			} catch(Exception e){
				throw new RuntimeException("error invoking method " + describeMethod(),e);
			}
		}
		
		public boolean isStaticMethod(){
			return Modifier.isStatic(method.getModifiers());
		}

		public String describeMethod(){
			return method.getDeclaringClass().getName() + ":" + method.toGenericString() + ": with args:(" + argsToString(args) + ")" ;
		}
		
		private String argsToString(Object[] args){
			String s = "";
			if(args!= null){
				boolean comma = false;
				for(Object x:args){
					if(comma){
						s += ",";
					}
					comma = true;
					s += x;	
				}
			}
			return s;
		}

	}
	
	private class Grouping {

		private GroupOp op;
		private boolean negate;
		private List<Matcher<Object>> matchers = new ArrayList<>();
		private Matcher currentMatcher;
		private MatcherModel model;
		
		public Grouping(MatcherModel model, GroupOp op, boolean negate) {
			super();
			this.op = op;
			this.negate = negate;
			this.model = model;
			//TODO:
		}
		
		public Matcher getMatcher(){
			if(currentMatcher == null){
				currentMatcher = model.newMatcher();
				add(currentMatcher);
			}
			return currentMatcher;
		}

		void add(Matcher<Object> matcher) {
			matchers.add(matcher);
			currentMatcher = matcher;
		}

		Matcher toMatcher() {
			Matcher matcher;
			if (matchers.size() == 0) {
				matcher = Logical.none();
			} else if (matchers.size() == 1) {
				matcher = matchers.get(0);
			} else {
				if (op == GroupOp.AND) {
					matcher = Logical.all(matchers);
				} else if (op == GroupOp.OR) {
					matcher = Logical.any(matchers);
				} else {
					throw new ParseException(
							"Don't know what grouping to convert to ");
				}
			}

			if (negate) {
				matcher = Logical.not(matcher);
			}
			return matcher;
		}
	}


}