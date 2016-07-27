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

package tm.javaLang.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.datum.AbstractFloatDatum64;
import tm.interfaces.Datum;
import tm.javaLang.ast.TyDouble;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/*******************************************************************************
 Class: DoubleDatum
 
 Overview:
 This class represents a Java double-precision (double) data item stored in
 memory.
 
 Review:          2002 06 12     Jonathan Anderson and Michael Burton
 *******************************************************************************/

public class DoubleDatum extends AbstractFloatDatum64 {    
    
    public DoubleDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
        super(add, size, p, m, name, TyDouble.get(), timeMan);
    }
    
    public DoubleDatum(int add, Memory m, String name, BTTimeManager timeMan) {
        this(add, null, m, name, timeMan);
    }

    public double getValue() {
        long bits = 0;
        int a = address + size - 1;
        for(int j = 0; j < size; j++) {
            bits = (bits << 8) | 0xFF & mem.getByte(a);
            a--;
        }
        return Double.longBitsToDouble(bits);
    }
    
    public void putNativeValue(Object obj) {
        Assert.check(obj instanceof Double);
        putValue(((Double)obj).doubleValue());
    }
    
    public Object getNativeValue() {
        return new Double(getValue());
    }
    
    public Class getNativeClass() {
        return double.class;
    }
}