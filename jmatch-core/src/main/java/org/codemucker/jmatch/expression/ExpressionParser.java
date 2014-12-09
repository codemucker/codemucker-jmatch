package org.codemucker.jmatch.expression;

import org.codemucker.lang.IBuilder;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * Parse a logical expression of the form
 * 
 *  jim && !sam && ( bob || !alice || ( mary && tim ) ) && sue
 *  
 *  The non logical and grouping expressions are converted into matchers using the provided {@link ParseCallback}
 *  
 * @param <T>
 */
public class ExpressionParser<T> {

	private final String s;
	private final int len;

	private int pos;
	private int startExp;
	private int groupCount;

	private final ParseCallback<T> builder;
	private final GroupTokens groupTokens;
	
	public static enum GroupTokens {
		BRACKETS('(',')'),SQUARE_BRACKETS('[',']');
		
		public final char start;
		public final char end;
		
		private GroupTokens(char start, char end) {
			this.start = start;
			this.end = end;
		}	
	}
	
	private ExpressionParser(String s,ParseCallback<T> builder, GroupTokens groupTokens) {
		Preconditions.checkNotNull(builder,"expect parse callback");
		Preconditions.checkNotNull(groupTokens,"expect parse groupTokens");
		
		this.s = s;
		this.len = s==null?0:s.length();
		this.builder = builder;
		this.groupTokens = groupTokens;
	}
	

	public static <T> T parse(String s,ParseCallback<T> builder) throws ParseException {
		return parse(s,builder, GroupTokens.BRACKETS);
	}

	public static <T> T parse(String s,ParseCallback<T> builder,GroupTokens tokens) throws ParseException {
		ExpressionParser<T> parser = new ExpressionParser<T>(s,builder,tokens);
		try {
			return parser.parse();
		} catch (Exception e){
			String upto = parser.s.substring(0, parser.pos);
			throw new ParseException("Error parsing expression '" + s + "', error at position " + parser.pos + ", parsed up to '" + upto + "'",e); 
		}
	}
	
	private T parse(){
		builder.onStart();
		while (canRead()) {
			char c = read();
			if( c == '\''){//start of escape
				//read until end of escape
				startNextExpression();
				if(readNextUntil('\'')){
					endLastExpression();
					startNextExpression();
				} else {
					throw new ParseException("No closing single quote found");
				}
			} else if (c == '!') {
				endLastExpression();
				builder.onNegate();
				startNextExpression();
			} else if (c == '&') {
				if (isPeek('&')) {
					endLastExpression();
					nextChar();
					builder.onAND();
					startNextExpression();
				}
			} else if (c == '|') {
				if (isPeek('|')) {
					endLastExpression();
					nextChar();
					builder.onOR();
					startNextExpression();
				}
			} else if(c == groupTokens.start){
				endLastExpression();
				groupCount++;
				builder.onStartGroup();
				startNextExpression();
			} else if (c == groupTokens.end) {
				if (groupCount <= 0) {
					throw new ParseException("Invalid expression, expect a '(' before ')' at character " + pos);
				}
				groupCount--;
				endLastExpression();
				builder.onEndGroup();
				startNextExpression();
			}
			nextChar();
		}
		endLastExpression();
		builder.onEnd();
		
		return builder.build();
	}
	
	private boolean readNextUntil(char until) {
		for (int i = pos + 1; i < len; i++) {
			if (s.charAt(i) == until) {
				pos = i;
				return true;
			}
		}
		return false;
	}

	private void startNextExpression() {
		startExp = pos + 1;
	}

	private void endLastExpression() {
		if (startExp < pos) {
			String exp = Strings.emptyToNull(s.substring(startExp, pos).trim());
			if (exp != null) {
				builder.onExpression(exp);
			}
		}
	}

	private boolean canRead() {
		return s!= null && pos < len;
	}

	private boolean hasNext() {
		return pos < len - 1;
	}

	private char read() {
		return s.charAt(pos);
	}

	private boolean isPeek(char c) {
		if (hasNext()) {
			return c == peek();
		}
		return false;
	}

	private char peek() {
		return s.charAt(pos + 1);
	}

	private boolean nextChar() {
		pos++;
		if (pos > len) {
			pos = len;
		}
		if (hasNext()) {
			return true;
		}
		return false;
	}

	public static interface ParseCallback<T> extends IBuilder<T> {

		void onStart();

		void onEndGroup();

		void onStartGroup();

		void onAND();

		void onOR();

		void onNegate();

		void onExpression(String expression);

		void onEnd();
		
	}

	public static class ParseException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public ParseException(String message, Throwable cause) {
			super(message, cause);
		}

		public ParseException(String message) {
			super(message);
		}

	}

}