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

import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.javaLang.datum.PointerDatum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyPointer

Overview:
This class represents the Java address (pointer) type.

Review:          xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class TyPointer extends TyAbstractPointer implements TyJava {
    public TyPointer() { super(); }
    public TyPointer(TypeNode pointeeTp) {super(pointeeTp);}


    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        PointerDatum d = new PointerDatum(address, parent, vms.getMemory(),
                                          name, this, vms.getStore(), vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(PointerDatum.size);
        PointerDatum d = new PointerDatum(address, null, vms.getMemory(), name,
                                          this, vms.getStore(), vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public int getNumBytes() { return PointerDatum.size; }

    public String getTypeString(){ return getPointeeType().getTypeString(); }

    public String elementId(){
        return typeId();
    }

    public String typeId() { return ((TyJava)getPointeeType()).typeId(); }
    
    public boolean isReachableByWideningFrom(TyJava fromType) {
    	if (fromType instanceof TyNull) return true;

      	if (fromType instanceof TyPointer) {
    		fromType = (TyJava)((TyPointer)fromType).getPointeeType();
            return ((TyJava)getPointeeType()).isReachableByWideningFrom(fromType); }
        else
            return false ;
    }

    public void addToEnd(TypeNode t) {
        Assert.check( t instanceof TyClass || t instanceof TyJavaArray );
        super.addToEnd(t);
    }
    
    public String toString(){return "pointer to " + typeId();}
}