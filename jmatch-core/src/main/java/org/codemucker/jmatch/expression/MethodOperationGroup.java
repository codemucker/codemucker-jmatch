package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;

/**
 * groups a number of methods which all perform the same operation but may take different arguments or have different name (aliases)
 */
public class MethodOperationGroup {
	private static final Object[] NO_ARGS = new Object[]{};
	private static final Object[] TRUE_ARG = new Object[]{};
	private static final Object[] FALSE_ARG = new Object[]{};
	
	private final ValueConverter converter;
	private final FilterOperator operator;
	private final List<MethodWrapper> methods = new ArrayList<>();
	
	public MethodOperationGroup(ValueConverter converter,FilterOperator operator){
		this.operator = operator;
		this.converter = converter;
	}
	
	void addMethod(Method m){
		//TODO:extract alias methods??
		//m.getAnnotation(PropertyAlias.class)
		methods.add(new MethodWrapper(converter,m));
	}


	public void invokeWithNoArg(IMethodFoundCallback filter) throws Exception {
		checkArgCount(0);
		switch(operator){
			case EQ:
				if(!tryInvokeWithNoArg(filter) && !tryInvokeWithArgs(filter,TRUE_ARG)){
					throwNoOperator();
				}
				break;
			case NOT_EQ:
				if(!tryInvokeWithNoArg(filter) && !tryInvokeWithArgs(filter,FALSE_ARG)){
					throwNoOperator();
				}
				break;
			default:
				throw new RuntimeException("Don't support invoking operator '" + operator + "' with no args");
		}
	}
	
	public void invokeWithArg(IMethodFoundCallback filter,Object val) throws Exception {
		checkArgCount(1);
		if(!tryInvokeWithArgs(filter,new Object[]{val})){
			throwNoOperator();
		}
	}

	public void invokeWithArgs(IMethodFoundCallback filter,Object leftVal, Object rightVal) throws Exception {
		checkArgCount(2);
		if(!tryInvokeWithArgs(filter, new Object[]{leftVal,rightVal})){
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
	
	private boolean tryInvokeWithNoArg(IMethodFoundCallback filter) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(!m.hasArgs()){
				m.invokeExact(filter, NO_ARGS);
				return true;
			}
		}
		return false;
	}

	private boolean tryInvokeWithArgs(IMethodFoundCallback filter,Object[] args) throws Exception {
		if(tryInvokeWithExactArg(filter,args)){
			return true;
		}
		return tryInvokeWithConvertedArg(filter,args);
	}

	private boolean tryInvokeWithExactArg(IMethodFoundCallback filter,Object[] args) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(m.hasArgs() && m.matchArgTypesExact(args)){ //exact match
				m.invokeExact(filter, args);
				return true;
			}
		}
		return false;
	}
	
	private boolean tryInvokeWithConvertedArg(IMethodFoundCallback filter,Object[] args) throws Exception {
		for(MethodOperationGroup.MethodWrapper m:this.methods){
			if(m.hasArgs() && m.matchArgTypesCanConvert(args)){ //exact match
				m.invokeConverted(filter, args);
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
		final Method method;
		
		public MethodWrapper(ValueConverter converter,Method m){
			this.method = m;
			this.converter = converter;
		}

		public boolean isInstance(){
			return !isStatic();
		}
		
		public boolean isStatic(){
			return Modifier.isStatic(method.getModifiers());
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
		
		public void invokeExact(IMethodFoundCallback filter, Object[] args) throws Exception{
			invoke(filter, args);
		}
		
		public void invokeConverted(IMethodFoundCallback filter, Object[] args) throws Exception {
			Class<?>[] paramTypes = method.getParameterTypes();
			Object[] converted  = new Object[args.length];
			for(int i = 0; i < args.length;i++){
				converted[i] = converter.convertTo(args[i], paramTypes[i]);
			}
			invoke(filter, converted);
		}

		private void invoke(IMethodFoundCallback filter,Object[] args) throws Exception{
			filter.foundFilterMethod(method, args);
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

	}
}