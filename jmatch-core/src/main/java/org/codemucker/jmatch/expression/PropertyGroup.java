package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.codemucker.jmatch.Matcher;

/**
 * Collects a number of operations possible on a property ('=','!=','<','<='...)
 */
public class PropertyGroup {
	private final ValueConverter converter;
	
	private final String propertyName;
	private Map<MatchOperator, MethodOperationGroup> methodGroups = new HashMap<>();
	
	public PropertyGroup(String propertyName,ValueConverter converter) {
		super();
		this.propertyName = propertyName;
		this.converter = converter;
	}
	
	//eg. ...static    or ...!static
	public void invokeWithNoArg(Matcher<?> matcher,boolean negate) throws Exception{
		if(!negate){
			MethodOperationGroup methods = methodGroups.get(MatchOperator.EQ);
			if(methods==null){
				throw new RuntimeException("no matcher method for " + MatchOperator.EQ + "'" + propertyName + "'");
			}
			methods.invokeWithNoArg(matcher);
		} else {
			//try 'not' methods first
			MethodOperationGroup methods = methodGroups.get(MatchOperator.NOT_EQ);
			if (methods != null) {
				methods.invokeWithNoArg(matcher);
				return;
			}
			methods = methodGroups.get(MatchOperator.EQ);
			if (methods != null) {
				methods.invokeWithArg(matcher, false);
				return;
			}
			
			throw new RuntimeException("no matcher method for " + MatchOperator.NOT_EQ + "'" + propertyName + "'");
		}
		
	}
	
	//eg.;;; static=true.... static!=true... name='build' ... numArgs=(2..4]
	public void invokeWithArg(Matcher<?> matcher,MatchOperator operator,Object val) throws Exception{
		MethodOperationGroup methods = methodGroups.get(operator);
		if(methods==null){
			throw new RuntimeException("no matcher method for '" + propertyName + "' " + operator);
			}
			methods.invokeWithArg(matcher, val);
		}

		public void addMethod(MatchOperator operator,Method m){
			getOrSet(operator).addMethod(m);
		}
		
		private MethodOperationGroup getOrSet(MatchOperator comparison){
			MethodOperationGroup group = methodGroups.get(comparison);
			if(group == null){
				group = new MethodOperationGroup(converter,comparison);
				methodGroups.put(comparison, group);
			}
			return group;
		}
}