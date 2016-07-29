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

package tm.portableDisplays;
import tm.displayEngine.D3Iterator ;
import tm.displayEngine.LinkedDatumDisplay ;
import tm.displayEngine.LinkedDisplay ;
import tm.displayEngine.generators.AbstractGenerator;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.PointerInterface;
import tm.utilities.Assert;
//import java.util.*;


/**
 * An iterator class for LinkedDatumDisplay objects. Walks the D3 tree defined
 * by a generator (an arbitrary set of top level datums) where leaves are top
 * level datums and branches are valid (non null) pointers (meaning they are
 * actually connected to datums).
 * 
 * This is distinct from a D3 iterator which walks top level datums internally
 * looking for scalars or unexpanded compounds. Linked D3 iterators make use of
 * D3 iterators to ferret out pointers.
 * 
 * Searching is depth first. See comments under D3 Iterators for discussion of D3
 * pairs and how a D3 tree (of either kind) is walked.
 * 
 * However, the problem is more complex for the linked case. Although the topology
 * of the tree is defined by the datums some work has to be done on the DatumDisplays
 * if new datums are added or their relationship changes (i.e if the topology of the
 * tree changes). This has been dealt with by adding a maintenance mode to iterators.
 * An iterator that is sent out on maintenance rebuilds or renews such topological information
 * as does exist in the DatumDisplay object associated with each datum. For example,
 * creating the DD if it is missing or updating its indirection level.
 * 
 * This is difficult to do as a visitor because the fixups are intimately related to (and
 * distributed throughout) the rather complex iteration process itself. Instead, the tree
 * is treated as if it were a railroad. Walking an iterator over it is akin to running a
 * train - all we are interested in is the stations it has to stop at. Railroad maintenance,
 * however, is more detailed. We are concerned with the condition of the track between the
 * stations. Instead of treating that differently, we do what the old railroads did and send
 * out a maintenance train.
 * 
 * Walking a linked tree is complicated by the fact that the tree can double back on
 * itself. Thus we have be able to tell during the course of an iteration whether a
 * node has already been visited or not. Since our iterations are designed to be one
 * way, the solution used is to stain a node each time we visit it. When the iterator
 * is reset, it picks up a new stain. Thus a visit on a particular pass is easily
 * detected by checking the stain. Of course, there are a finite no. of stains available.
 * Here we use an integer stain. The presumption is that a node cannot keep an old stain
 * long enough to cycle the entire integer set around again.
 * 
 * NOTE: Generators and regions are closely related (indeed, at the moment I think the
 * correct approach to them would be to make region a special class of generator. That is
 * a region is a generator that has the property that its sets of datums span a contiguous
 * address space. Ooops, a datum already has that property so it would be more logical to
 * derive generators FROM regions, loosening the constraint. Unhappily, loosening constraints
 * breaks polymorphism. The looser constraint should always be farther up the tree. Hah! If
 * only mother nature were so neat!) ANYWAY, at the moment, Regions are datums with the special
 * property that their children (the actual datums in the region) don't recognise their parentage.
 * Thus, although we have not quite determined finally the relationship between generators and
 * regions, they are sufficiently close that generators better be consistent with the inconsistency
 * of regions. Thus we need special code at the top level. SI-IIGH! Extra Comment. I have since
 * found this convenient since it conforms to the notion of a top-level datum (i.e., one without
 * a parent). If we were consistent in setting top level datums parent to their region we would
 * require special code to detect a top level datum - namely that one's grandparent was null.
 * 
 * 2007.03.19 Iterator can no longer be put on maintenance. Instead a refresh is called on it,
 * forcing it to do maintenance on the entire tree at once.
 * 
 * @author Michael Bruce-Lockhart
 * @since April 12, 2001
 * @see D3Iterator
 * @see LinkedDisplay
 * @see LinkedDatumDisplay
 */

public class LinkedD3Iterator1 extends Object {
    /**
     * The root node for the whole tree, generally the first child
     *  of the generator
     */
    
    private tm.portableDisplays.LinkedDatumDisplay myRoot;    // The first Datum Display in the generator

    /**
     * The current high level node that the iterator is at.
     */
    private tm.portableDisplays.LinkedDatumDisplay current;

    /**
     * The number of the kid that is the root of the sub-tree on which
     * the iterator is located
     */
    private int kid;                // Which root D3 object am I on?

    /**
     * The particular view of memory to which the DatumDisplays
     * of this tree are representing.
     */
    private DataDisplayView theView;

    /**
     * The generator contains the set of root node datums for this tree
     * 
     * @see AbstractGenerator class, RegionGenerator Class
     */
    private AbstractGenerator myGenerator;

    /**
     * When the doMaintenance flag is set the iterator does maintenance
     * on the tree by (a) adding DatumDisplay Objects for datums that
     * lack them (b)updating the indirection level of the DatumDisplay
     * objects (c) updating the link objects.
     * The flag should be set during and the tree walked once whenever
     * its topology may need to be updated (e.g. after a refresh). It
     * should be cleared when a normal walk is being done.
     * 
     * @since April 11, 2001
     * @see onMaintenance()
     * @see offMaintenance()
     */
    private boolean doMaintenance;      // Flag to force maintenance on tree
    private int myStain;
    private static int stain = 0;
    
    


/**
 * Constructor. Designed to be called just by the generator.
 * LinkedD3Iterators should be obtained directly from the generator
 * via its getIterator() method.
 * 
 * @param generator The generator which contains the datums that act as the root
 * nodes of the LinkedDisplay
 * @param view The view of Memory as returned by the getDisplayString() method
 * of the LinkedDisplay class
 */

// Public Interface
    public LinkedD3Iterator1(AbstractGenerator generator, DataDisplayView view){
        myGenerator = generator;
        theView = view;
        myStain = nextStain();
        kid = 0;
        doMaintenance = false;
        if (generator.getNumChildren() == 0)
            myRoot = null;
        else {
            myRoot = tm.portableDisplays.LinkedDatumDisplay.getLDD(generator.getChildAt(0), theView);
            if (myRoot == null) {
                myRoot = new tm.portableDisplays.LinkedDatumDisplay(generator.getChildAt(0), theView, 0);
            }
        }
        current = myRoot;
}

    /**
     * Resets the iterator to the first root node, effectively
     * making all nodes unvisited. It does NOT change the maintenance status.
     */
    
    public void reset(){
        current = myRoot;
        kid = 0;
        myStain = nextStain();
    }
    
 /**
  * Put the iterator on maintenance. To be effective, the iterator
  * should be required to walk the entire tree
  
 
    void onMaintenance(){
        doMaintenance = true;
    }*/
    
 /**
  * Take the iterator off maintenance. To be effective, the iterator
  * should be allowed to walk the entire tree before taking it off.
  

    void offMaintenance(){
        doMaintenance = false;
    }*/
    
    /**
     * Puts the iterator on maintenance and walks the entire tree before
     * taking it off. Refresh should really be a method 
     * of the generator and not of the tree but I put it here since there
     * is no generic way to write it as there has to be DataDisplayView 
     * associated with every iterator. Thus each generator would have to
     * implement its own refresh method (which would have exactly the same code).
     * iterator is reset afterwards so it starts at the beginning.
     */
    public void refresh(){
    	reset();
    	doMaintenance = true;
        while(!atEnd())
            increment();
        doMaintenance=false;
        reset();
    }

    /**
     * Advance to the next topLevel D3 pair, using depth-first search
     */
    
 /* This has been reverted to Version 95 as has the getIterator method in AbstractGenerator*/  
    public void increment(){
        Assert.check(current != null);
        // uncomment to tree trace: putting it before actual increment catches the first node
//               System.out.println("Top level: " + current.toString());
               current.setStain(myStain); // leaving the node. Time to stain it
               current = oneStep(current);  // one step from current
           // special code to handle the fact that top level datums DON'T treat the generator as a parent!!
               if (current == null)
                   if (++kid < myGenerator.getNumChildren()){ // More root datums
                       current = tm.portableDisplays.LinkedDatumDisplay.getLDD(myGenerator.getChildAt(kid), theView);
                       if (doMaintenance && current == null) // If no associated dd make it only if on maintenance
                           current = new tm.portableDisplays.LinkedDatumDisplay(myGenerator.getChildAt(kid), theView, 0);
                       Assert.check(current != null);
                   }
                       
       }
    
    
    /**
     * Takes one step through tree to next top datum
     *         IN pseudo code (from is a top level datum)
     *             if (pointer != null) return *pointer // Not a leaf node
     *             while (from = backup(from) != null) // requires a back pointer
     *                 pointer = getNextPointer(from) // requires an iterator
     *                 if pointer != null return *pointer
     *             return null
     * 
     * @param from
     */
    protected tm.portableDisplays.LinkedDatumDisplay oneStep(tm.portableDisplays.LinkedDatumDisplay from){
   // Get the from D3's interior iterator
        D3Iterator1 iterator = from.getIterator();
        tm.portableDisplays.LinkedDatumDisplay deref = getNextDeref(iterator);
        
        if (deref != null) { // from is not a leaf node since it has at least one pointer
            deref.setBackPointer(from);
            if (doMaintenance)
                updateILevel(from, deref);
            return deref;
        }
        while ( (from = from.getBackPointer()) != null) {// backup one level
            iterator = from.getIterator();
            iterator.increment();
            deref = getNextDeref(iterator);
            if (deref != null)
                return deref;
        }
        return null;
    }

    /**
     * Starting from the D3 at the iterator, retrieve the next 
     *     D3 in the tree that is the target of a pointer. The datum
     *     actually returned is the Top level datum of the one being
     *     dereferenced
     * 
     * @param iterator internal iterator for the top level D3 pair which finds all
     * internal scalar nodes
     */

    private tm.portableDisplays.LinkedDatumDisplay getNextDeref(D3Iterator1 iterator){
        while (!iterator.atEnd()) { // Pick up the next datum
        	tm.portableDisplays.LinkedDatumDisplay node = (tm.portableDisplays.LinkedDatumDisplay)iterator.getNode();
            if (node.getDatum() instanceof PointerInterface){ // is it a pointer
                PointerInterface pointer = (PointerInterface)node.getDatum();
                if (pointer.deref() != null) {// if it really points to something
                    Datum targetDatum = topLevel(pointer.deref());
                    tm.portableDisplays.LinkedDatumDisplay target = tm.portableDisplays.LinkedDatumDisplay.getLDD(targetDatum, theView);
                    if (doMaintenance) {
                        if (target == null) { // oops! must have linked up a new datum
                            target = new tm.portableDisplays.LinkedDatumDisplay(targetDatum, theView, node.getIndirection() + 1);
                        }
                        // Make sure the link is updated
                        if (targetDatum != pointer.deref()) // The link should point at the interior d3
                            node.getLink().update(tm.portableDisplays.LinkedDatumDisplay.getLDD(pointer.deref(), theView));
                        else
                            node.getLink().update(target);
                    } else Assert.check(target != null);   // All datums are picked up on maintence runs
                    if (target.getStain() != myStain)
                        return target;
                }
                else if (doMaintenance){
                    node.getLink().update(null);
                    node.getLink().setNull(pointer.isNull());
                }
            }
            iterator.increment();
        }
        return null;    // Didn't find anything
    }
    
    private Datum topLevel(Datum datum){
        Datum parent = datum.getParent();
        while (parent != null) { // Make sure to return top level datum
            datum = parent;
            parent = parent.getParent();
        }
        return datum;
    }
    
    private void updateILevel(tm.portableDisplays.LinkedDatumDisplay from, tm.portableDisplays.LinkedDatumDisplay to){
        Assert.check(doMaintenance);     // This is a maintenance only routine
        if (to.getIndirection() > 0  && // not a root node
            to.getStain() != myStain) // not been visited before
                to.setIndirection(from.getIndirection() + 1);
    }
    
        
    public tm.portableDisplays.LinkedDatumDisplay getNode(){
        Assert.check (current != null);
        return current;
    }
    
    public boolean atEnd(){
        return (current == null);
    }
    
    public boolean atStart(){
        return (current == myRoot);
    }
    
    public String toString(){
        return "LinkedD3Iterator for " + myGenerator.toString()
        + " at " + current.toString();
    }
 
    private static int nextStain(){
        stain++;
        if (stain == Integer.MAX_VALUE)
            stain = Integer.MIN_VALUE;
        return stain;
    }

} 
