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

import tm.clc.ast.TyAbstractPointer;
import tm.clc.datum.AbstractDatum;
import tm.cpp.analysis.Cpp_SpecifierSet;
import tm.cpp.analysis.Cpp_Specifiers;
import tm.cpp.datum.PointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;


public class TyPointer extends TyAbstractPointer implements TyCpp {
    
    public TyPointer() {
        super() ; }

	/**
	 * Creates a new TyPointer with the indicated cv-qualification
	 * @param cv_qualifiers a list of specifier elements in the 
	 * set (CONST, VOLATILE) - (AUTO is ignored, as default qualification)
	 */
	public TyPointer (Cpp_SpecifierSet cv_qualifiers) {
		this ();

		// At the moment we're building a flag representation ourselves
		// - may be more appropriately made a SpeciferSet method, 
		//   however SpecifierSet is generically applicable to a range
		//   of language entities. 
		// - will see how the use of SpecifierSet in other contexts plays
		//   out first
		//   DR 12-10-01
		int cv_qual_flags = 0;
		if (cv_qualifiers.contains (Cpp_Specifiers.SP_CONST)) {
			cv_qual_flags += Cpp_Specifiers.CVQ_CONST;
		}

		if (cv_qualifiers.contains (Cpp_Specifiers.SP_VOLATILE)) {
			cv_qual_flags += Cpp_Specifiers.CVQ_VOLATILE;
		}

		this.setAttributes (cv_qual_flags);
		
	}
        
	public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
		PointerDatum d = new PointerDatum( address, parent,
		                        vms.getMemory(), name, this, vms.getStore(), vms.getTimeManager() ) ;
		return d ; }
	
	public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
		int address = mr.findSpace( PointerDatum.size ) ;
		PointerDatum d = new PointerDatum( address, null,
								vms.getMemory(), name, this, vms.getStore(), vms.getTimeManager() ) ;
		vms.getStore().addDatum(d) ;
		return d ; }
	
	public String getTypeString(){
	    return "ptr to "+getPointeeType().getTypeString(); }

	public int getNumBytes() { return PointerDatum.size ; }
	
	public String typeId() { return typeId( "", false ) ; }
	
	public String typeId( String seed, boolean lastWasLeft ) {
	    if( (getAttributes() & Cpp_Specifiers.CVQ_CONST) != 0 )
	        seed = "const"+seed ;
	    seed = "*"+ seed;
	    return ((TyCpp)getPointeeType()).typeId( seed, true ); 
	}
    
}
