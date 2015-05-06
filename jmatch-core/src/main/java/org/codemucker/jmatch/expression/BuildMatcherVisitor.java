package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.codemucker.jmatch.Logical;
import org.codemucker.jmatch.Matcher;
import org.codemucker.jmatch.expression.parser.Displayer;
import org.codemucker.jmatch.expression.parser.Rule_AFILTER;
import org.codemucker.jmatch.expression.parser.Rule_AND;
import org.codemucker.jmatch.expression.parser.Rule_ANTEXPR;
import org.codemucker.jmatch.expression.parser.Rule_ATTNAME;
import org.codemucker.jmatch.expression.parser.Rule_ATTVALEXPR;
import org.codemucker.jmatch.expression.parser.Rule_EQ;
import org.codemucker.jmatch.expression.parser.Rule_GCLOSE;
import org.codemucker.jmatch.expression.parser.Rule_GOPEN;
import org.codemucker.jmatch.expression.parser.Rule_GREATER;
import org.codemucker.jmatch.expression.parser.Rule_GREATER_EQ;
import org.codemucker.jmatch.expression.parser.Rule_INTVAL;
import org.codemucker.jmatch.expression.parser.Rule_LESS;
import org.codemucker.jmatch.expression.parser.Rule_LESS_EQ;
import org.codemucker.jmatch.expression.parser.Rule_MEXPR;
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

	private boolean negate;
	private final Stack<Grouping> groups = new Stack<>();
	
	private Stack<MatcherModel> mappingStack = new Stack<>();
	//private Stack<Matcher> matcherStack = new Stack<>();
	private MatcherModel currentModel;
	
	private String filterName;
	private FilterOperator filterOperator;
	private Object accumulatedFilterVal;
	
	private Matcher currentMatcher;

	private Map<String, Object> vars = new HashMap<>();
	
	private enum GroupOp {
		UNKNOWN,AND,OR
	}
	
	public BuildMatcherVisitor(Models models){
		this.models = models;
	}
	
	public BuildMatcherVisitor(Models models,Class<? extends Matcher<?>> matcherClass){
		this.models = models;
		pushModel(new MatcherModel(matcherClass));
	}

	public Matcher<?> getMatcher(){
		return currentMatcher;
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
	public Object visit(Rule_MEXPR rule) {
		startGrouping(currentModel);
		Object obj = super.visit(rule);
		endGrouping();
		return obj;
	}

	@Override
	public Object visit(Rule_GOPEN rule) {
		startGrouping(currentModel);
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_GCLOSE rule) {
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
			currentMatcher = group.toMatcher();
		} else {
			//add group we were in to the previous group
			groups.peek().add(group.toMatcher());
		}
	}
	@Override
	public Object visit(Rule_AFILTER rule) {
		resetFilter();
		
		Object o = super.visit(rule);
		//find the method to invoke to create the filter
		MethodFoundCallback callback = new MethodFoundCallback();
		if(accumulatedFilterVal != null){
			currentModel.findMethod(callback,filterName, filterOperator, accumulatedFilterVal);
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
			getOrCreateGrouping().add(newMatcher);
			//here we group the current matcher group an do an 'and'
		} else { //we're adding to existing matcher
			
			//e.g.  method[(name=get* || name=is*)]   vs method[name=get*||name=is*&&static]
  			//just adding to current matcher
			callback.invokeMethod(getOrCreateGrouping().getMatcher());
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
	public Object visit(Rule_ATTNAME rule) {
		filterName = rule.spelling.toLowerCase();			
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_ATTVALEXPR rule) {
		
		return super.visit(rule);
	}

	@Override
	public Object visit(Rule_VARNAME rule) {
		String varName = rule.spelling;
		if(!vars.containsKey(varName)){
			throw new RuntimeException("no variable name '" + varName + "'");
		}
		Object varVal = vars.get(varName);
		appendFilterVal(varVal);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_INTVAL rule) {
		int val = Integer.parseInt(rule.spelling);
		appendFilterVal(val);
		return super.visit(rule);
	};
	
	
	@Override
	public Object visit(Rule_ANTEXPR rule) {
		appendFilterVal(rule.spelling);
		return super.visit(rule);
	}
	
	@Override
	public Object visit(Rule_QVAL rule) {
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
		currentMatcher = null;
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
			throw new ExpressionParser.ParseException("Unexpected '" + op.name()	+ "', mixing logical operators without groupings is ambiguous. Use grouping to remove ambiguity");
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
		
		Exception error;
		Method method;
		Object[] args;
		
		MethodFoundCallback(){
		}
		
		public void foundFilterMethod(Method m, Object[] args){
			this.method = m;
			this.args = args;
		}

		private void log(String msg){
			System.out.println(getClass().getSimpleName() + ":" + msg);
		}

		@Override
		public void onError(String msg, Exception e) {
			onError(new RuntimeException(msg,e));
		}
		
		@Override
		public void onError(Exception e) {
			this.error = e;
		}

		public boolean hasError(){
			return this.error != null;
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

		public void throwIfError() throws RuntimeException {
			if(error!=null){
				if(error instanceof RuntimeException){
					throw (RuntimeException)error;
				} else {
					throw new RuntimeException("wrapped previous error",error);
				}
			}
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


}