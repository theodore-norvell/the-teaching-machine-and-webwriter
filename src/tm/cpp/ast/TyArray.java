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

package tm.cpp.ast;

import tm.clc.ast.TyAbstractArray;
import tm.clc.datum.AbstractDatum;
import tm.cpp.datum.ArrayDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.VMState;


public class TyArray extends TyAbstractArray implements TyCpp {
    
    // elementCount -- The number of elements per array.

    private int elementCount ;
    
    public TyArray() {
		// -1 means the size is unknown.
		elementCount = -1 ;
    }
    
    public void setNumberOfElements(int count) {
        elementCount = count ; }
        
    public int getNumberOfElements() {
        return elementCount ; }
    
    public int getNumBytes() { return elementCount * getElementType().getNumBytes() ; }

	public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
	    /*DBG System.out.println("TyArray.makeMemberDatum at "+address) ;/*DBG*/
	    Assert.check( elementCount >= 0 ) ;
	    ArrayDatum d = new ArrayDatum( address, elementCount, getElementType().getNumBytes(),
	                                    parent, vms.getMemory(), name, this, vms.getTimeManager() ) ;
        int	elementAddress = address ;
		for( int i=0, sz=elementCount ; i<sz ; ++i ) {
	        /*DBG System.out.println("Adding an element at "+elementAddress) ;/*DBG*/
	        String i_as_string = Integer.toString( i ) ;
		    AbstractDatum f = getElementType().makeMemberDatum(vms, elementAddress, d, name+"["+i_as_string+"]") ;
		    d.putElement( i, f ) ;
		    elementAddress += getElementType().getNumBytes() ; }
		return d ; }
		
	public String getTypeString() {
	    if( elementCount >= 0 ) {
	        return "array of " + elementCount + " " + getElementType().getTypeString()  ; }
	    else
	        return "array of unknown size of " + getElementType().getTypeString()  ; }
	
	public String typeId() { return typeId( "", false ) ; }
	
	public String typeId( String seed, boolean lastWasLeft ) {
	    
	    seed += ( elementCount >= 0 ) ? "["+elementCount+"]" : "[]" ;
	    if( lastWasLeft ) seed = "("+seed+")" ;
	    return ((TyCpp)getElementType()).typeId( seed, false ); 
	}

	public boolean equal_types( TypeInterface t ) {
	    // We implement `structural' equivalence.
		if( t instanceof TyArray ) {
		    TyArray ta = (TyArray) t ;
		    return ta.elementCount == elementCount
		        && getElementType().equal_types( ta.getElementType() ) ; }
		else return false ; }
}