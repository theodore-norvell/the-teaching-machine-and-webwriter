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

package tm.javaLang.ast;

import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.VoidDatum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

public class TyVoid extends TypeNode implements TyJava {
    private static final String CANNOT_GET_SIZE_OF_VOID =
            "Cannot get size of incomplete (void) type";


    private static TyVoid instance;

    /**
     * Returns a <code>TyVoid</code> instance with default (none/auto)
     * cv-qualification.
     * @return a <code>TyVoid</code> instance
     * @see Cpp.Analysis.SpecifierSet
     */
    public static TyVoid get () {
        if (instance == null) instance = new TyVoid();
            return instance;
    }



    public AbstractDatum makeMemberDatum(VMState vms, int address, AbstractDatum parent, String name) {
            return new VoidDatum( vms.getMemory(), this, vms.getTimeManager() ); }


    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        return new VoidDatum( vms.getMemory(), this, vms.getTimeManager() ); }


    public boolean equal_types( TypeInterface t ) {
            return t instanceof TyVoid ; }

    public String getTypeString() { return "void" ; }

    public int getNumBytes() {
            Assert.apology (CANNOT_GET_SIZE_OF_VOID);
            return -1; }

    public String typeId() { return "void"; }

    public String elementId() { return "V"; }
    
    public boolean isReachableByWideningFrom(TyJava fromType) {
        return false;
    }
}




