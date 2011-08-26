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
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractFloatDatum64;
import tm.cpp.ast.TyDouble;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/*=========================================================================
Class: DoubleDatum

Overview:
This class represents a floating point (double) data item stored in memory.
===========================================================================
*/ 
 

public class DoubleDatum extends AbstractFloatDatum64 {

	private static final TyDouble tp = TyDouble.get() ;

	protected DoubleDatum(int add, Datum p, Memory m, String name, TypeNode tp, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}
	
	public DoubleDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}

	public DoubleDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add, null, m, name, timeMan);
	}

    protected DoubleDatum(DoubleDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new DoubleDatum(this);
    }

}

