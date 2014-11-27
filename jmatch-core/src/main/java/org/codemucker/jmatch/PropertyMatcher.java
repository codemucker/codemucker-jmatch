package org.codemucker.jmatch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class PropertyMatcher<T> extends ObjectMatcher<T> {

	private static final Object[] NoArgs = new Object[]{};
	
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
	@SuppressWarnings("rawtypes")
    public PropertyMatcher(Class beanClass){
	    super(beanClass);
	}
	
	/**
	 * Add a matcher to match on the given property path. Multiple calls to this for the same property simply adds matchers, it does not replace existing ones. This way matchers
	 * can be added to test different aspects of a given property.
	 * 
	 * @param propertyName the property name. This can be either the method name ('isFoo','getFoo'), simple name ('foo'), or a path expression ('foo.bar.something') 
	 * 
	 * <p>Rules for matching:
	 * <ul>
	 * <li>If the property name starts with 'is' or 'get' then a method with the same name is looked for (so for 'getFoo' only 'getFoo' is looked for)
	 * <li>If the property name is in the form 'foo', then methods 'isFoo' (only for boolean types) ,'getFoo','foo' is looked for.</li>
	 * <li>If no property with the given case can be found, a match is attempted ignoring the case of the property, throwing an exception if multiple methods match. ('getFOO' -&gt; 'getfoo')</li>
	 * </ul>
	 * </p>
	 * <p>If no matching getter method is found, an exception is thrown</p>
	 * 
	 * @param expectedPropertyType the property type. A check is performed to ensure the getter return type matches (throwing an error in this method call if it does not match)
	 * @param propertyValueMatcher the matcher to validate the property value. Cannot be null
	 */
	protected <TProperty> void matchProperty(String propertyName, Class<TProperty> expectedPropertyType, Matcher<TProperty> propertyValueMatcher){
	    Preconditions.checkArgument(!isBlank(propertyName),"expect non blank property name");
        Preconditions.checkNotNull(propertyValueMatcher,"expect matcher");
	    Preconditions.checkNotNull(expectedPropertyType,"expect propertyType");
        
	    Matcher<T> beanMatcher;
	    Class<?> actualPropertyType;
	    
	    if(propertyName.contains(".")){
            Method[] getters = findGetterPath(propertyName,expectedPropertyType);
            actualPropertyType = getters[getters.length-1].getReturnType();
            beanMatcher = new PropertyPathValueMatcher<TProperty>(getters, propertyValueMatcher);	        
	    } else {
    		Method getter = findGetter(propertyName,expectedPropertyType);
    		actualPropertyType = getter.getReturnType();
    		beanMatcher =new PropertyValueMatcher<TProperty>(getter, propertyValueMatcher);
    	}
	    if(!expectedPropertyType.isAssignableFrom(actualPropertyType)){
            //the check above doesn't take into account auto boxing
            if(autoBoxedConversions.get(actualPropertyType) != expectedPropertyType){
                throw new IllegalArgumentException(String.format("property type (%s) and matcher type (%s) don't match for property '%s' on %s", actualPropertyType,expectedPropertyType,propertyName, getExpectType().getName()));
            }
        }
        addMatcher(beanMatcher);
	}
	
	private static boolean isBlank(String s){
	    return s == null || s.trim().length() == 0;
	}


    private Method[] findGetterPath(final String propertyPath, final Class<?> propertyType) {
        String[] parts = propertyPath.split(".");
        Method[] methods = new Method[parts.length];
        
        for(int i = 0; i < parts.length; i++){
            String part = parts[i];
            try {
                Method m = findGetter(part, i==parts.length - 1?propertyType:Object.class);
                methods[i] = m;
            } catch (Exception e){
                throw new JMatchException("Could not find getter for property path '" + propertyPath + "'. Got to " + Arrays.toString(Arrays.copyOfRange(parts,0, i)) );
            }
        }
        return methods;
    }
    
    private Method findGetter(String propertyName, Class<?> propertyType) {
        String[] tryNames;
        if (propertyName.startsWith("get") || propertyName.startsWith("is")) {
            tryNames = new String[] { propertyName };
        } else {
            String camelCase = firstToUpper(propertyName);
            if (propertyType == Boolean.class || propertyType == boolean.class) {
                tryNames = new String[] { "is" + camelCase, "get" + camelCase, propertyName };
            } else {
                tryNames = new String[] { "get" + camelCase, propertyName };
            }
        }
        for (String name : tryNames) {
            Method m = getGetterOrNull(name);
            if (m != null) {
                return m;
            }
        }
        throw new JMatchException("Couldn't find no-arg getter method for property '" + propertyName + "' on class '" + getExpectType().getName() + "'. Tried names " + Arrays.toString(tryNames) + " including case insensitive matches");
    }
    
	private Method getGetterOrNull(String methodName){
		try {
			return getExpectType().getMethod(methodName, (Class<?>[])null);
		} catch(NoSuchMethodException e){
		    Method found = null;
		    //lets perform a case insensitive match
		    String expectLowerName = methodName.toLowerCase();
            for (Method m : getExpectType().getMethods()) {
                if (m.getParameterTypes().length == 0 && m.getReturnType() != Void.class) {
                    String nameLower = m.getName().toLowerCase();
                    if (nameLower.equals(expectLowerName)) {
                        if (found != null) {
                            throw new IllegalArgumentException("found multiple no-arg methods with the same lowercase name for '" + methodName + "'");
                        }
                        found = m;
                    }
                }
            }
		    return found;
			
		}
	}
	
	private static String firstToUpper(String name){
		if( name.length() > 1){
			return Character.toUpperCase(name.charAt(0)) + name.substring(1);
		}
		return name.toUpperCase();
	}
	
	/**
	 * Used to match a direct property
	 *
	 * @param <TProperty>
	 */
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
				ctxt.mismatched(DefaultDescription.with().value("propertyGetterException", e));
				return false;
			} catch(ClassCastException e){//shouldn't happen
				ctxt.mismatched(DefaultDescription.with().value("propertyGetterException", e));
				return false;
			}
			return ctxt.tryMatch(this,propertyVal, matcher);
		}
		
		@Override
	    public void describeTo(Description desc) {
	        desc.value(getterMethod.getName() + "() is", matcher);
	    }
	}

	/**
	 * Used to match a sub property as in foo.bar.myProp
	 *
	 * @param <TProperty>
	 */
       private class PropertyPathValueMatcher<TProperty> extends AbstractMatcher<T> {
            
            private final Method[] getterMethods;
            private final Matcher<TProperty> matcher;
            private final String methodPathDescription;
            
            private PropertyPathValueMatcher(Method[] methods,Matcher<TProperty> matcher){
                this.getterMethods = methods;
                this.matcher = matcher;
                String s = "";
                for (Method m : getterMethods) {
                    if (s.length() > 0) {
                        s += ".";
                    }
                    s += m.getName() + "()";
                }
                this.methodPathDescription = s;
            }
    
            @Override
            public boolean matchesSafely(T actual, MatchDiagnostics ctxt) {
                Object instance = actual;
                TProperty propertyVal = null;
                try {
                    for(int i = 0; i < getterMethods.length && instance != null;i++){
                        propertyVal = (TProperty)getterMethods[i].invoke(instance, NoArgs);
                        instance = propertyVal;        
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    ctxt.mismatched(DefaultDescription.with().value("propertyGetterException", e));
                return false;
            } catch(ClassCastException e){//shouldn't happen
                ctxt.mismatched(DefaultDescription.with().value("propertyGetterException", e));
                return false;
            }
            if(instance !=null){
                return ctxt.tryMatch(this,propertyVal, matcher);         
            } else {
                return ctxt.tryMatch(this,null, matcher);
            }
        }
        
        @Override
        public void describeTo(Description desc) {
            desc.value(methodPathDescription + " is", matcher);
        }
    }

	
}
