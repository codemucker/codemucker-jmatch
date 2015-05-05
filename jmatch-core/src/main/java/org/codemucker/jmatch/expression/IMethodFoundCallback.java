package org.codemucker.jmatch.expression;

import java.lang.reflect.Method;

public interface IMethodFoundCallback {
	public void foundFilterMethod(Method m, Object[] args);
	
	public void onError(Exception e);
	public void onError(String msg,Exception e);
	
}