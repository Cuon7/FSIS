package a3_1801040028.fsis;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;

import utils.DomainConstraint;
import utils.OptType;
import utils.DOpt;
import utils.AttrRef;
import utils.EmptyException;
import utils.NotFoundException;

/**
 * @overview: SortedSet is a set which make the objects sorted in the ascending order based on the result of comparing them 
 * @attributes: elements    SortedSet<Comparable>
 * @objects: A typical SortedSet object has S = {x1..xn}. x1,...,xn are elements
 * @abstract_properties: mutable(elements) = true /\ optional(elements) = false /\
 * 
 * @author: Cuon7	
 */
public class SortedSet {
	
	@DomainConstraint(type = "Vector", mutable = true, optional = false)

	public Vector<Comparable> elements;
        
        /**
         * @effects:
         * <pre>
         *      create an empty SortedSet<>
         * </pre>
         */
	public SortedSet() {
		elements = new Vector<Comparable>();
	}
	
	/**
	 * @effects:
	 *           <pre>
	 *        if no comparable y is in Set 
	 *           add Comparable y
	 *        else 
	 *        	 Sort the set
	 *           </pre>
	 */      
        @DOpt(type = OptType.MutatorAdd) @AttrRef("insert")
        public void insert(Comparable y) {
        if (search(y) == false) {
            elements.add(y);        
        for (int i =0; i<size(); i++)  {
            for (int j = i+1; j<size(); j++) {
                if(elements.get(i).compareTo(elements.get(j)) > 0) {
                    Comparable c = elements.get(i);
                    elements.set(i, elements.get(j));
                    elements.set(j, c);
                }
            }
        }
    }else 
            System.out.println("Element exist");
}

	/**
	 * @effects:
	 * 			<pre>
	 *		if comparable y is not in Sorted Set
	 * 			return false
	 * 		else
	 * 			return true
	 * 			</pre>
	 * @param: Comparable y
	 */
	@DOpt(type = OptType.ObserverContains) @AttrRef("search")
	public boolean search(Comparable y) {
            return elements.contains(y);
	}
	
	/**
	 * @effect:
	 *                   <pre>
	 *              if Set is empty
         *                  throw EmptyException
         *              if input element does not match
         *                  throw NotFoundException
         *              else
         *                  remove this input element
         *                  </pre>
	 * @param: Comparable y
	 */
	@DOpt(type = OptType.MutatorRemove) @AttrRef("remove")
	public void remove(Comparable y) {
		if (elements.isEmpty()) {
                        throw new EmptyException("SortedSet.remove: Empty set");
		} else if (!search(y)) {
			throw new NotFoundException("SortedSet.remove: Element does not exist!");
		} else {
			elements.remove(y);
                        
		}
	}        
	
	/**
	 * @effects return the size of <tt>this</tt>
	 * 
	 */
	@DOpt(type = OptType.ObserverSize) @AttrRef("size")
	public int size() {

		return elements.size();
	} 

	/**
	 * @effects
	 *          <pre>
	 *            if this satisfies abstract properties
	 *              return true
	 *            else
	 *              return false
	 *          </pre>
	 */	
	@DOpt(type = OptType.Helper)
	public boolean repOK() {
		if (elements == null ) {
			return false;
		}

		for (int i = 0; i < size() - 1; i++) {
			Comparable c = elements.get(i);
			for (int j = i + 1; j < size(); j++) {
				if (c.compareTo(elements.get(i)) <= 0) {
					return false;
				}
			}
		}

		return true;
	}      
	
	/**
	 * @effects: 
	 * <pre>
	 *           if size == 0
	 * 		return empty set
	 *           else
	 * 		print out and return set
	 * </pre>
	 */
	@DOpt(type = OptType.Default)
	@Override
	public String toString() {
		if (size() == 0) {
			return "SortedSet:{}";
                }
		String S = "SortedSet:{";
		Iterator<Comparable> iterator = elements();
		while (iterator.hasNext()) {
			S += iterator.next();
		}
		S = S + "}";
		return S;
	}
        
        @Override
	public boolean equals(Object o) {
		if (!(o instanceof SortedSet)) {
			return false;
		}
		return elements.equals(((SortedSet) o).elements);
	}

	/**
	 * @effects
	 *          <pre>
	 * 		if set is empty
	 * 			throw EmptyException
	 * 		else
	 * 			return the element to generator for functioning
	 *          </pre>
	 * @requires <tt>this</tt> must stay unchanged when using generator 
	 */
	@DOpt(type = OptType.ObserverIterator)
	public Iterator<Comparable> elements() throws EmptyException {
		if (elements.isEmpty()) {
			throw new EmptyException("SortedSet.iterator: Empty set");
		}
		return new SortedSetGen();
	}
	
	// generator
	/**
	 * @overview: show the generator containing elements
	 * @attributes:
	 * <pre>
	 * ind Integer
         * </pre>
	 * @abstract_properties: 
         * mutable(elements) = true /\ optional(elements) = false /\
         * mutable(ind) = false /\ min(ind) = 0 /\
	 * index<SortedSetGen.size() /\ SortedSetGen.new =[x1....] /\
	 * 	
	 */
	private class SortedSetGen implements Iterator<Comparable>{
		@DomainConstraint(type = "Integer", mutable = false, min = 0)
		private int ind; // next index
		
		//constructor methods
		public SortedSetGen() {
			ind=0;
		}
		
		
		/**
		 * @effects: 
		 * if there are more elements 
		 * 		return true 
		 * else 
		 * 		return false
		 */
		@Override
		public boolean hasNext() {
                    return ind < size();
		}

		
		/**
		 * @effects 
		 * 		
		 * 	  <pre>
		 * 		if there are more result to yield
		 * 			returns the next result
		 * 			and adjust the state of this to record 
		 * 		else
		 * 			throw NoSuchElementException
		 * 
		 * 		</pre>
		 */
		@Override
		public Comparable next() throws NoSuchElementException {			
			if(hasNext()== true){
				Comparable next = elements.get(ind);
				ind++;
				return next;			
			}else{
				throw new NoSuchElementException("SortedSet.elements");
			}
		}
		
	}
	
}
