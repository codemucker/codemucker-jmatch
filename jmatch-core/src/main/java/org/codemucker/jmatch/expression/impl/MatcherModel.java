package org.codemucker.jmatch.expression.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.codemucker.jmatch.AbstractMatcher;
import org.codemucker.jmatch.Matcher;
import org.codemucker.lang.BeanNameUtil;

import com.google.common.base.Predicate;

class MatcherModel {
		
	private static final Set<String> IGNORE_METHODS = toNames(AbstractMatcher.class.getMethods());
	private final ValueConverter converter;
	
	private final Class<? extends Matcher<?>> matcherClass;
	private final Method factoryCreateMethod;
	
	private final Map<String, PropertyGroup> propertyGroupByName = new HashMap<>();
	
	public MatcherModel(Class<? extends Matcher<?>> matcherClass){
		this(matcherClass, new ValueConverter());
	}
	
	public MatcherModel(Class<? extends Matcher<?>> matcherClass,ValueConverter converter){
		this.matcherClass = matcherClass;
		this.converter = converter;
		
		factoryCreateMethod = findFirstOrNull(matcherClass, new Predicate<Method>() {

			@Override
			public boolean apply(Method m) {
				int mod = m.getModifiers();
				return Modifier.isStatic(mod) && Modifier.isPublic(mod) && m.getParameterTypes().length == 0 && m.getReturnType() != Void.class && ("with".equals(m.getName()) || "that".equals(m.getName()));
			}
		});
		
		List<Method> methods = findMethods(matcherClass, new Predicate<Method>() {

			@Override
			public boolean apply(Method m) {
				int mod = m.getModifiers();
				String nameLower = m.getName().toLowerCase();
				boolean match = !Modifier.isStatic(mod) && Modifier.isPublic(mod) && !Modifier.isNative(mod) && !Modifier.isAbstract(mod) && m.getParameterTypes().length <= 2 && !( nameLower.equals("with") || nameLower.equals("that"));
				
				return match;
			}
		});

		methods:for(Method m:methods){
			if(IGNORE_METHODS.contains(m.getName())){
				continue;
			}
			String name = m.getName().toLowerCase();
			//m.getAnnotation(PropertyAlias.class).operater()/names()
			for(FilterOperator op:FilterOperator.values()){
				if(op != FilterOperator.EQ){
					if(prefixMatches(m,name,op) || suffixMatches(m,name,op)){
						continue methods;
					}
				}
			}
			if(prefixMatches(m,name, FilterOperator.EQ) || suffixMatches(m,name, FilterOperator.EQ)){
				continue methods;
			}
			addMethod(name, FilterOperator.EQ, m);
		}
	}
	
	private static Set<String> toNames(Method[] methods){
		Set<String> names = new HashSet<>();
		for(Method m:methods){
			names.add(m.getName());
		}
		return names;
	}	
	
	private static Method findFirstOrNull(Class<?> klass,Predicate<Method> predicate){
		List<Method> methods = findMethods(klass,predicate);
		return methods.size()==0?null:methods.get(0);
	}
	
	private static List<Method> findMethods(Class<?> klass,Predicate<Method> predicate){
		List<Method> matched = new ArrayList<>();
		for(Method m: klass.getMethods()){
			if(predicate.apply(m)){
				matched.add(m);
			}
		}
		return matched;
	}
	
	private boolean prefixMatches(Method m, String lowerName, FilterOperator op){
		if (op.prefixes != null) {
			for (String prefix : op.prefixes) {
				if (tryStartsWith(prefix, m, lowerName, op)) {// e.g. isLessOrEqualToFoo
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean suffixMatches(Method m, String lowerName, FilterOperator op){
		if(op.suffixes!=null){
			for(String suffix:op.suffixes){
				if(tryEndsWith(suffix,m,lowerName, op)){//e.g. fooLessOrEqualToFoo
					return true;
				}
				if(tryEndsWith(suffix,m,"is" + lowerName, op)){//e.g. fooIsLessOrEqualTo
					return true;
				} 
			}
		}
		return false;
	}
	
	private boolean tryStartsWith(String startsWith, Method m, String lowerName, FilterOperator op){
		if(lowerName.startsWith(startsWith)){
			String propertyName = lowerName.substring(startsWith.length());
			addMethod(propertyName, op, m);
			return true;
		}
		return false;
	}

	private boolean tryEndsWith(String endsWith,Method m, String lowerName, FilterOperator op){
		if(lowerName.endsWith(endsWith)){
			String propertyName = lowerName.substring(0,lowerName.length() - endsWith.length());
			addMethod(propertyName, op, m);
			return true;
		}
		return false;
	}
	
	private void addMethod(String propertyName, FilterOperator op, Method m){
		log("adding method:" + m.getDeclaringClass().getSimpleName() + "." + m.getName() + "(" +  m.getParameterCount() +") for " + propertyName + " " + op);
		getOrCreatePropertyGroup(propertyName).addMethod(op, m);
	}

	public void findMethod(IMethodFoundCallback callback, String propertyName, boolean negate) {
		try {
			getPropertyGroup(propertyName).invokeWithNoArg(callback, negate);
		} catch (Exception e) {
			throw new RuntimeException("Error creating filter " + (negate?"!":"") + " '" + propertyName + "''",e);
		}
	}

	public void findMethod(IMethodFoundCallback callback, String propertyName, FilterOperator operator, Object methodVal){
		try{
			getPropertyGroup(propertyName).invokeWithArg(callback, operator, methodVal);
		} catch (Exception e) {
			throw new RuntimeException("Error creating filter  '" + propertyName + "' " + operator.symbol + " '" + methodVal + "'",e);
		}
	}
	
	public void findMethod(IMethodFoundCallback callback, String propertyName, FilterOperator operator, Object leftArg, Object rightArg){
		try{
			getPropertyGroup(propertyName).invokeWithArg(callback, operator, leftArg, rightArg);
		} catch (Exception e) {
			throw new RuntimeException("Error creating filter  '" + propertyName + "' " + operator.symbol + " '" + leftArg + "," + rightArg + "'",e);
		}
	}
	
	private PropertyGroup getPropertyGroup(String propertyName){
		propertyName = propertyName.toLowerCase();
		PropertyGroup group = propertyGroupByName.get(propertyName);
		
		if(group ==null){
			//isfoo->foo,
			group = propertyGroupByName.get(BeanNameUtil.stripPrefix(propertyName));
		}
		if(group == null){
			throw new RuntimeException("Couldn't find matching filter method for property '" + propertyName + "'");
		}
		return group;
	}
	
	private PropertyGroup getOrCreatePropertyGroup(String propertyName){
		propertyName = propertyName.toLowerCase();
		PropertyGroup group = propertyGroupByName.get(propertyName);
		
		if(group ==null){
			group = new PropertyGroup(propertyName,converter);
			propertyGroupByName.put(propertyName,group);
		}
		return group;
	}

	private void log(String msg){
		System.out.println( getClass().getSimpleName() + ":" + msg);
	}

	
	public Matcher<?> newMatcher() {
		if(factoryCreateMethod != null){	
			try {
				return (Matcher<?>) factoryCreateMethod.invoke(null, null);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new RuntimeException("could not create a new matcher instance calling " + matcherClass.getName() + "." + factoryCreateMethod.getName() + "()", e);
			}
		}
		try {
			return matcherClass.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("could not create a new matcher instance calling default constructor " + matcherClass.getName() + "()", e);
		}
	}
	
	
}