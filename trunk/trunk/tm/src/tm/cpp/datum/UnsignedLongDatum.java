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

package tm.cpp.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.datum.AbstractIntDatum;
import tm.cpp.ast.TyUnsignedLong;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;


/*=========================================================================
Class: UnsignedLongDatum

Overview:
This class represents an unsigned long integer data item stored in memory.
===========================================================================
*/ 
 

public class UnsignedLongDatum extends AbstractIntDatum {

	private static final TyUnsignedLong tp = TyUnsignedLong.get() ;

    public static final int size = 8 ;

	public UnsignedLongDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}

	public UnsignedLongDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add,null, m, name, timeMan);
	}
	
    protected UnsignedLongDatum(UnsignedLongDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new UnsignedLongDatum(this);
    }

    public boolean input( VMState vms ) {
        boolean success = DatumUtilities.inputInt( vms, this, size, true ) ;
        return success ;
    }
    
    public long getValue() { return getUnsignedValue() ; }
}
