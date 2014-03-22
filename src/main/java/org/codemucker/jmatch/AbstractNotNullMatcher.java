package org.codemucker.jmatch;

public abstract class AbstractNotNullMatcher<T> extends AbstractMatcher<T> {
	
	public AbstractNotNullMatcher(){
		super(AllowNulls.NO);
	}
}
