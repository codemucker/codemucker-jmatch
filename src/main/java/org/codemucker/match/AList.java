package org.codemucker.match;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codemucker.lang.annotation.ThreadSafe;
import org.codemucker.match.Description;
import org.codemucker.match.Matcher;


/**
 * Matcher to match on a collections of objects. Configurable to take into account element order
 * and/or whether we ignore additional list items or not.
 * 
 * Typical usage:
 * 
 * AList.of(String.class).inAnyOrder().containingOnly().a(myMatcher).a(myMatcher2)
 * 
 * Thread safe if no modifications are made to the matcher once it's been handed off to the various threads
 *
 * @author Bert van Brakel
 *
 * Example usage:
 * 
 * AList.ofStrings().withNothing()
 * AList.of(Foo.class).withNothing()
 * 
 * AList.inOrder().withAtLeast(AFoo.with().name("bar"))
 * AList.withOnly(AFoo.with().name("bar"))
 * AList.anyOrder().withOnly(AFoo.with().name("bar")).and(AFoo.with().name("bob"))
 * 
 */

public class AList {

	private AList(){
		//prevent instantiation
	}
	
	public static ListOrderAndContainsRequired<Object> ofObjects(){
		return of(Object.class);
	}
	
	public static ListOrderAndContainsRequired<String> ofStrings(){
		return of(String.class);
	}
	
	public static ListOrderAndContainsRequired<Integer> ofInts(){
		return of(Integer.class);
	}
	
	public static ListOrderAndContainsRequired<Character> ofChars(){
		return of(Character.class);
	}
	
	public static <T> ListOrderAndContainsRequired<T> of(Class<T> itemType){
		return new ListOrderAndContainsRequired<T>();
	}

	/**
	 * A matcher which expects an empty list
	 * @return
	 */
	public static <T> Matcher<Iterable<T>> withNothing(){
		return new ListEmptyMatcher<T>();
	}
	
	public static <T> Matcher<Iterable<T>> withOnly(Matcher<T> matcher){
		return new ListMatcher<T>(ListMatcher.ORDER.ANY,ListMatcher.CONTAINS.ONLY).add(matcher);
	}
	
	/**
	 * A list match builder for a list where item order is important
	 * @return
	 */
	public static ListContainsAndTypeRequired inOrder(){
		return new ListContainsAndTypeRequired(ListMatcher.ORDER.EXACT);
	}
	
	/**
	 * A list match builder for a list where item order is not important
	 * @return
	 */
	public static ListContainsAndTypeRequired inAnyOrder(){
		return new ListContainsAndTypeRequired(ListMatcher.ORDER.ANY);
	}
	
	public static class ListOrderAndContainsRequired<T>{
		/**
		 * A matcher which expects an empty list
		 * @return
		 */
		public Matcher<Iterable<T>> withNothing(){
			return new ListEmptyMatcher<T>();
		}
		
		public Matcher<Iterable<T>> withOnly(Matcher<T> matcher){
			return new ListMatcher<T>(ListMatcher.ORDER.ANY,ListMatcher.CONTAINS.ONLY).add(matcher);
		}
	
		public ListContainsRequired<T> inOrder(){
			return new ListContainsRequired<T>(ListMatcher.ORDER.EXACT);
		}
		
		public ListContainsRequired<T> inAnyOrder(){
			return new ListContainsRequired<T>(ListMatcher.ORDER.ANY);
		}
	}
	
	public static class ListContainsAndTypeRequired {

		private final ListMatcher.ORDER order;
		
		ListContainsAndTypeRequired(ListMatcher.ORDER order){
			this.order = order;
		}
		
		public ListTypeRequired withAtLeast(){
			return new ListTypeRequired(order,ListMatcher.CONTAINS.ATLEAST);
		}
		
		public <T> IAcceptMoreMatchers<T> withAtLeast(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ATLEAST).add(matcher);
		}
		
		public ListTypeRequired withOnly(){
			return new ListTypeRequired(order,ListMatcher.CONTAINS.ONLY);
		}
		
		public <T> IAcceptMoreMatchers<T> withOnly(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ONLY).add(matcher);
		}		
	}

	public static class ListContainsRequired<T> {

		private final ListMatcher.ORDER order;
		
		ListContainsRequired(ListMatcher.ORDER order){
			this.order = order;
		}

		public IAcceptMoreMatchers<T> withAtLeast(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ATLEAST).add(matcher);
		}
		
		public IAcceptMoreMatchers<T> withAtLeast(){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ATLEAST);
		}
		
		public IAcceptMoreMatchers<T> withOnly(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ATLEAST).add(matcher);
		}
		
		public IAcceptMoreMatchers<T> withOnly(){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ONLY);
		}
	}
	
	public static class ListTypeRequired {
		
		private final ListMatcher.ORDER order;
		private final ListMatcher.CONTAINS contains;
		
		ListTypeRequired(ListMatcher.ORDER order,ListMatcher.CONTAINS contains){
			this.order = order;
			this.contains = contains;
		}
		
		/**
	     * Add a matcher. Synonym for {@link #add(Matcher)}
	     */
		public <T> IAcceptMoreMatchers<T> item(final Matcher<T> matcher){
			return new ListMatcher<T>(order, contains).add(matcher);
		}
		

		/**
	     * Add a list of matchers
	     */
		public <T>  IAcceptMoreMatchers<T> items(final Iterable<? extends Matcher<T>> matchers){
			ListMatcher<T> list = new ListMatcher<T>(order, contains);
			for(Matcher<T> m : matchers){
				list.add(m);
			}
			return list;
		}
		
		@SuppressWarnings("unchecked")
		public <T> IAcceptMoreMatchers<T> items(final Matcher<T>... matchers){
			ListMatcher<T> list = new ListMatcher<T>(order, contains);
			for(Matcher<T> m : matchers){
				list.add(m);
			}
			return list;
		}
		
	}
	
	private static class ListEmptyMatcher<T> extends AbstractNotNullMatcher<Iterable<T>>
	{
		@Override
		public boolean matchesSafely(Iterable<T> actual,MatchDiagnostics diag) {
			if( actual instanceof Collection){
				return ((Collection<T>)actual).size() == 0;
			}
			return !actual.iterator().hasNext();
		}
	}
	 
	
	public interface IAcceptMoreMatchers<T> extends Matcher<Iterable<T>>{
		/**
	     * Add a matcher. Synonym for {@link #add(Matcher)}
	     */
		public IAcceptMoreMatchers<T> add(final Matcher<T> matcher);
		/**
	     * Add a matcher. Synonym for {@link #add(Matcher)}
	     */
		public IAcceptMoreMatchers<T> item(final Matcher<T> matcher);
		/**
	     * Add a matcher.
	     */
		public IAcceptMoreMatchers<T> and(final Matcher<T> matcher);
		/**
	     * Add a list of matchers
	     */
		public IAcceptMoreMatchers<T> items(final Iterable<? extends Matcher<T>> matchers); 
		
		@SuppressWarnings("unchecked")
		public IAcceptMoreMatchers<T> items(final Matcher<T>... matchers); 
		
	}
	
	@ThreadSafe(caveats="if no matchers are added once it's been handed off to the various threads")
	private final static class ListMatcher<T> extends AbstractNotNullMatcher<Iterable<T>> implements IAcceptMoreMatchers<T>
	{
	    private final Collection<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
	    private final ORDER order;
	    private final CONTAINS contains;
	
	    private static enum ORDER {
	        /**
	         * Exact order. Elements must be in the same order as the matcher
	         */
	        EXACT,
	        /**
	         * Any order. Elements can be in any order
	         */
	        ANY;
	    }
	
	    private static enum CONTAINS {
	        /**
	         * All matchers have to match. Additional elements are allowed. One matcher per item
	         */
	        ATLEAST,
	        /**
	         * All matchers have to match, and no additional elements are allowed. One matcher per item
	         */
	        ONLY;
	    }
	
	    private ListMatcher(final ORDER order, final CONTAINS contains) {
	        if (contains == null) {
	            throw new IllegalArgumentException("Must supply non null strictness. One of " + Arrays.toString(CONTAINS.values()) );
	        }
	        if (order == null) {
	            throw new IllegalArgumentException("Must supply non null order. One of " + Arrays.toString(ORDER.values()) );
	        }
	        this.contains = contains;
	        this.order = order;
	    }
	    
	    @Override
	    public ListMatcher<T> items(final Iterable<? extends Matcher<T>> matchers) {
	        for(Matcher<T>matcher:matchers){
	        	add(matcher);
	        }
	        return this;
	    }
	    
	    @Override
	    public ListMatcher<T> items(final Matcher<T>... matchers) {
	        for(Matcher<T> matcher:matchers){
	        	add(matcher);
	        }
	        return this;
	    }
	    
	    
	    @Override
	    public ListMatcher<T> item(final Matcher<T> matcher) {
	    	return add(matcher);
	    }
	    
	    @Override
	    public ListMatcher<T> and(final Matcher<T> matcher) {
	        return add(matcher);
	    }
	    
	    @Override
	    public ListMatcher<T> add(final Matcher<T> matcher) {
	        this.matchers.add(matcher);
	        return this;
	    }
	    
	    @Override
	    public boolean matchesSafely(final Iterable<T> actual,MatchDiagnostics diag) {
	        // loop though each item in the actual list, and find a matching
	        // matcher. If all the matchers
	        // have passed once, then we have no more checks to perform
	        final List<Matcher<T>> matchersLeft = new ArrayList<Matcher<T>>(matchers);
	        int actualCount=0;
	        switch (order) {
	        case ANY:
	            // in any order
	            for (final T actualItem : actual) {
	                actualCount++;
	                // since order is not important, lets find the first matcher
	                // which matches
	                matcher:for (final Iterator<Matcher<T>> iter = matchersLeft.iterator(); iter
	                        .hasNext();) {
	                    final Matcher<T> matcher = iter.next();
	                    if (matcher.matches(actualItem)) {
	                        // we've used the matcher, lets not use it again
	                        iter.remove();
	                        break matcher;
	                    }
	                }
	            }
	            break;
	        case EXACT:
	            // in the same order as the matchers
	            for (final T actualItem : actual) {
	                actualCount++;
	                // ensure we have matchers left
	                if (matchersLeft.size() == 0) {
	                    break;
	                }
	                // always use the first matcher as this should match first.
	                if (matchersLeft.get(0).matches(actualItem)) {
	                    matchersLeft.remove(0);
	                }
	            }
	            break;
	        }
	        //don't expect any more items than matchers for CONTAINS.ONLY
	        if( CONTAINS.ONLY == contains && matchers.size() != actualCount){
	            return false;
	        }
	
	        // if zero means each matcher has matched once
	        return matchersLeft.size() == 0;
	    }
	    
	    @Override
	    public void describeTo(final Description desc) {
	        desc.value("contains", contains.toString().toLowerCase());
	        desc.value( "order ", order.toString().toLowerCase());
	        desc.values("items", matchers);
	    }
	}
}
