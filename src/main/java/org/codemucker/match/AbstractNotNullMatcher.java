package org.codemucker.match;

public abstract class AbstractNotNullMatcher<T> extends AbstractMatcher<T> {
	
	public AbstractNotNullMatcher(){
		super(AllowNulls.NO);
	}
}
