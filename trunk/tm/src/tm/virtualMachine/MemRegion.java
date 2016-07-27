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
import tm.interfaces.RegionInterface;
import tm.interfaces.STEntry;
import tm.interfaces.TypeInterface;
import tm.interfaces.ViewableST;
import tm.utilities.Assert;

public abstract class MemRegion extends PropertyList implements RegionInterface {
    protected Store theStore ;
    protected int firstAddr ;
    protected int regionSize ;

    /* The top level datums in this region in order of their address. */
    protected BTVector regionDatums;

    private ViewableST symTab ;
    protected String name ;
    private int serialNumber ;
    private static int nextSerialNumber = -1 ;

    MemRegion(BTTimeManager timeMan, int first, int size, Store store, ViewableST symTab, String name) {
        super( timeMan ) ;
        firstAddr = first ;
        regionSize = size ;
        theStore = store ;
        regionDatums = new BTVector( timeMan ) ;
        this.symTab = symTab ;
        this.name = name ;
        this.serialNumber = nextSerialNumber-- ; 
   }

    public int getSerialNumber() { return serialNumber ; }
    
    public abstract int findSpace(int i) ;

    void addDatum(Datum newDatum) {
        int address = newDatum.getAddress() ;
        int spot = regionDatums.size() ;
        loop: while( spot > 0 ) {
            Datum nextDatum = (Datum) regionDatums.elementAt(spot-1) ;
            if( nextDatum.getAddress() < address ) break loop;
            --spot ; }
        regionDatums.insertElementAt( newDatum, spot ) ;
    }

    void removeDatum(Datum oldDatum) {
        int spot = regionDatums.size() -1 ;
        while( spot >= 0 && regionDatums.elementAt( spot ) != oldDatum ) {
            --spot ; }

        Assert.check( spot >= 0 ) ;
        regionDatums.removeElementAt( spot ) ; }

    /*===========Implementing the Datum Interface=================*/

    public int getHighlight() { return Datum.PLAIN ; }

    public Datum getParent() { return null ; }

    public String getTypeString() { return "Memory Region" ; }

    public String getName() { return name ; }

    public String getValueString() { return "Memory Region" ; }

    public int getNumBytes() { return regionSize ; }

    public int getAddress() { return firstAddr ; }

    public int getByte(int relativeAddress) {
        /* Note according to the GUI Interface doc as of 99.08.23,
        this should return an int between 0 and 255.
        But as implemented currently it returns an int from -128 to 127.
        Which is right?  The same comment applies to the Abstractdatum
        classes in the language layers */
        return theStore.mem.getByte( relativeAddress + firstAddr ) ; }

    public int getNumChildren() {
        //System.out.println( getName() +": getNumChildren() = "+
        //                    regionDatums.size() ) ;
        return regionDatums.size() ; }

    public Datum getChildAt(int i) {
        Assert.check( 0 <= i && i < getNumChildren() ) ;
        //System.out.println( getName() +": getChildAt("+ i +") = "+
        //            ((Datum)regionDatums.elementAt(i)) ) ;
        return (Datum) regionDatums.elementAt(i) ; }

 /* 2001.02.21 (mpbl) Shouldn't be called on regions as they have no parents
 */
    public int getBirthOrder(){
        Assert.check(false);
        return -1;
    }

    public String getChildLabelAt(int i)
    /*    Now what I do here is ugly.  I ask the symbol table.
          An alternative I considered was to record the names separately in
       the regions (this class), but it looked like serious changes to
       the interpreter would be needed.
          Asking the symbol is rather inefficient, since the
       symbol table is inefficient at retieving by address. But then it
       is no more inefficient then what we used to do. */
    {   Assert.check( 0 <= i && i < getNumChildren() ) ;
        Datum d = getChildAt( i ) ;
        int addr = d.getAddress() ;
        STEntry ste = symTab.getEntryAt( addr ) ;
        if( ste == null ) return "" ;
        else              return ste.getName() ;
    }

    public TypeInterface getType() { return null ; }
    
    
	public void defaultInitialize() {
		// I don't think this should ever happen.
		Assert.apology("defaultInitialize executed on memory region") ;
	}

}