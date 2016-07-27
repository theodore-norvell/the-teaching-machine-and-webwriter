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

package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.utilities.Assert;




/*=========================================================================
Class: AbstractInt

Overview:
This abstract class groups all integral types..
===========================================================================
*/ 
 

 public abstract class AbstractIntDatum extends AbstractScalarDatum {

	public AbstractIntDatum(int add, int sz, Datum p, Memory m, String n, TypeNode tp, BTTimeManager timeMan) {
		super(add,sz,p, m, n, tp, timeMan);
	}


	public void putValue(long v ) {
		int a = address ;
		for( int i=0 ; i < size ; ++i) {
			mem.putByte( a, (byte) (v & 0xFF) ) ;
			v = v >> 8 ;
			a += 1 ; }
	}
	
	public void putNativeValue(Object obj){
		Assert.check(obj instanceof Integer);
		putValue(((Integer)obj).longValue());
	}
	

	public long getValue() {
		long v = getUnsignedValue() ;
		// Sign extend.
		v = v << (8*(8-size)) ;
		v = v >> (8*(8-size)) ;
		return v ;
	}
	
	final public long getUnsignedValue() {
		long v = 0 ;
		int a = address  ; // Locn of LSB
		// Copy size bytes from mem to the size LSBs of v.
		for( int i=0 ; i < size ; ++i ) {
			long byteVal = 0xFF & (long)mem.getByte(a) ;
			v = v | byteVal << 8*i ;
			++a ; }
	    return v ;
	}
	    

	public String getValueString(){
		Long i = new Long( getValue() );
		return i.toString();
	}
	
	public void defaultInitialize(){
		putValue(0);
	}
	
	public boolean isEqual(Datum another){
		Assert.check(another instanceof AbstractIntDatum, "Can't check equality of an int to another type");
		AbstractIntDatum theOther = (AbstractIntDatum) another;
		return getValue() == theOther.getValue();
	}

}