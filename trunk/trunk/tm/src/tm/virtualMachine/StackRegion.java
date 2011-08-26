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

import tm.backtrack.BTStack;
import tm.backtrack.BTTimeManager;
import tm.interfaces.Datum;
import tm.interfaces.ViewableST;
import tm.utilities.Assert;

/** Stack Regions must be used in a FIFO manner */
class StackRegion extends MemRegion{

    private BTStack marks ;

    StackRegion(BTTimeManager timeMan, int first, int size, Store store, ViewableST symTab, String name) {
        super(timeMan, first, size, store, symTab, name) ;
        marks = new BTStack(timeMan) ;
        setMark() ; }

    public Datum copy(){
    	Assert.check("Sorry. Copy shouldn't be called on a Stack region.");
		return null;
    }

    /** Find space on the stack. This routine does not reserve the space,
        so a datum should immediately be created that uses the space and
        that datum should be added to the store via addDatum.

        Throws an exception if no space can be found. */
    public int findSpace(int size) {
        int nextAddr ;
        int vectorSize = size() ;
        if( vectorSize == 0 ) { nextAddr = Math.max( 1, firstAddr ) ; }
        else {
            Datum topDatum = get(vectorSize-1) ;
            int topDatumAddr = topDatum.getAddress() ;
            int topDatumSize = topDatum.getNumBytes() ;
            if( topDatumSize == 0 ) {
                nextAddr = topDatumAddr + 1 ; }
            else {
                nextAddr = topDatumAddr + topDatumSize ; } }

        Assert.apology( nextAddr + size < firstAddr + regionSize,
                        "Virtual Machine Stack overflow" ) ;

        /*DBG System.out.println("Space found at: " + nextAddr ) ;*/
        return nextAddr ; }

    Datum top() {
        int vectorSize = regionDatums.size() ;
        return get(vectorSize-1) ; }

    Datum get(int i) {
        return (Datum) regionDatums.elementAt(i) ; }

    int size() {
        return regionDatums.size() ; }

    void setMark() {
        marks.push( new Integer( size() ) ) ; }

    void unsetMark() {
        marks.pop() ; }

    int getMark() {
        Integer topMark = (Integer) marks.top() ;
        return topMark.intValue() ; }

    public int getFrameBoundary() {
        return getMark() ; }

    public String toString() { return name; }

}