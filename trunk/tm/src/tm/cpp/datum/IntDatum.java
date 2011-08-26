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
import tm.cpp.ast.TyInt;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;


/*=========================================================================
Class: IntDatum

Overview:
This class represents an integer (int) data item stored in memory.
===========================================================================
*/ 
 

public class IntDatum extends AbstractIntDatum {

	private static final TyInt tp = TyInt.get() ;

    public static final int size = 4 ;

	public IntDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}

	public IntDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add,null, m, name, timeMan);
	}
	
    protected IntDatum(IntDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new IntDatum(this);
    }

    public boolean input( VMState vms ) {
        boolean success = DatumUtilities.inputInt( vms, this, size, false ) ;
        return success ;
    }
}

