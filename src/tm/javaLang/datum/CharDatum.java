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
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractIntDatum;
import tm.interfaces.Datum;
import tm.javaLang.ast.TyChar;
import tm.utilities.Assert;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: CharDatum

Overview:
This class represents a Java character (char) data item stored in memory.

Review:          2002 06 17     Jonathan Anderson and Michael Burton
*******************************************************************************/

public class CharDatum extends AbstractIntDatum {
    public static final int size = 2;
    
    public CharDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
        super(add, size, p, m, name, TyChar.get(), timeMan);
    }

    public CharDatum(int add, Memory m, String name, BTTimeManager timeMan) {
        this(add,null, m, name, timeMan);
    }

    protected CharDatum(CharDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new CharDatum(this);
    }

    public String getValueString(){
        return "'" + DatumUtilities.unicodeToString((int) getValue()) + "'";
    }

    public long getValue() { return getUnsignedValue(); }
    
    public void putNativeValue(Object obj) {
   		Assert.check(obj instanceof Character);
   		putValue(((Character)obj).charValue());
   }
   
    public Object getNativeValue() {
        return new Character((char)getValue());
    }

    public Class getNativeClass() {
        return char.class;
    }


    // Must override output to get nice printing
    public void output(VMState vms) {
        byte val = (byte) getValue();
        Console console = vms.getConsole();
        console.putchar((char) val);
    }
    
    public boolean input(VMState vms) {
        Console console = vms.getConsole();
        boolean success = DatumUtilities.skipWhiteSpace(console);
        if(!success) {
            // Had to ask for more input.
            return false; }
        else {
            char inputChar = console.peekChar(0);
            if(inputChar == '\0') {
                console.setFailBit(); }
            else {
                console.consumeChars(1);
                putValue((long) inputChar); }
            return true;
        }
    }
}