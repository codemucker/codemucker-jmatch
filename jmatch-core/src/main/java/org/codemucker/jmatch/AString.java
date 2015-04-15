package org.codemucker.jmatch;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.codemucker.jmatch.AbstractMatcher.AllowNulls;
import org.codemucker.jmatch.expression.ExpressionParser;
import org.codemucker.jmatch.expression.StringMatcherBuilderCallback;

import com.google.common.collect.Lists;

public class AString {

    public static final Matcher<String> equalToAnything() {
        return new AbstractMatcher<String>(AllowNulls.YES) {
            @Override
            protected boolean matchesSafely(String actual, MatchDiagnostics diag) {
                return true;
            }

            @Override
            public void describeTo(Description desc) {
                desc.text("a string of anything or null");
            }
        };
    }

	@SafeVarargs
	public static final List<Matcher<String>> equalToAll(final String... expectAll) {
		List<Matcher<String>> matchers = new ArrayList<Matcher<String>>(
				expectAll.length);
		for (int i = 0; i < expectAll.length; i++) {
			matchers.add(equalTo(expectAll[i]));
		}
		return matchers;
	}
	
	/**
	 * Use a {@link ExpressionParser} to parse the expression and convert it to a matcher. Expression parts are interpreted as
	 * {@link #matchingAntPattern(String). If the expression is null or blank then match anything
	 * 
	 * <p> E.g    ( foo && ! bar && !(alice || bob ) )</p>
	 * @param expression
	 * @return
	 */
	public static final Matcher<String> matchingExpression(String expression){
		expression = expression==null?null:expression.trim();
		if(expression == null || expression.length() == 0){
			return equalToAnything();
		}
		return ExpressionParser.parse(expression, new StringMatcherBuilderCallback());
	}
	
	public static final Matcher<String> matchingAnyAntPattern(final String... antPatterns){
	    List<String> patterns = new ArrayList<>();
	    for(String p:antPatterns){
	        patterns.add(p);
	    }
		return matchingAnyAntPattern(patterns);
	}
	
	public static final Matcher<String> matchingAnyAntPattern(Iterable<String> antPatterns){
        List<Matcher<String>> matchers = new ArrayList<>();
        for (String pattern:antPatterns) {
            matchers.add(matchingAntPattern(pattern));
        }
        return Logical.any(matchers);
    }
	
	public static final Matcher<String> equalToAny(final String... expects){
		return equalToAny(Lists.newArrayList(expects));
	}
	
	public static final Matcher<String> equalToAny(final Iterable<String> expects){
        List<Matcher<String>> matchers = new ArrayList<>();
        for (String expect:expects) {
            matchers.add(equalTo(expect));
        }
        return Logical.any(matchers);
    }
    
	public static final Matcher<String> equalToNull(){
	    return equalTo(null);
	}
	
	public static final Matcher<String> equalTo(final String expect){
		return new AbstractMatcher<String>(AllowNulls.YES){ 
			@Override
			public boolean matchesSafely(String found, MatchDiagnostics ctxt) {
				if( expect == null && found == null){
					return true;
				}
				if( expect == null || found == null){
					return false;
				}
				return expect.equals(found);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string equal to '%s'", expect);
			}
		};
	}

	public static final Matcher<String> equalToIgnoreWhiteSpace(final String expect){
	    final String cleanExpect = safeRemoveWhiteSpace(expect);
        return new AbstractMatcher<String>(AllowNulls.YES){ 
            @Override
            public boolean matchesSafely(String found, MatchDiagnostics ctxt) {
                if( expect == null && found == null){
                    return true;
                }
                if( expect == null || found == null){
                    return false;
                }
                String cleanActual = safeRemoveWhiteSpace(found);
                return cleanExpect.equals(cleanActual);
            }
            
            @Override
            public void describeTo(Description desc) {
                super.describeTo(desc);
                desc.text("a string equal to '%s' ignoring whitespace", expect);
            }
        };
    }
	
	public static final Matcher<String> equalToIgnoreCaseWhiteSpace(final String expect){
	    final String cleanExpect = safeToLower(safeRemoveWhiteSpace(expect));
        return new AbstractMatcher<String>(AllowNulls.YES){ 
            @Override
            public boolean matchesSafely(String found, MatchDiagnostics ctxt) {
                if( expect == null && found == null){
                    return true;
                }
                if( expect == null || found == null){
                    return false;
                }
                String cleanActual = safeToLower(safeRemoveWhiteSpace(found));
                return cleanExpect.equals(cleanActual);
            }
            
            @Override
            public void describeTo(Description desc) {
                super.describeTo(desc);
                desc.text("a string equal to '%s' ignoring case and whitespace", expect);
            }
        };
    }
	
	private static String safeToLower(String s) {
	    if(s == null){
	        return s;
	    }
	    return s.toLowerCase();
	}
    
	private static String safeRemoveWhiteSpace(String s) {
        if (s == null) {
            return s;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }
	
	public static final Matcher<String> equalToIgnoreCase(final String expect){
		return new AbstractMatcher<String>(AllowNulls.YES){ 
			@Override
			public boolean matchesSafely(String found, MatchDiagnostics ctxt) {
				if( expect == null && found == null){
					return true;
				}
				if( expect == null || found == null){
					return false;
				}
				return expect.equalsIgnoreCase(found);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string equal to '%s' ignoring case", expect);
			}
		};
	}
	
	public static final Matcher<String> notEmpty(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found, MatchDiagnostics diag) {
				return !found.isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a non empty or null string");
			}
		};
	}

	public static final Matcher<String> empty(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("an empty string");
			}
		};
	}
	
	public static final Matcher<String> notBlank(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return !found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string not null or blank");
			}
		};
	}
	
	public static final Matcher<String> blank(){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a non null blank string");
			}
		};
	}
	
	public static final Matcher<String> blankOrNull(){
		return new AbstractMatcher<String>(AllowNulls.YES){ 
			@Override
			public boolean matchesSafely(String found, MatchDiagnostics diag) {
				return found == null || found.trim().isEmpty();
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a blank or null string");
			}
		};
	}
	
	public static final Matcher<String> endingWith(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.endsWith(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string ending with '%s'", expect);
			}
		};
	}
	
	public static final Matcher<String> endingWithIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.toLowerCase().endsWith(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string ending with '%s' (ignoring case)", expect);
			}
		};
	}
	
	public static final Matcher<String> contains(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.contains(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string containing '%s'", expect);
			}
		};
	}
	
	public static final Matcher<String> containsIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.toLowerCase().contains(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string containing '%s' (ignoring case)", expect);
			}
		};
	}
	
	public static final Matcher<String> startingWith(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.startsWith(expect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string starting with '%s'", expect);
			}
		};
	}
	
	public static final Matcher<String> startingWithIgnoreCase(final String expect){
		return new AbstractNotNullMatcher<String>(){ 
			final String lowerExpect = expect.toLowerCase();
				
			@Override
			public boolean matchesSafely(String found,MatchDiagnostics diag) {
				return found.toLowerCase().startsWith(lowerExpect);
			}
			
			@Override
			public void describeTo(Description desc) {
				super.describeTo(desc);
				desc.text("a string starting with '%s' (ignoring case)", expect);
			}
		};
	}
	
	/**
	 * Match using an ant expression where '*' matches anything which is not a forward or back slash, '**" matches anything, '?' matches a single char
	 * 
	 * @param antExpression
	 * @return
	 */
	public static Matcher<String> matchingAntFilePathPattern(String antExpression){
		return matchingAntPattern(antExpression,true);
	}
	
	/**
	 * Match using an ant expression where '*' matches anything, '?' matches a single char
	 * 
	 * @param antExpression
	 * @return
	 */
	public static Matcher<String> matchingAntPattern(String antExpression){
		return matchingAntPattern(antExpression,false);
	}
	
	private static Matcher<String> matchingAntPattern(String antExpression, boolean requireDoubleStarForSlashes){
		return new RegExpPatternMatcher(antExpToPattern(antExpression,requireDoubleStarForSlashes));
	}
	
	public static Matcher<String> matchingRegex(String pattern){
		return new RegExpPatternMatcher(Pattern.compile(pattern));
	}
	
	public static Matcher<String> matchingRegex(Pattern pattern){
		return new RegExpPatternMatcher(pattern);
	}
	
	/**
	 * Convert an ant regular expression into a java pattern
	 */
	private static Pattern antExpToPattern(String antPattern,boolean requireDoubleStarForSlashes) {
		return Pattern.compile(antExpToPatternExp(antPattern, requireDoubleStarForSlashes),Pattern.DOTALL);
	}
	
	/**
	 * Convert an ant regular expression to a standard java pattern expression
	 * 
	 * '*' --&lt; '[^\\]*'
	 * '?' --&lt; '.{1}'
	 * '**' --&lt; '.*'
	 * 
	 */
	private static String antExpToPatternExp(String antPattern, boolean requireDoubleStarForSlashes) {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < antPattern.length(); i++) {
    		char c = antPattern.charAt(i);
    		if (c == '.') {
    			sb.append("\\.");
    		} else if (c == '*') {
    		    //to match path separators an ANT expression requires a double asterix as in '**'
                if (i <= antPattern.length() - 2 && antPattern.charAt(i + 1) == '*') {
                    sb.append(".*");
                    i++;
                } else {
                	if(requireDoubleStarForSlashes){
                		sb.append("[^/\\\\]*");// a single asterix '*' doesn't match path separators
                	} else {
                		 sb.append(".*");	
                	}
                }
    		} else if (c == '?') {
       			sb.append(".{1}");
    		} else {
    			sb.append(c);
    		}
    	}
    	return sb.toString();
    }

	private static class RegExpPatternMatcher extends AbstractNotNullMatcher<String> {
		private final Pattern pattern;
		

		public RegExpPatternMatcher(Pattern pattern) {
			this.pattern = checkNotNull(pattern,"expect regexp pattern");
		}

		@Override
		public boolean matchesSafely(String s,MatchDiagnostics diag) {
			return pattern.matcher(s).matches();
		}
		
		@Override
		public void describeTo(Description desc) {
			//super.describeTo(desc);
			desc.text("a string matching reg exp '%s'%s", pattern.pattern(),toOptions(pattern));
		}
		
		private static String toOptions(Pattern p){
		    int flags = p.flags();
		    StringBuilder sb = new StringBuilder();
		    addIf(sb,flags,Pattern.CANON_EQ,"canonical");
		    addIf(sb,flags,Pattern.CASE_INSENSITIVE,"case insensitive");
		    addIf(sb,flags,Pattern.COMMENTS,"permit comments and whitespace");
		    addIf(sb,flags,Pattern.DOTALL,"dotall");
		    addIf(sb,flags,Pattern.LITERAL,"literal");
		    addIf(sb,flags,Pattern.MULTILINE,"multiline");
		    addIf(sb,flags,Pattern.UNICODE_CASE,"unicode case");
		    addIf(sb,flags,Pattern.UNICODE_CHARACTER_CLASS,"canon");
		    addIf(sb,flags,Pattern.UNIX_LINES,"unix lines");
		    if(sb.length() > 0 ){
		        return " with pattern option " + sb.toString();
		    }
		    return sb.toString();
		}
		
		private static void addIf(StringBuilder sb, int flags,int opt, String name){
		    if((flags & opt) != 0){
		        if(sb.length() > 0){
		            sb.append(",");
		        }
		        sb.append(name);
		    }
		}
	}
	
}
