package org.codemucker.jmatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PropertyMatcher<T> extends ObjectMatcher<T> {

	private static final Object[] NoArgs = new Object[]{};
	
	private final Class<T> beanClass;
	
	private static final Map<Class<?>,Class<?>> autoBoxedConversions = new HashMap<>();
	
	static {
		autoBoxed(boolean.class, Boolean.class);
		autoBoxed(byte.class, Byte.class);
		autoBoxed(short.class, Short.class);
		autoBoxed(char.class, Character.class);
		autoBoxed(int.class, Integer.class);
		autoBoxed(float.class, Float.class);
		autoBoxed(double.class, double.class);
		autoBoxed(long.class, Long.class);
	}
	
	private static void autoBoxed(Class<?> from, Class<?> to){
		autoBoxedConversions.put(from, to);
		autoBoxedConversions.put(to, from);
	}
	
	/**
	 * @param beanClass the class this matcher is for
	 */
	public PropertyMatcher(Class<T> beanClass){
		this.beanClass = beanClass;
	}
	
	protected <TProperty> void withProperty(String propertyName, Class<TProperty> expectedPropertyType, Matcher<TProperty> matcher){
		Method getter = getGetterOrNull("get" + firstToUpper(propertyName));
		if( getter == null ){
			getter = getGetterOrNull(propertyName);	
		}
		
		if( getter == null){
			throw new MatchRuntimeException("Couldn't find getter method '" + propertyName + "' on class '" + beanClass.getName() + "'. Tried with and with 'get' prefixed");
		}
		//check property type is the same as matcher
		Class<?> actualPropertyType = getter.getReturnType();
		
		if(!expectedPropertyType.isAssignableFrom(actualPropertyType)){
			//the check above doesn't take into account auto boxing
			if(autoBoxedConversions.get(actualPropertyType) != expectedPropertyType){
				throw new IllegalArgumentException(String.format("property type (%s) and matcher type (%s) don't match for property '%s' on %s", actualPropertyType,expectedPropertyType,propertyName, beanClass.getName()));
			}
		}
		withMatcher(new PropertyValueMatcher<TProperty>(getter, matcher));
	}
	
	private Method getGetterOrNull(String methodName){
		try {
			return beanClass.getMethod(methodName, (Class<?>[])null);
		} catch(NoSuchMethodException e){
			return null;
		}
	}
	
	private static String firstToUpper(String name){
		if( name.length() > 1){
			return Character.toUpperCase(name.charAt(0)) + name.substring(1);
		}
		return name.toUpperCase();
	}
	
	private class PropertyValueMatcher<TProperty> extends AbstractMatcher<T> {
		
		private final Method getterMethod;
		private final Matcher<TProperty> matcher;
		
		private PropertyValueMatcher(Method method,Matcher<TProperty> matcher){
			this.getterMethod = method;
			this.matcher = matcher;
		}

		@Override
		public boolean matchesSafely(T actual, MatchDiagnostics ctxt) {
			TProperty propertyVal;
			try {
				propertyVal = (TProperty)getterMethod.invoke(actual, NoArgs);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				ctxt.MisMatched(DefaultDescription.with().value("propertyGetterException", e));
				return false;
			} catch(ClassCastException e){//shouldn't happen
				ctxt.MisMatched(DefaultDescription.with().value("propertyGetterException", e));
				return false;
			}
			if(!ctxt.TryMatch(propertyVal, matcher)){
				return false;
			}
			return true;
		}
	}
}
