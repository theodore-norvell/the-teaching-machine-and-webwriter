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
import tm.javaLang.datum.ByteDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyByte

Overview:
This class represents the Java byte type.

Review:          2002 06 13     Jonathan Anderson and Michael Burton
Super-review:    xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class TyByte extends TyIntegral {
    private static TyByte instance;

    private TyByte() { super(); }

    public static TyByte get () {
        if (instance == null) { instance = new TyByte(); }
        return instance;
    }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        ByteDatum d = new ByteDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(ByteDatum.size);
        ByteDatum d = new ByteDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) {
        return t instanceof TyByte;
    }

    public int getNumBytes() { return ByteDatum.size; }

    public String getTypeString () { return "byte"; }
    public String typeId() { return "byte"; }
    public String elementId() { return "B"; }
    public boolean isReachableByWideningFrom(TyJava fromType){ return false;}
}