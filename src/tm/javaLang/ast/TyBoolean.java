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

import tm.clc.datum.AbstractDatum;
import tm.interfaces.TypeInterface;
import tm.javaLang.datum.BooleanDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyBoolean

Overview:
This class represents the boolean type.

Review:          2002 06 12     Jonathan Anderson and Michael Burton
Super-review:    xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class TyBoolean extends TyPrimitive {
    private static TyBoolean instance;

    private TyBoolean() { super(); }

    public static TyBoolean get () {
        if (instance == null) { instance = new TyBoolean(); }
        return instance;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        BooleanDatum d = new BooleanDatum(address, parent, vms.getMemory(),
                                          name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(BooleanDatum.size);
        BooleanDatum d = new BooleanDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) {
        return t instanceof TyBoolean;
    }

    public int getNumBytes() { return BooleanDatum.size; }

    public String getTypeString () { return "boolean"; }
    public String typeId() { return "boolean"; }
    public String elementId() { return "Z"; }
    
    public boolean isReachableByWideningFrom(TyJava fromType){ return false;}
}