package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Joiner;

/**
 * Collects a number of operations possible on a property ('=','!=','<','<='...)
 */
public class PropertyGroup {
	private final ValueConverter converter;
	
	private final String propertyName;
	private Map<FilterOperator, MethodOperationGroup> methodGroups = new HashMap<>();
	
	public PropertyGroup(String propertyName,ValueConverter converter) {
		super();
		this.propertyName = propertyName;
		this.converter = converter;
	}
	
	//eg. ...static    or ...!static
	public void invokeWithNoArg(IMethodFoundCallback callback,boolean negate) throws Exception{
		if(!negate){
			MethodOperationGroup methods = methodGroups.get(FilterOperator.EQ);
			throwIfNull(methods,FilterOperator.EQ);
			methods.matchWithNoArg(callback);
		} else {
			//try 'not' methods first
			MethodOperationGroup methods = methodGroups.get(FilterOperator.NOT_EQ);
			if (methods != null) {
				methods.matchWithNoArg(callback);
				return;
			}
			methods = methodGroups.get(FilterOperator.EQ);
			if (methods != null) {
				methods.matchWithArg(callback, false);
				return;
			}
			
			throwIfNull(methods,FilterOperator.NOT_EQ);
		}
		
	}
	
	//eg.;;; static=true.... static!=true... name='build' ... numArgs=(2..4]
	public void invokeWithArg(IMethodFoundCallback callback,FilterOperator operator,Object val) throws Exception {
		MethodOperationGroup methods = methodGroups.get(operator);
		throwIfNull(methods,operator);
		methods.matchWithArg(callback, val);
	}

	private void throwIfNull(MethodOperationGroup group, FilterOperator op){
		if(group==null){
			throw new RuntimeException("no matcher method for '" + propertyName + " " + op.symbol + "'. Have methods [" + Joiner.on(",").join(methodGroups.values()) + "]");
		}
	}
	
	public void addMethod(FilterOperator operator,Method m){
		getOrSet(operator).addMethod(m);
	}
	
	private MethodOperationGroup getOrSet(FilterOperator comparison){
		MethodOperationGroup group = methodGroups.get(comparison);
		if(group == null){
			group = new MethodOperationGroup(converter,comparison);
			methodGroups.put(comparison, group);
		}
		return group;
	}
}