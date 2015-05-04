package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.codemucker.jmatch.Matcher;

/**
 * groups a number of methods which all perform the same operation but may take different arguments or have different name (aliases)
 */
public class MethodOperationGroup {
	private static final Object[] NO_ARGS = new Object[]{};
	private static final Object[] TRUE_ARG = new Object[]{};
	private static final Object[] FALSE_ARG = new Object[]{};
	
	private final ValueConverter converter;
	private final MatchOperator operator;
	private final List<MethodWrapper> methods = new ArrayList<>();
	
	public MethodOperationGroup(ValueConverter converter,MatchOperator operator){
		this.operator = operator;
		this.converter = converter;
	}
	
	void addMethod(Method m){
		//TODO:extract alias methods??
		//m.getAnnotation(PropertyAlias.class)
		methods.add(new MethodWrapper(converter,m));
	}
	

	public void invokeWithNoArg(Matcher<?> matcher) throws Exception {
		checkArgCount(0);
		switch(operator){
			case EQ:
				if(!tryInvokeWithNoArg(matcher) && !tryInvokeWithArgs(matcher, TRUE_ARG)){
					throwNoOperator();
				}
				break;
			case NOT_EQ:
				if(!tryInvokeWithNoArg(matcher) && !tryInvokeWithArgs(matcher, FALSE_ARG)){
					throwNoOperator();
				}
				break;
			default:
				throw new RuntimeException("Don't support invoking operator '" + operator + "' with no args");
		}
	}
	
	public void invokeWithArg(Matcher<?> matcher, Object val) throws Exception {
		checkArgCount(1);
		if(!tryInvokeWithArgs(matcher, new Object[]{val})){
			throwNoOperator();
		}
	}

	public void invokeWithArgs(Matcher<?> matcher, Object leftVal, Object rightVal) throws Exception {
		checkArgCount(2);
		if(!tryInvokeWithArgs(matcher, new Object[]{leftVal,rightVal})){
			throwNoOperator();
		}
	}
	
	private void throwNoOperator() {
		throw new RuntimeException("no method to invoke for operator " + operator);
	}

	private void checkArgCount(int numArgs){
		if( operator.minArgs > numArgs){
			throw new RuntimeException("expect atleast " + operator.minArgs + " for operator " + operator);
		}
		if( operator.maxArgs < numArgs){
			throw new RuntimeException("expect at most " + operator.maxArgs + " for operator " + operator);
		}
	}
	
	private boolean tryInvokeWithNoArg(Matcher<?> matcher) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(!m.hasArgs()){
				m.invokeExact(matcher, NO_ARGS);
				return true;
			}
		}
		return false;
	}

	private boolean tryInvokeWithArgs(Matcher<?> matcher, Object[] args) throws Exception {
		if(tryInvokeWithExactArg(matcher, args)){
			return true;
		}
		return tryInvokeWithConvertedArg(matcher,args);
	}

	private boolean tryInvokeWithExactArg(Matcher<?> matcher,Object[] args) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(m.hasArgs() && m.matchArgTypesExact(args)){ //exact match
				m.invokeExact(matcher, args);
				return true;
			}
		}
		return false;
	}
	
	private boolean tryInvokeWithConvertedArg(Matcher<?> matcher,Object[] args) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(m.hasArgs() && m.matchArgTypesCanConvert(args)){ //exact match
				m.invokeConverted(matcher, args);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Wraps a method to provide some convenience methods
	 */
	private static class MethodWrapper {
		private final ValueConverter converter;
		private final Method method;
		
		public MethodWrapper(ValueConverter converter,Method m){
			this.method = m;
			this.converter = converter;
		}

		public boolean hasArgs(){
			return method.getParameterTypes().length > 0;
		}
		
		public boolean matchArgTypesExact(Object[] args){
			Class<?>[] paramTypes = method.getParameterTypes();
			if(args.length != paramTypes.length){
				return false;
			}
			for(int i = 0 ; i < args.length;i++){
				if(!argMatchesExactly(args[i], paramTypes[i])){
					return false;
				}
			}
			return true;
		}
		
		public Object invokeExact(Object matcher,Object[] args) throws Exception{
			return invoke(matcher, args);
		}
		
		public Object invokeConverted(Object matcher,Object[] args) throws Exception {
			Class<?>[] paramTypes = method.getParameterTypes();
			Object[] converted  = new Object[args.length];
			for(int i = 0; i < args.length;i++){
				converted[i] = converter.convertTo(args[i], paramTypes[i]);
			}
			return invoke(matcher, converted);
		}
		
		
		private Object invoke(Object matcher,Object[] args) throws Exception{
			log("invoking:" + matcher.getClass().getName() + "." + method.getName() + "(" + argsToString(args) + ")");
			return method.invoke(matcher, args);
		}
		
		
		public boolean matchArgTypesCanConvert(Object[] args){
			Class<?>[] paramTypes = method.getParameterTypes();
			if(args.length != paramTypes.length){
				return false;
			}
			for(int i = 0 ; i < args.length;i++){
				if(!converter.canConvert(args[i], paramTypes[i])){
					return false;
				}
			}
			return true;
		}
		
		private boolean argMatchesExactly(Object from, Class<?> to) {
			if(from == null){
				return to == String.class;
			}
			return ClassUtils.isAssignable(from.getClass(), to);
		}
		
		private static String argsToString(Object[] args){
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

		
		private void log(String msg){
			System.out.println(getClass().getSimpleName() + ":" + msg);
		}


	}
}