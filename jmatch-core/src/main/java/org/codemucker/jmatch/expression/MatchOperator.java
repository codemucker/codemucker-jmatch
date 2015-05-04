package org.codemucker.jmatch.expression;

enum MatchOperator {
	
	EQ("=",strings("IsEqualTo", "EqualTo", "Equals", "Is", "of"),0,2),
	NOT_EQ("!=",strings("IsNot","IsEqualTo", "NotEqualTo", "Not"),0,2), 
	GREATER(">",strings("IsGreaterThan","GreaterThan"),1,1), 
	GREATER_EQ(">=",strings("IsGreaterOrEqualTo","IsGreaterThanOrEqualTo", "GreaterOrEqualTo","GreaterThanOrEqualTo"),1,1), 
	LESS("<",strings("IsLessThan","LessThan"),1,1), 
	LESS_EQ("<=",strings("IsLessThanOrEqualTo","IsLessOrEqualTo","LessThanOrEqualTo","LessOrEqualTo"),1,1), 
	BETWEEN(" between ",strings("IsBetween","Between"),2,2);

	final String[] prefixes;
	final String[] suffixes;
	
	final int minArgs;
	final int maxArgs;
	final String symbol;
	
	private MatchOperator(String symbol,String[] prefixes,int minArgs,int maxArgs) {
		this.symbol = symbol;
		this.prefixes = prefixes;
		this.suffixes = prefixes;
		this.minArgs = minArgs;
		this.maxArgs = maxArgs;
	}
	
	private static String[] strings(String...values){
		for(int i = 0; i < values.length;i++){
			values[i] = values[i].toLowerCase();
		}
		return values;
	}
}