package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.codemucker.lang.annotation.ThreadSafe;


/**
 * <p>Matcher to match on a collections of objects. Configurable to take into account element order
 * and/or whether we ignore additional list items or not.
 * 
 * </p>
 * <p>
 * Typical usage:
 * 
 * <pre>
 *  AList.of(String.class).inAnyOrder()
 *      .withOnly(myMatcher)
 *      .and(myMatcher2)
 *  </pre>
 * 
 * Thread safe if no modifications are made to the matcher once it's been handed off to the various threads
 *</p>
 * <p> Example usages:
 * 
 * <pre>
 * AList.ofStrings().withNothing()
 * AList.of(Foo.class).withNothing()
 * 
 * AList.inOrder().withAtLeast(AFoo.with().name("bar"))
 * AList.withOnly(AFoo.with().name("bar"))
 * AList.inAnyOrder()
 *      .withOnly(AFoo.with().name("bar"))
 *      .and(AFoo.with().name("bob"))
 * </pre>
 * Using the expector:
 * 
 * <pre>
 * Expect
 *  .that(foo)
 *  .is(AList.inOrder()
 *      .withOnly(AFoo.with().name("bob"))
 *      .and(AFoo.with().name("alice")))
 * </pre>
 * </p>
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
	
	public static <T> Matcher<Iterable<T>> withOnly(T value){
        return withOnly(AnInstance.equalTo(value));
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
	
	/**
     * Builder for a list matcher where order and contains (only/any) must still be provided
     */
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
	/**
	 * Builder for a list matcher where item type has not yet been provided
	 */
	public static class ListContainsAndTypeRequired {

		private final ListMatcher.ORDER order;
		
		ListContainsAndTypeRequired(ListMatcher.ORDER order){
			this.order = order;
		}
		
		public ListTypeRequired withAtLeast(){
			return new ListTypeRequired(order,ListMatcher.CONTAINS.ATLEAST);
		}
		
		public <T> IAcceptMoreMatchers<T> withAtLeast(T  value){
            return withAtLeast(AnInstance.equalTo(value));
        }
		
		public <T> IAcceptMoreMatchers<T> withAtLeast(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ATLEAST).add(matcher);
		}
		
		public ListTypeRequired withOnly(){
			return new ListTypeRequired(order,ListMatcher.CONTAINS.ONLY);
		}
		
		public <T> IAcceptMoreMatchers<T> withOnly(T value){
            return withOnly(AnInstance.equalTo(value));
        }       
		
		public <T> IAcceptMoreMatchers<T> withOnly(Matcher<T> matcher){
			return new ListMatcher<T>(order,ListMatcher.CONTAINS.ONLY).add(matcher);
		}		
	}

	/**
     * Builder for a list matcher where the item type has already been provided
     */
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
			return new ListMatcher<T>(order, contains).items(matchers);
		}
		
		public <T> ListMatcher<T> items(final T... exactItems) {
			return new ListMatcher<T>(order, contains).items(exactItems);	
		}
		
		@SuppressWarnings("unchecked")
		public <T> IAcceptMoreMatchers<T> items(final Matcher<T>... matchers){
			return new ListMatcher<T>(order, contains).items(matchers);
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
	     * Synonym for {@link #and(Matcher)}
	     */
		IAcceptMoreMatchers<T> add(final Matcher<T> matcher);
		
		/**
         * Synonym for {@link #and(Object))}
         */
		IAcceptMoreMatchers<T> add(T value);

        /**
         * Synonym for {@link #and(Object))}
         */
        IAcceptMoreMatchers<T> item(T value);

        /**
	     * Synonym for {@link #and(Matcher)}
	     */
		IAcceptMoreMatchers<T> item(final Matcher<T> matcher);

        /**
         * Add a list of matchers
         */
        IAcceptMoreMatchers<T> items(final Iterable<? extends Matcher<T>> matchers); 
        
        IAcceptMoreMatchers<T> items(final T... exactItems); 
        
        @SuppressWarnings("unchecked")
        IAcceptMoreMatchers<T> items(final Matcher<T>... matchers); 
        
		/**
		 * Add a matcher which performs an exact match for the given value
		 */
		IAcceptMoreMatchers<T> and(T value);
        
		/**
	     * Add a matcher.
	     */
		IAcceptMoreMatchers<T> and(final Matcher<T> matcher);
		
	}
	
	/**
	 * The list matcher which does the actual work
	 *
	 * @param <T> the type of the item we are expecting
	 */
	@ThreadSafe(caveats="if no matchers are added once it's been handed off to the various threads")
	private final static class ListMatcher<T> extends AbstractNotNullMatcher<Iterable<T>> implements IAcceptMoreMatchers<T> {
	    
	    private final Collection<Matcher<T>> matchers = new ArrayList<Matcher<T>>();
	    private final ORDER order;
	    private final CONTAINS contains;
	
	    /**
	     * The order we expect list elements to be in
	     */
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
	
	    /**
	     * How we deal with additional items in the list
	     */
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
	    public ListMatcher<T> items(final T... exactItems) {
	        for(T item:exactItems){
	        	add(AnInstance.equalTo(item));
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
        public ListMatcher<T> item(T value) {
            return add(value);
        }
	    
	    @Override
	    public ListMatcher<T> item(final Matcher<T> matcher) {
	    	return add(matcher);
	    }
	    
	    @Override
        public ListMatcher<T> and(final T value) {
            return add(value);
        }
        
	    
	    @Override
	    public ListMatcher<T> and(final Matcher<T> matcher) {
	        return add(matcher);
	    }
	    
	    @Override
        public ListMatcher<T> add(final T value) {
            return add(AnInstance.equalTo(value));
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
	        final List<ItemIndexHolder<T>> nonMatchedItems = new ArrayList<>();
        	final boolean diagEnabled = !diag.isNull();
        	
	        int actualCount=0;
	        switch (order) {
	        case ANY:
	            // in any order
	            for (final T actualItem : actual) {
	                actualCount++;
	                // since order is not important, lets find the first matcher
	                // which matches
	                boolean entryMatched = false;
	                matcher:for (final Iterator<Matcher<T>> iter = matchersLeft.iterator(); iter
	                        .hasNext();) {
	                    final Matcher<T> matcher = iter.next();
	                    MatchDiagnostics child = diag.newChild();
	                    if (matcher.matches(actualItem,child)) {
	                        // we've used the matcher, lets not use it again
	                    	diag.child(child);
	                        iter.remove();
	                        entryMatched = true;
	                        break matcher;
	                    }
	                }
	                if(!entryMatched && diagEnabled){
	            		nonMatchedItems.add(new ItemIndexHolder<T>(actualCount,actualItem));
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
	                MatchDiagnostics child = diag.newChild();
	                // always use the first matcher as this should match first.
	                if (matchersLeft.get(0).matches(actualItem,diag)) {
	                	diag.child(child);
	                    matchersLeft.remove(0);
	                } else {
	                	if(diagEnabled){
	                		nonMatchedItems.add(new ItemIndexHolder<T>(actualCount,actualItem));
	                	}
	                }
	            }
	            break;
	        }
	        boolean matched = true;
	        //don't expect any more items than matchers for CONTAINS.ONLY
	        if(CONTAINS.ONLY == contains && matchers.size() != actualCount){
	        	if(diagEnabled && nonMatchedItems.size() > 0){
	        		for(ItemIndexHolder<T> itemAndIndex:nonMatchedItems){
	        			diag.mismatched("position=" + itemAndIndex.position + ",item=" + itemAndIndex.item);
	        		}
	        	}
	        	matched = false;
	        }
	        
	        // if zero means each matcher has matched once
	        if(matchersLeft.size() != 0){
	        	if(diagEnabled){
	        		MatchDiagnostics child = diag.newChild();
	        		for(SelfDescribing matcher:matchersLeft){
		        		child.mismatched(matcher);
		        	}
	        		diag.child("Matchers NOT satisfied", child);
	        	}
	        	matched = false;
	        }
	        return matched;
	    }
	    
	    @Override
	    public void describeTo(final Description desc) {
	        if(desc.isNull()){
	            return;
	        }
	        int size = matchers.size();
	        String order = this.order.toString().toLowerCase();
	        String contains = this.contains.toString().toLowerCase();
	        
	        desc.values("items in " + order + " order matching " +  contains + " (" + size + " matcher" + (size==1?"":"s")+ ")" , matchers);
	    }
	}
	
	private static class ItemIndexHolder<T> {
		private final int position;
		private final T item;
		public ItemIndexHolder(int position, T item) {
			super();
			this.position = position;
			this.item = item;
		}
		
		
	}
}
