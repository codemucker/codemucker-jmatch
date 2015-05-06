package org.codemucker.jmatch.expression.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.codemucker.jmatch.Matcher;
import org.codemucker.lang.annotation.ThreadSafe;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@ThreadSafe(caveats="provided no more calls to 'addMatcher' are made once the get* methods are called")
public class MatcherModels {

	private final ValueConverter converter = new ValueConverter();
	
	private final Map<String, MatcherModel> modelsByName = new HashMap<>();
	private final LoadingCache<Class<? extends Matcher<?>>, MatcherModel> modelsByClass = CacheBuilder.newBuilder()
	       .maximumSize(100)
	       .build(
	           new CacheLoader<Class<? extends Matcher<?>>, MatcherModel>() {
	             public MatcherModel load(Class<? extends Matcher<?>> matcherClass){
	               return new MatcherModel(matcherClass,converter);
	             }
	        });
	
	public synchronized void addMatcher(String matcherName,Class<? extends Matcher<?>> matcherClass){
		modelsByName.put(matcherName, getModelByClass(matcherClass));
	}
	
	public MatcherModel getModelByName(String name){
		return modelsByName.get(name);
	}
	
	public MatcherModel getModelByClass(Class<? extends Matcher<?>> matcherClass){
		try {
			return modelsByClass.get(matcherClass);
		} catch (ExecutionException e) {
			throw new RuntimeException("Error creating matcher model for " + matcherClass.getName(),e);
		}
	}
	
}
