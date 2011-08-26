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
import tm.cpp.ast.TyUnsignedShortInt;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;


/*=========================================================================
Class: UnsignedShortDatum

Overview:
This class represents an unisigned short int data item stored in memory.
===========================================================================
*/ 
 

public class UnsignedShortDatum extends AbstractIntDatum {

	private static final TyUnsignedShortInt tp = TyUnsignedShortInt.get() ;

    public static final int size = 2 ;

	public UnsignedShortDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}

	public UnsignedShortDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add,null, m, name, timeMan);
	}
	
    protected UnsignedShortDatum(UnsignedShortDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new UnsignedShortDatum(this);
    }

    public boolean input( VMState vms ) {
        boolean success = DatumUtilities.inputInt( vms, this, size, true ) ;
        return success ;
    }
    
    public long getValue() { return getUnsignedValue() ; }
}
