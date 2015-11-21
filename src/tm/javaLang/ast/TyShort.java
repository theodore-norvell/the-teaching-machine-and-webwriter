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
import tm.javaLang.datum.ShortDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyShort

Overview:
This class represents the Java short integer (short) type.

Review:          2002 06 13     Jonathan Anderson and Michael Burton
*******************************************************************************/

public class TyShort extends TyIntegral {
    private static TyShort instance;

    private TyShort() { super(); }

    public static TyShort get () {
        if (instance == null) { instance = new TyShort(); }
        return instance;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        ShortDatum d = new ShortDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(ShortDatum.size);
        ShortDatum d = new ShortDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) { return t instanceof TyShort; }

    public int getNumBytes() { return ShortDatum.size; }

    public String getTypeString () { return "short"; }
    public String typeId() { return "short"; }
    public String elementId() { return "S"; }
    
    public boolean isReachableByWideningFrom(TyJava fromType) {
        return fromType instanceof TyByte;
    }
    
}