//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/* (C) Michael Bruce-Lockhart 2001 
 * A generator is a sequence of datums used to create a particular display.
 * Generators were created to support displays at higher levels of abstraction
 * than simple data stores which are closely tied to physical memory. Such
 * levels are particularly required in courses in data structures and algorithms
 * although they may be useful at any level.
 * 
 *     Displays for data structures such as linked lists or balanced trees quickly
 * get very complicated. Thus, one often only wants to display a few variables
 * rather than every variable in the TM. A generator holds only those datums that
 * an instructor wants to use for the root nodes of a data structure display.
 * 
 *     Root nodes because far more than just those datums are displayed. A root
 * datum might be the head pointer to a linked list (in which case the whole list
 * should be displayed), an array (display the array components), etc. 
 * 
 *     There are a number of different strategies for setting the datums in a
 * generator, vis-
 * 
 *    local strategy: datums are chosen locally, either at construction or
 *                    via a local controls (which might choose to use the
 *                    stack or the heap or both as a generator, for example).
 *                       
 *    selection strategy: there is a singleton selection mechanism which allows
 *               datums to be selected (e.g. with a mouse) from participating
 *               displays across the TM. The heap, stack and static store displays
 *               all participate but any data display plugins derived from the
 *               DataVisualizerADapter may opt to participate.
 *               
 *    external strategy: datums are added or removed from the generator by external
 *               software command.
 *               
 *    Note that there is no inherent reason for strategies to be mutually exclusive.
 *    If they are, we simply switch strategies. If not, then we enable strategies
 *    
 * */
package tm.displayEngine.generators;

import java.util.Vector;

import tm.displayEngine.DataDisplayView;
import tm.displayEngine.LinkedD3Iterator;
import tm.interfaces.Datum;
import tm.utilities.Assert;
 

public abstract class AbstractGenerator {
	
	public static final String CANT_ADD = "Can't add to a generator which isn't malleable";
	public static final String CANT_REMOVE = "Can't remove from a generator which isn't malleable";
	public static final String CANT_CLEAR = "Can't clear a generator which isn't malleable";
//	protected LinkedD3Iterator myIterator; reverted to version 95
	
	// Abstract methods to be implemented	
    public abstract int getNumChildren(); // number of datums used as display roots
    public abstract Datum getChildAt(int i);  // i'th display root datum

    // default methods
    public void refresh() {
    }
    
    public boolean isMalleable(){
    	return false;
    }
    
    // Methods which must be implemented for malleable generators only
    
	public void add(Datum datum) {
		Assert.check(CANT_ADD);
	}
	
	public void add(Vector<Datum> datums) {
		Assert.check(CANT_ADD);
	}
	
	public void add(AbstractGenerator generator) {
		Assert.check(CANT_ADD);
	}
	
	public void clear() {
		Assert.check(CANT_CLEAR);
	}

	public void remove(Datum datum){
		Assert.check(CANT_REMOVE);
	}
	
	public void remove(AbstractGenerator generator) {
		Assert.check(CANT_REMOVE);
	}
 
    // common methods
    /* The LinkedD3Iterator is used by the drawing routines for linked views
     * where the set of root datums plus all the datums connected to them in
     * general form a graph.
     * It is this graph which must be drawn. The LinkedD3Iterator handles
     * iterating over the graph without visiting any node more than once.
     */
    public LinkedD3Iterator getIterator( DataDisplayView view ) {
 //   	if (myIterator == null)  reverted to version 95 which tried to use a singleton
    		/*myIterator = */
        return /*myIterator;*/new LinkedD3Iterator(this, view);
    }
    
}