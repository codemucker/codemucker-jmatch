package org.codemucker.jmatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.codemucker.lang.annotation.ThreadSafe;

public class AMap<K, V> extends PropertyMatcher<Map<K, V>> {

	private AMap() {
		super(Map.class);
	}

	// public static <K,V> AMap<K,V> of(){
	// return new AMap<K,V>();
	// }
	//
	public static MapOrderAndContainsRequired<String, String> ofStringString() {
		return of(String.class, String.class);
	}

	public static MapOrderAndContainsRequired<String, Object> ofStringObject() {
		return of(String.class, Object.class);
	}

	public static <K, V> MapOrderAndContainsRequired<K, V> of(Class<K> key, Class<V> value) {
		return new MapOrderAndContainsRequired<K, V>();
	}

	public AMap<K, V> size(int size) {
		size(AnInt.equalTo(size));
		return this;
	}

	public AMap<K, V> size(Matcher<Integer> matcher) {
		matchProperty("size", Integer.class, matcher);
		return this;
	}

	/**
	 * A matcher which expects an empty map
	 * @return
	 */
	public Matcher<Map<K,V>> withNothing(){
		return new MapEmptyMatcher<K,V>();
	}
	
	public static <K,V> Matcher<Map<K,V>> withOnly(K key, V value){
        return inAnyOrder().withOnly(key, value);
    }
	
	public static <K,V> Matcher<Map<K,V>> withOnly(K key, Matcher<V> valueMatcher){
        return inAnyOrder().withOnly(key, valueMatcher);
    }
	
	public static <K,V> Matcher<Map<K,V>> withOnly(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
        return inAnyOrder().withOnly(keyMatcher, valueMatcher);
    }
	
	public static <K,V> Matcher<Map<K,V>> withOnly(Matcher<Map.Entry<K,V>> entryMatcher){
		return inAnyOrder().withOnly(entryMatcher);
	}
	
	/**
	 * A map match builder for a map where key order is important
	 * @return
	 */
	public static MapContainsAndTypeRequired inOrder(){
		return new MapContainsAndTypeRequired(MapMatcher.ORDER.EXACT);
	}
	
	/**
	 * A list match builder for a list where item order is not important
	 * @return
	 */
	public static MapContainsAndTypeRequired inAnyOrder(){
		return new MapContainsAndTypeRequired(MapMatcher.ORDER.ANY);
	}
	
	
	/**
     * Builder for a map matcher where order and contains (only/any) must still be provided
     */
	public static class MapOrderAndContainsRequired<K,V>{
		/**
		 * A matcher which expects an empty map
		 * @return
		 */
		public Matcher<Map<K,V>> withNothing(){
			return new MapEmptyMatcher<K,V>();
		}
		
		public Matcher<Map<K,V>> withOnly(Matcher<Map.Entry<K, V>> entryMatcher){
			return withOnly().and(entryMatcher);
		}
	
		public Matcher<Map<K,V>> withOnly(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
			return withOnly().and(keyMatcher,valueMatcher);
		}
		
		public Matcher<Map<K,V>> withOnly(K key, Matcher<V> valueMatcher){
			return withOnly().and(key,valueMatcher);
		}
		
		public Matcher<Map<K,V>> withOnly(Matcher<K> keyMatcher,V value){
			return withOnly().and(keyMatcher,value);
		}
		
		public Matcher<Map<K,V>> withOnly(K key,V value){
			return withOnly().and(key,value);
		}
		
		private MapMatcher<K,V> withOnly(){
			return new MapMatcher<K,V>(MapMatcher.ORDER.ANY,MapMatcher.CONTAINS.ONLY);
		}
		
		public MapContainsRequired<K,V> inOrder(){
			return new MapContainsRequired<K,V>(MapMatcher.ORDER.EXACT);
		}
		
		public MapContainsRequired<K,V> inAnyOrder(){
			return new MapContainsRequired<K,V>(MapMatcher.ORDER.ANY);
		}
	}
	/**
	 * Builder for a map matcher where key and value type has not yet been provided
	 */
	public static class MapContainsAndTypeRequired {

		private final MapMatcher.ORDER order;
		
		MapContainsAndTypeRequired(MapMatcher.ORDER order){
			this.order = order;
		}
	
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<Map.Entry<K, V>> entryMatcher){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST).and(entryMatcher);
        }
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST).and(keyMatcher,valueMatcher);
		}
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(K key, Matcher<V> valueMatcher){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST).and(key,valueMatcher);
		}
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<K> keyMatcher, V value){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST).and(keyMatcher,value);
        }
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(K key, V value){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST).and(key,value);
        }
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withAtLeast(){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST);
        }
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(Matcher<Map.Entry<K, V>> entryMatcher){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY).and(entryMatcher);
        }

		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY).and(keyMatcher,valueMatcher);
		}

		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(K key, Matcher<V> valueMatcher){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY).and(key,valueMatcher);
		}
		
		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(Matcher<K> keyMatcher, V value){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY).and(keyMatcher,value);
        }

		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(K key, V value){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY).and(key,value);
        }

		public <K,V> IAcceptMoreMapMatchers<K,V> withOnly(){
            return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY);
        }

	}

	/**
     * Builder for a map matcher where the key and value type have already been provided
     */
	public static class MapContainsRequired<K,V> {

		private final MapMatcher.ORDER order;
		
		MapContainsRequired(MapMatcher.ORDER order){
			this.order = order;
		}

		public IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<Map.Entry<K, V>> matcher){
			return withAtLeast().and(matcher);
		}
		
		public IAcceptMoreMapMatchers<K,V> withAtLeast(K key, V value){
			return withAtLeast().and(key,value);
		}
		
		public IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<K> keyMatcher, V value){
			return withAtLeast().and(keyMatcher,value);
		}
		
		public IAcceptMoreMapMatchers<K,V> withAtLeast(K key, Matcher<V> valueMatcher){
			return withAtLeast().and(key,valueMatcher);
		}
		
		public IAcceptMoreMapMatchers<K,V> withAtLeast(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
			return withAtLeast().and(keyMatcher,valueMatcher);
		}
		
		
		public IAcceptMoreMapMatchers<K,V> withAtLeast(){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ATLEAST);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(Matcher<Map.Entry<K, V>> entryMatcher){
			return withOnly().and(entryMatcher);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(K key, V value){
			return withOnly().and(key,value);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(Matcher<K> keyMatcher, V value){
			return withOnly().and(keyMatcher,value);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(K key, Matcher<V> valueMatcher){
			return withOnly().and(key,valueMatcher);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
			return withOnly().and(keyMatcher,valueMatcher);
		}
		
		public IAcceptMoreMapMatchers<K,V> withOnly(){
			return new MapMatcher<K,V>(order,MapMatcher.CONTAINS.ONLY);
		}
	}
	
	/**
	 * Builder for a map matcher where the map key and value type still need to be provided
	 */
	public static class MapTypeRequired {
		
		private final MapMatcher.ORDER order;
		private final MapMatcher.CONTAINS contains;
		
		MapTypeRequired(MapMatcher.ORDER order,MapMatcher.CONTAINS contains){
			this.order = order;
			this.contains = contains;
		}
		
		/**
	     * Add a matcher. Synonym for {@link #add(Matcher)}
	     */
		public <K,V> IAcceptMoreMapMatchers<K,V> entries(final Matcher<Map.Entry<K, V>> matcher){
			return new MapMatcher<K,V>(order, contains).add(matcher);
		}
		

		/**
	     * Add a list of matchers
	     */
		public <K,V> IAcceptMoreMapMatchers<K,V> entries(final Iterable<? extends Matcher<Map.Entry<K, V>>> matchers){
			MapMatcher<K,V> list = new MapMatcher<K,V>(order, contains);
			for(Matcher<Map.Entry<K, V>> m : matchers){
				list.add(m);
			}
			return list;
		}
		
		@SuppressWarnings("unchecked")
		public <K,V> IAcceptMoreMapMatchers<K,V> entries(final Matcher<Map.Entry<K,V>>... matchers){
			MapMatcher<K,V> list = new MapMatcher<K,V>(order, contains);
			for(Matcher<Map.Entry<K, V>> m : matchers){
				list.add(m);
			}
			return list;
		}
		
	}
	
	private static class MapEmptyMatcher<K,V> extends AbstractNotNullMatcher<Map<K,V>>
	{
		@Override
		public boolean matchesSafely(Map<K,V> actual,MatchDiagnostics diag) {
			return actual.size() == 0;
		}
	}

	
	public interface IAcceptMoreMapMatchers<K, V> extends Matcher<Map<K, V>> {

		IAcceptMoreMapMatchers<K, V> andKey(K key);

		IAcceptMoreMapMatchers<K, V> andKey(Matcher<K> keyMatcher);

		/**
		 * Add a matcher which performs an exact match for the given value
		 */
		IAcceptMoreMapMatchers<K, V> andValue(V value);

		IAcceptMoreMapMatchers<K, V> andValue(Matcher<V> valueMatcher);

		
		IAcceptMoreMapMatchers<K, V> and(K key, V value);

		IAcceptMoreMapMatchers<K, V> and(Matcher<K> keyMatcher, V value);


		/**
		 * Synonym for {@link #and(Object))}
		 */
		IAcceptMoreMapMatchers<K, V> and(Matcher<K> keyMatcher,
				Matcher<V> valueMatcher);

		/**
		 * Synonym for {@link #and(Matcher)}
		 */
		IAcceptMoreMapMatchers<K, V> and(K key, Matcher<V> valueMatcher);

		/**
		 * Add a list of matchers
		 */
		IAcceptMoreMapMatchers<K, V> entries(
				final Iterable<? extends Matcher<Map.Entry<K, V>>> matchers);

		@SuppressWarnings("unchecked")
		IAcceptMoreMapMatchers<K, V> entries(
				final Matcher<Map.Entry<K, V>>... matchers);

//		/**
//		 * Synonym for {@link #and(Matcher)}
//		 */
//		IAcceptMoreMapMatchers<K, V> entry(final Matcher<Map.Entry<K, V>> matcher);

		/**
		 * Synonym for {@link #and(Matcher)}
		 */
		IAcceptMoreMapMatchers<K, V> add(final Matcher<Map.Entry<K, V>> matcher);

		/**
		 * Add a matcher.
		 */
		IAcceptMoreMapMatchers<K, V> and(final Matcher<Map.Entry<K, V>> matcher);

	}

	@ThreadSafe(caveats = "if no matchers are added once it's been handed off to the various threads")
	public static class MapMatcher<K, V> extends
			AbstractNotNullMatcher<Map<K, V>> implements
			IAcceptMoreMapMatchers<K, V> {

		private final Collection<Matcher<Map.Entry<K, V>>> matchers = new ArrayList<>();
		private final ORDER order;
		private final CONTAINS contains;

		
		/**
		 * The order we expect elements to be in
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
		 * How we deal with additional items
		 */
		private static enum CONTAINS {
			/**
			 * All matchers have to match. Additional elements are allowed. One
			 * matcher per item
			 */
			ATLEAST,
			/**
			 * All matchers have to match, and no additional elements are
			 * allowed. One matcher per item
			 */
			ONLY;
		}

		private MapMatcher(final ORDER order, final CONTAINS contains) {
			if (contains == null) {
				throw new IllegalArgumentException(
						"Must supply non null strictness. One of "
								+ Arrays.toString(CONTAINS.values()));
			}
			if (order == null) {
				throw new IllegalArgumentException(
						"Must supply non null order. One of "
								+ Arrays.toString(ORDER.values()));
			}
			this.contains = contains;
			this.order = order;
		}


		@Override
		protected boolean matchesSafely(Map<K, V> actual, MatchDiagnostics diag) {
			// loop though each entry in the actual map, and find a matching
	        // matcher. If all the matchers
	        // have passed once, then we have no more checks to perform
	        final List<Matcher<Map.Entry<K, V>>> matchersLeft = new ArrayList<Matcher<Map.Entry<K, V>>>(matchers);
	        List<Map.Entry<K, V>> nonMatchedEntries = new ArrayList<>();
        	final boolean diagEnabled = !diag.isNull();
	        switch (order) {
	        case ANY:
	            // in any order
	            for (final Map.Entry<K, V> entry : actual.entrySet()) {
	                // since order is not important, lets find the first matcher
	                // which matches
	            	boolean entryMatched = false;
	                matcher:for (final Iterator<Matcher<Map.Entry<K,V>>> iter = matchersLeft.iterator(); iter.hasNext();) {
	                    final Matcher<Map.Entry<K,V>> matcher = iter.next();
	                    MatchDiagnostics child = diag.newChild();
	                    
	                    if (matcher.matches(entry,child)) {
	                        // we've used the matcher, lets not use it again
	                        diag.child(child);
	                    	iter.remove();
	                    	entryMatched = true;
	                        break matcher;
	                    }
	                }
	            	if(!entryMatched && diagEnabled){
	            		nonMatchedEntries.add(entry);
	            	}
	            }
	            break;
	        case EXACT:
	            // in the same order as the matchers
	            for (final Map.Entry<K, V> entry : actual.entrySet()) {
	                // ensure we have matchers left
	                if (matchersLeft.size() == 0) {
	                    break;
	                }
	                // always use the first matcher as this should match first.
	                MatchDiagnostics child = diag.newChild();
                    
	                if (matchersLeft.get(0).matches(entry,child)) {
	                	diag.child(child);
	                    matchersLeft.remove(0);
	                } else {
	                	if(diagEnabled){
	                		nonMatchedEntries.add(entry);
	                	}
	                }
	            }
	            break;
	        }
	        boolean matched = true;
	        //don't expect any more items than matchers for CONTAINS.ONLY
	        if(CONTAINS.ONLY == contains && matchers.size() != actual.size()){
	        	if(diagEnabled && nonMatchedEntries.size() > 0){
	        		for(Map.Entry<K, V> e:nonMatchedEntries){
	        			diag.mismatched("entry.key=" + e.getKey() +",entry.value=" + e.getValue());
	        		}
	        	}
	        	matched = false;
	        }
	        
	        // if zero means each matcher has matched once
	        if(matchersLeft.size() != 0){
	        	if(diagEnabled){
	        		MatchDiagnostics child = diag.newChild();
	        		for(SelfDescribing d:matchersLeft){
		        		child.mismatched(d);
		        	}
	        		diag.child("Matchers NOT satisfied", child);
	        	}
	        	matched = false;
	        }
	        return matched;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> and(K key, V value) {
			and(AnInstance.equalTo(key), AnInstance.equalTo(value));
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> and(Matcher<K> keyMatcher,V value) {
			and(keyMatcher, AnInstance.equalTo(value));
			return this;
		}
		
		@Override
		public IAcceptMoreMapMatchers<K, V> and(Matcher<K> keyMatcher,Matcher<V> valueMatcher) {
			and(new EntryMatcher(keyMatcher, valueMatcher));
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> and(K key, Matcher<V> valueMatcher) {
			and(new EntryMatcher(AnInstance.equalTo(key), valueMatcher));
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> entries(
				Iterable<? extends Matcher<Map.Entry<K, V>>> matchers) {
			for(Matcher<Map.Entry<K, V>> m : matchers){
				and(m);
			}
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> entries(
				Matcher<Map.Entry<K, V>>... entryMatchers) {
			for(Matcher<Map.Entry<K, V>> m : entryMatchers){
				and(m);
			}
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> andValue(V value) {
			andValue(AnInstance.equalTo(value));
			return this;
		}

		public IAcceptMoreMapMatchers<K, V> andValue(Matcher<V> valueMatcher) {
			and(new EntryMatcher(null, valueMatcher));
			return this;
		}

		
		@Override
		public IAcceptMoreMapMatchers<K, V> andKey(K key) {
			andKey(AnInstance.equalTo(key));
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> andKey(Matcher<K> keyMatcher) {
			and(new EntryMatcher(keyMatcher, null));
			return this;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> add(Matcher<Map.Entry<K, V>> entryMatcher) {
			and(entryMatcher);
			return null;
		}

		@Override
		public IAcceptMoreMapMatchers<K, V> and(Matcher<Map.Entry<K, V>> entryMatcher) {
			matchers.add(entryMatcher);
			return this;
		}
		
		@Override
	    public void describeTo(final Description desc) {
	        if(desc.isNull()){
	            return;
	        }
	        
	        desc.values("entries in " + order.toString().toLowerCase() + " order matching " +  contains.toString().toLowerCase() + " (" + matchers.size() + " matchers)" , matchers);
	    }
		
		/**
		 * Provides ability to match on key, value, both or none
		 */
		private class EntryMatcher extends AbstractMatcher<Map.Entry<K, V>>{
			private final Matcher<K> keyMatcher;
			private final Matcher<V> valueMatcher;
			
			EntryMatcher(Matcher<K> keyMatcher, Matcher<V> valueMatcher){
				this.keyMatcher = keyMatcher;
				this.valueMatcher = valueMatcher;
			}
			
			public boolean matchesSafely(Map.Entry<K, V> entry, MatchDiagnostics diag){
				MatchDiagnostics child = diag.newChild();
				
				boolean matched = true;
				if(keyMatcher != null && !child.tryMatch(this, "key",entry.getKey(),keyMatcher)){
					matched = false;
				}
				if(valueMatcher != null && !child.tryMatch(this,"value", entry.getValue(),valueMatcher)){
					matched = false;
				}
				
				if(matched){
					diag.child("entry matched", child);
				} else {
					diag.child("entry match FAILED", child);	
				}
				return matched;
			} 
			
			@Override
			public void describeTo(Description desc) {
				desc.value("key", keyMatcher==null?"anything":keyMatcher);
				desc.value("value", valueMatcher==null?"anything":valueMatcher);
			}
		}

	}

}
