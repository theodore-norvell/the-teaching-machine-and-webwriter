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
import tm.javaLang.datum.CharDatum;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: TyChar

Overview:
This class represents the Java character (char) type.

Review:          2002 06 12     Jonathan Anderson and Michael Burton
Super-review:    xxxx xx xx     xxxxxxxxxxxxx
*******************************************************************************/

public class TyChar extends TyIntegral {
    private static TyChar instance;

    public static TyChar get () {
        if (instance == null) { instance = new TyChar(); }
        return instance;
    }

    private TyChar() { super(); }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        CharDatum d = new CharDatum(address, parent, vms.getMemory(), name, vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(CharDatum.size);
        CharDatum d = new CharDatum(address, null, vms.getMemory(), name, vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public boolean equal_types(TypeInterface t) { return t instanceof TyChar; }

    public int getNumBytes() { return CharDatum.size; }

    public String getTypeString () { return "char"; }
    public String typeId() { return "char"; }
    public String elementId() { return "C"; }
    public boolean isReachableByWideningFrom(TyJava fromType){ return false;}
}