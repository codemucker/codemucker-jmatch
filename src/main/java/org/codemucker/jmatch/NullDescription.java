package org.codemucker.jmatch;

public class NullDescription implements Description {

    public static final Description INSTANCE = new NullDescription();
    
	@Override
	public Description text(String string) {
		return this;
	}

	@Override
	public boolean isNull() {
		return true;
	}

	@Override
	public Description text(String string, Object arg1) {
		return this;
	}

	@Override
	public Description text(String string, Object arg1, Object arg2) {
		return this;
	}

	@Override
	public Description text(String string, Object arg1, Object arg2, Object arg3) {
		return this;
	}

	@Override
	public Description text(String string, Object... args) {
		return this;
	}

	@Override
    public Description value(Object value) {
        return null;
    }
	
	@Override
	public Description value(String label, Object value) {
		return this;
	}

	@Override
	public Description values(String label, Iterable<?> values) {
		return this;
	}

	@Override
	public Description child(Object child) {
		return this;
	}
	
	@Override
	public Description child(String label, Object child) {
		return this;
	}

	@Override
	public void describeTo(Description desc) {
		//do nothing
	}

}
