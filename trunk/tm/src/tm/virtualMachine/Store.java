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

package tm.virtualMachine;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVector;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.interfaces.ViewableST;
import tm.utilities.Assert;
import tm.utilities.Debug;

/** The Store is a collection of datums. All toplevel datums that reside in the
    stack, heap, or static memory areas should be added to this collection.
    For example, auto variables, static variables, variables created with new
    and all temps should be placed in this collection. Datums that are a part
    of a larger datum, should not --- e.g. array elements and struct fields.

    <P> Creation of a top level datum usually follows the following sequence:
    <UL>

    <LI> One of methods <code> findHeapSpace, findStackSpace, or findStaticSpace
         </code> of the store object is called.
         N.B. These routines only report what space is available, they do not
         reserve it.

    <LI> Next the Datum is constructed by calling its constructor.

    <LI> Now the datum is registered with the Store by calling
        <code> addDatum </code>. This reserves the space.
    <UL>
    The Datum is now ready to be used and will be displayed by the GUI.

      <P> When the datum is no longer needed, one should call <code>
           removeDatum </code> to free its space.



    <P> The store is layed out as follows
    <pre>
    __________________
    |                | topOfMemory
    |                |
    |     scratch    |
    |                |
    |________________|bottomOfScratch
    |                |topOfStack
    |                |
    |                |
    |     stack      |
    |                |
    |________________|bottomOfStack
    |                |topOfHeap
    |                |
    |     heap       |
    |                |
    |                |
    |________________|bottomOfHeap
    |                |topOfStatic
    |                |
    |     static     |
    |                |
    |________________| 0
    </pre>

  @see Backtrack
*/
public class Store {

    private int topOfStatic,	bottomOfHeap, topOfHeap,
                bottomOfStack, topOfStack, bottomOfScratch, topOfMemory ;
        // Partitions the memory into static, heap, stack, and scratch areas.

    private StackRegion theStatic, theStack, theScratch ;
    private HeapRegion theHeap ;


    /** Holds top level datums in order of increasing address.
      * Top level datum's must not overlap. Furthermore, no two datums should have the
      * same address. (The second point is not quite implied by the first because of zero
      * length datums).  So when finding space special consideration has to be made
      * for zero length datums. */
    BTVector topLevelData ;

    Memory mem ;

    /** If there is a top-level datum at 'address', returns the location of the datum.
     * If 'address' is within a top-level datum, returns the location of the datum.
     * If 'address' is not within a datum, returns one less than the index where it
     * such a datum should be.
       <p>Precondition: See the invariant on topLevelData.
       <p>Postcondition: result is the smallest number such that the adresses of
          all larger locations are larger. I.e.
        result is such that for all i, 0 <= i < topLevelData.size()</p> 
             <ul>
                 <li> i <= result implies  d.getAddress() <= address 
                 <li> i > result implies d.getAddress() > address 
             </ul>
       <p>where d == (Datum) topLevelData.elementAt(i).
    */
    private int bsearch(int address ) {
            int low = -1 ;
            int high = topLevelData.size()-1 ;
            
            // Invariant: 0 <= low <= high <= topLevelData.size()
            // and for all i, 0 <= i < topLevelData.size() 
            //  * i <= low implies d.getAddress() <= address
            //  * i > high implies d.getAddress()  > address
            // where d == (Datum) topLevelData.elementAt(i).

            while( low < high ) {
                int mid = (low+high+1) / 2 ;
                // low < mid <= high
                Datum d = (Datum) topLevelData.elementAt(mid) ;
                int daddr = d.getAddress() ;
                if( daddr <= address )
                    // d.getAddress() <= address
                    // forall i: i <= mid implies d.getAddress() <= address, where d ...
                    low = mid ;
                else // d.getAddress() > address
                    // forall i: i >= mid implies d.getAddress() > address , where d ...
                    // forall i: i > mid-1 implies d.getAddress() > address , where d ... 
                    high = mid-1 ; }
            return low ; }

    /** Find a datum within datum "d" that has address "address"
     * and type "type".
     * 
     * <p> Multiple datums can share the same address for two reasons.
     * First a parent may share an address with its child.
     * Secondly, siblings may share the same address when the first
     * is of length 0. With parents and children, we pick the parent
     * over the child.  (This is really a moot point since the
     * parent and child are not going to have the same type, but
     * it may matter in the future, when "virtual" chases are
     * supported.) With siblings, we take the first sibling that
     * matches. The algorithm is a depth first search with
     * children accessed in ascending child order.
     * 
     * @param d
     * @param address
     * @param type
     * @param virtual Ignored at the moment.
     * @return
     */
    private Datum chasePointerPrime( Datum d,
                                     int address,
                                     TypeInterface type,
                                     boolean virtual ) {
        
        if( address == d.getAddress() && type.equal_types( ((VMDatum)d).getType() ) ) {
            return d ; }

        else {
            for( int i=0, sz=d.getNumChildren() ; i < sz ; ++i ) {
                Datum child = d.getChildAt( i ) ;
                Datum result = chasePointerPrime( child, address, type, virtual) ;
                if( result != null ) return result ; }
            return null ; } }

    private void dumpDatum(Datum d) {
            Debug db = Debug.getInstance() ;
            if( d instanceof tm.interfaces.PointerInterface ) {
                tm.interfaces.PointerInterface p = (tm.interfaces.PointerInterface) d ;
                if( p.deref() != null ) {
                    db.msgNoNL(Debug.ALWAYS, " pointer to a "+p.deref().getTypeString()
                                + " at " + p.getValueString() ) ; }
                else if( p.isNull() ) {
                    db.msgNoNL(Debug.ALWAYS, "null pointer" ) ; }
                else {
                    db.msgNoNL(Debug.ALWAYS, "stray pointer" ) ; } }
            else if( d instanceof tm.interfaces.ScalarInterface ) {
                db.msgNoNL(Debug.ALWAYS, d.getValueString()) ; }
            else {
                db.msgNoNL(Debug.ALWAYS, "{" ) ;
                int j=0, sz=d.getNumChildren() ;
                while(true) {
                    Datum f = d.getChildAt( j ) ;
                    db.msgNoNL(Debug.ALWAYS, d.getChildLabelAt(j)+": " ) ;
                    dumpDatum( f ) ;
                    ++j ;
                    if(  j == sz ) break ;
                    db.msgNoNL(Debug.ALWAYS, ", ") ; }
                db.msgNoNL(Debug.ALWAYS, "}") ; }
    }

    ////////////// P U B L I C   P A R T//////////////////////

    /** Constructor: The sizes of the various parts of memory must be passed.
    Construct only one object. */
    public Store(BTTimeManager timeMan, Memory m, int boStatic, int toStatic, int boHeap, int toHeap,
            int boStack, int toStack, int boScratch, int toScratch, ViewableST symTab ) {

        Assert.check( boStatic==0 && boStatic < toStatic && toStatic+1 == boHeap
                    && boHeap < toHeap && toHeap+1 == boStack && boStack < toStack
                    && toStack+1 == boScratch && boScratch < toScratch ) ;

        mem = m ;
        topOfStatic = toStatic ;
        bottomOfHeap = boHeap ;
        topOfHeap = toHeap ;
        bottomOfStack = boStack ;
        topOfStack = toStack ;
        bottomOfScratch = boScratch ;
        topOfMemory = toScratch ;

        theStatic = new StackRegion(timeMan, 0, topOfStatic+1-boStatic, this, symTab, "Static Region" ) ;
        theHeap = new HeapRegion(timeMan, boHeap, toHeap+1-boHeap, this, symTab, "Heap Region") ;
        theStack = new StackRegion(timeMan, boStack, toStack+1-boStack, this, symTab, "Stack Region" ) ;
        theScratch = new StackRegion(timeMan, boScratch, toScratch+1-boScratch, this, symTab, "Scratch Region" ) ;

        topLevelData = new BTVector(timeMan) ;
    }

    /** Add a new Datum to the store */
    public void addDatum(Datum newDatum) {
        /* DBG  System.out.println("Store.addDatum."
                    + " address = " + newDatum.getAddress()
                    + " type = " + newDatum.getTypeString() ) ;*/
        /*DBG dumpStore() ; */
        int address = newDatum.getAddress() ;
        int size = newDatum.getNumBytes() ;
        int index = bsearch( address ) + 1 ;
            
        // Check that no datum in the store overlaps the new datum
        // and that no two have the same address.
        if( index < topLevelData.size() ) {
            Datum d = (Datum) topLevelData.elementAt( index ) ;
            Assert.check(   newDatum.getAddress() + newDatum.getNumBytes()
                         <= d.getAddress() ) ;
            Assert.check( d.getAddress() != address ) ; }
        if( index > 0 ) {
            Datum d = (Datum) topLevelData.elementAt( index - 1 ) ;
            Assert.check(   d.getAddress() + d.getNumBytes()
                           <= newDatum.getAddress() ) ;
            Assert.check( d.getAddress() != address ) ; }
        topLevelData.insertElementAt( newDatum, index ) ;
        if( address <= topOfStatic) {
            theStatic.addDatum( newDatum ) ; }
        else if( address <= topOfHeap ) {
            theHeap.addDatum( newDatum ) ; }
        else if( address <= topOfStack) {
            theStack.addDatum( newDatum ) ; }
        else {
            theScratch.addDatum( newDatum ) ; }
        /*DBG dumpStore() ; */ }

    /** Remove a datum from the store */
    public void removeDatum(Datum oldDatum) {
        /* DBG  System.out.println("Store.addDatum."
                    + " address = " + oldDatum.getAddress()
                    + " type = " + oldDatum.getTypeString() ) ;*/
        /*DBG dumpStore() ; */
        int address = oldDatum.getAddress() ;
        int size = oldDatum.getNumBytes() ;
        int index = bsearch( address ) ;
        Assert.check( topLevelData.elementAt(index) == oldDatum ) ;
        topLevelData.removeElementAt(index) ;
        if( address <= topOfStatic) {
            theStatic.removeDatum( oldDatum ) ; }
        else if( address <= topOfHeap ) {
            theHeap.removeDatum( oldDatum ) ; }
        else if( address <= topOfStack) {
            /*DBG*/ // System.out.println("Removing from stack");
            theStack.removeDatum( oldDatum ) ; }
        else {
            theScratch.removeDatum( oldDatum ) ; }
        /*DBG dumpStore() ;*/ }

    public MemRegion getStack() { return theStack ; }

    public MemRegion getHeap() { return theHeap ; }

    public MemRegion getStatic() { return theStatic ; }

    public MemRegion getScratch() { return theScratch ; }


    /** Obtain the top datum from the stack. */
    public Datum top() {
        return theStack.top() ; }

    /** Obtain the top datum from the scratch area */
    public Datum topScratch() {
        return theScratch.top() ; }

    /** Deallocate the top datum from the stack. */
    public void pop() {	removeDatum( top() ) ; }

    /** Deallocate the top datum from the scratch area. */
    public void popScratch() {	removeDatum( topScratch() ) ; }

    /** Set mark in the stack area */
    public void setStackMark() { theStack.setMark() ; }

    /** Set mark in the scratch area */
    public void setScratchMark() { theScratch.setMark() ; }

    /** Get the most recent marck for the stack area */
    public int getStackMark() { return theStack.getMark() ; }

    /** Get the most recent mark for the scratch area */
    public int getScratchMark() { return theScratch.getMark() ; }

    /** Erase the most recent mark for the stack area */
    public void unsetStackMark() { theStack.unsetMark() ; }

    /** Erase the most recent mark for the scratch area */
    public void unsetScratchMark() { theScratch.unsetMark() ; }

    /** Number of Datums in the scratch area */
    public int scratchSize() { return theScratch.size() ; }

    /** Find the datum that has a particular address and type.
    <P> Suppose a datum X has type C,
    and C has a parent P. Suppose also address is the address of X and type
    is the object representing P. A datum representing X cast to type P
    is returned.
    <P> If none can be found, returns null.*/
    public Datum chasePointer( int address, TypeInterface type ) {
        /*DBG System.out.println("chasePointer("+address+", ... )"); */
        int index = bsearch( address ) ;
        if( index == -1 )
            return null ;
        else {
            Datum d = (Datum) topLevelData.elementAt( index ) ;
            return chasePointerPrime( d, address, type, false ) ; } }

    /** Find the datum that has a particular address and type.
    <P> Suppose a datum X has type C,
    and C has a parent P. Suppose also address is the address of X and type
    is the object representing P. X is returned.
    <P> If none can be found, returns null.*/
    public Datum chasePointerVirtual( int address, TypeInterface type ) {
        /*DBG System.out.println("chasePointer("+address+", ... )"); */
        int index = bsearch( address ) ;
        if( index == -1 )
            return null ;
        else {
            Datum d = (Datum) topLevelData.elementAt( index ) ;
            return chasePointerPrime( d, address, type, true ) ; } }

    public void dumpStore() {
        Debug db = Debug.getInstance() ;
        db.msg(Debug.ALWAYS, "Dumping the store") ;
        for( int i = 0, sz=topLevelData.size(); i < sz ; ++i ) {
            Datum d = (Datum) topLevelData.elementAt(i) ;
            db.msgNoNL(Debug.ALWAYS, "   "+i+": address="+d.getAddress()
                              +" type = "+d.getTypeString()
                              +" value: ") ;
            dumpDatum( d ) ;
            db.msg(Debug.ALWAYS, "") ; } }

}