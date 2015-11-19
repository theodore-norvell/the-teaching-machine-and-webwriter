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
import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.Datum;
import tm.javaLang.ast.TyBoolean;
import tm.utilities.Assert;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: BooleanDatum

Overview:
This class represents a boolean data item stored in memory.

Review:          2002 06 12     Jonathan Anderson and Michael Burton
*******************************************************************************/


public class BooleanDatum extends AbstractIntDatum {
    public static final int size = 1;

    public BooleanDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
        super(add, size, p, m, name, TyBoolean.get(), timeMan);
    }

    public BooleanDatum(int add, Memory m, String name, BTTimeManager timeMan) {
        this(add,null, m, name, timeMan);
    }

    protected BooleanDatum(BooleanDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new BooleanDatum(this);
    }

    public void putValue(long v ) {
        Assert.check( v==0 || v==1 ) ;
        super.putValue( v ) ;
    }

    public String getValueString () {
        if (getValue() != 0 ) { return "true"; }
        else { return "false"; }
    }

    public void putNativeValue(Object obj) {
   		Assert.check(obj instanceof Boolean);
   		putValue(((Boolean)obj).booleanValue() ? 1 : 0);
   }
   
    public Object getNativeValue() {
        return new Double(getValue());
    }

    public Class getNativeClass() {
        return double.class;
    }
    // Must override output to get nice printing
    public void output (VMState vms) {
        Console console = vms.getConsole ();
        if (getValue ()!=0 ) { console.putchar ('t');console.putchar ('r');console.putchar ('u');console.putchar ('e'); }
        else { console.putchar ('f'); console.putchar ('a');console.putchar ('l');console.putchar ('s');console.putchar ('e');}
    }

 /*   public boolean input(VMState vms) {
        boolean succ45ess = DatumUtilities.inputInt(vms, this, 1, true);
        if(!success) { return false; }
        if(getValue() > 1) { vms.getConsole().setFailBit(); }
        return true;
    }*/
}