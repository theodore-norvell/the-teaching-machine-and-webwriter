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
import tm.interfaces.ScalarInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;

abstract public class AbstractScalarDatum extends tm.clc.datum.AbstractDatum
									  implements ScalarInterface {

// These methods are common to all scalar Datums
	public AbstractScalarDatum(int add, int s, Datum p, Memory m, String name,
	                      TypeNode tp, BTTimeManager timeMan) {
		super(add,s,p, m, name, tp, timeMan);
	}
	
}
