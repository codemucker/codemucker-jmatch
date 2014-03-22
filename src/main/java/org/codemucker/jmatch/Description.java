package org.codemucker.jmatch;

public interface Description extends SelfDescribing {

	Description text(String string);
	Description text(String string,Object arg1);
	Description text(String string,Object arg1,Object arg2);
	Description text(String string,Object arg1,Object arg2,Object arg3);
	Description text(String string,Object... args);
	
	Description value(String label, Object value);
	Description values(String label, Iterable<?> values);
	Description child(Object child);
	Description child(String label,Object child);
	
	/**
	 * Return whether this is a null description. That is, a descroption which ignores all input
	 * @return
	 */
	boolean isNull();
	
}
