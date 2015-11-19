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
import tm.interfaces.Datum;
import tm.interfaces.ViewableST;
import tm.utilities.Assert;

class HeapRegion extends MemRegion{

    HeapRegion(BTTimeManager timeMan, int first, int size, Store store, ViewableST symTab, String name) {
        super(timeMan, first, size, store, symTab, name ) ;
}
    
    public Datum copy(){
    	Assert.check("Sorry. Copy shouldn't be called on a Heap region.");
    	return null;
    }

    /**Find space in the heap This routine does not reserve the space,
        so a datum should immediately be created that uses the space and
        that datum should be added to the store via addDatum

    Throws an exception if space can not be found.
    */
    public int findSpace(int size) {
        //Use first fit.

        if( regionDatums.size() == 0 ) {
            // This region is empty.
            int start = Math.max( 1, firstAddr ) ;
            if( start+size <= firstAddr+regionSize ) return start ;
            else Assert.apology("Virtual machine out of heap space") ; }
        else {
            int index = 0 ;
            /*DBG System.out.println("Searching from "+index); /*DBG*/
            while( index <= regionDatums.size() ) {
                // Look betwen datum index-1 and datum index, keeping in mind that either
                // of these indecies could be out of range.

                // Set start
                int start ;
                if( index == 0 ) {
                    start = firstAddr ; }
                else {
                    Datum a = (Datum) regionDatums.elementAt( index-1 ) ;
                    /*DBG System.out.println("index = "+index+" datum 'a' is from "+a.getAddress()+" to "+a.getAddress()+a.getNumBytes()  ); /*DBG*/
                    int aSize = a.getNumBytes() ;
                    start = a.getAddress() + aSize ;
                    // For zero size datums leave a 1 byte space after.
                    if( aSize == 0 ) ++start ; }
                /*DBG System.out.println("start = "+start+" limit = "+(firstAddr+regionSize)); /*DBG*/

                //Set end
                int end ;
                if( index == regionDatums.size() ) {
                    end = firstAddr+regionSize ; }
                else {
                    Datum b = (Datum) regionDatums.elementAt( index ) ;
                    end = b.getAddress() ;
                    // When the size of the datum is 0, there must be an extra byte after it
                    if( size==0 ) --end ; }

                /*DBG System.out.println("index = "+index+" start = "+start+" end = "+end ); /*DBG*/
                if( size <= end-start ) {
                    /*DBG System.out.println("Space found at: " + start ) ;/*DBG*/
                    return start ; }
                index ++ ; }
            Assert.apology("Virtual machine out of heap space") ; }
        return -1 ; /* Unreachable */ }

    public int getFrameBoundary() {
        return 0 ; }

    public String toString() { return "A Heap Region" ; }
}