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
import tm.clc.ast.TyAbstractClass;
import tm.clc.datum.AbstractObjectDatum;
import tm.cpp.ast.TyClass;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;

/*=========================================================================
Class: ObjectDatum

Overview:
This class represents an datum of class or struct type.
===========================================================================
*/ 
 

 public class ObjectDatum extends AbstractObjectDatum {
    
	/** Creates a struct or class datum. Immediately after construction all
	  * subobjects and fields should be added, subobjects first and then
	  * fields.
	  */
	public ObjectDatum(int add, Datum p, Memory m, String n, TyAbstractClass tp, BTTimeManager timeMan) {
		super(add, p, m, n, tp, timeMan);
    }

	public String getValueString(){
		return type.getTypeString()+"{..}";
	}

}