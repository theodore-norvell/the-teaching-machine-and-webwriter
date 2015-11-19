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
import tm.interfaces.ScalarInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;
import tm.interfaces.Datum;

public class VoidDatum extends AbstractDatum
									  implements ScalarInterface {

// These methods are common to all scalar Datums
	public VoidDatum(Memory m, TypeNode tp, BTTimeManager timeMan) {
		super(0, 1, null, m, "", tp, timeMan) ;
	}
	
	protected VoidDatum(VoidDatum original){
		super(original);
	}
	
	public String getValueString(){
	    return "no value" ;}

	/* (non-Javadoc)
	 * @see tm.interfaces.Datum#defaultInitialize()
	 */
	public void defaultInitialize() {
		// I don't think this should ever happen.
		Assert.apology("defaultInitialize() executed on void datum") ;
	}
	/**
	 * Use the object, as returned by the <code>invoke</code> method of the <code>Method</code> class
     * in <code>java.lang.reflection</code>, to set the value of the datum
	 * @param obj
	 */
	
	public void putNativeValue(Object obj, VMState vms){
//    	Deliberate nop to override Assert.unsupported("Native objects");
	}
	
    public Datum copy(){
    	return new VoidDatum(this);
    }
}