package org.codemucker.jmatch.expression;

import java.util.HashMap;
import java.util.Map;

import org.codemucker.jmatch.Matcher;

public class Models {

	private Map<String, MatcherModel> maps = new HashMap<>();

	public void addMapping(String matcherName,Class<? extends Matcher<?>> matcherClass){
		maps.put(matcherName, new MatcherModel(matcherClass));
	}
	
	public MatcherModel getModel(String name){
		return maps.get(name);
	}
}
