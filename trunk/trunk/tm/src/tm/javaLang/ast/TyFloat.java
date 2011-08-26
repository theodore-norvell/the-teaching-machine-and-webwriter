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
import tm.javaLang.datum.FloatDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyFloat

Overview:
This class represents a float type.

Review:          2002 06 12     Jonathan Anderson and Michael Burton
*******************************************************************************/

public class TyFloat extends TyFloating {
    private static TyFloat instance;

    public static TyFloat get () {
        if (instance == null) { instance = new TyFloat(); }
        return instance;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        FloatDatum d = new FloatDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(FloatDatum.size);
        FloatDatum d = new FloatDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) { return t instanceof TyFloat; }

    public int getNumBytes() { return FloatDatum.size; }

    public String getTypeString () { return "float"; }
    public String typeId() { return "float"; }
    public String elementId() { return "F"; }

    public boolean isReachableByWideningFrom(TyJava fromType) {
        return fromType instanceof TyIntegral;

    }

}