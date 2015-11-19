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

import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.VoidDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

public class TyNone extends TypeNode implements TyCpp {
	private static final String INVALID_OPERATION = 
		"Trying to perform {0} with non-existent type";

	private TyNone() {
		super() ; }

	private static TyNone instance;

	/**
	 * Returns a <code>TyNone</code> instance
	 * @return a <code>TyNone</code> instance
	 */
	public static TyNone get () {
		if (instance == null) instance = new TyNone ();
		return instance;
	}

	public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
		return new VoidDatum (vms.getMemory (), this, vms.getTimeManager());
		//		Assert.apology (INVALID_OPERATION, "makeMemberDatum");
		//		return null; 
	}

	public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
		return new VoidDatum (vms.getMemory (), this, vms.getTimeManager());
		//		Assert.apology (INVALID_OPERATION, "makeDatum");
		//	    return null; 
	}

	public boolean equal_types( TypeInterface t ) {
		return t instanceof TyNone ; }
	
	public String getTypeString() { return "" ; }

	public int getNumBytes() { 
		Assert.apology (INVALID_OPERATION, "getNumBytes");
		return -1; }
	
	public String typeId() { return typeId( "", false ) ; }
	
	public String typeId( String seed, boolean lastWasLeft ) {
	    return seed ;
	}
}

