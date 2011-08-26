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

package tm.cpp.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.datum.AbstractIntDatum;
import tm.cpp.ast.TyBool;
import tm.interfaces.Datum;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;



/*=========================================================================
Class: BoolDatum

Overview:
This class represents a boolean data item stored in memory.
===========================================================================
*/ 
 

public class BoolDatum extends AbstractIntDatum {

    private static final TyBool tp = TyBool.get() ;

    public static final int size = 1 ;

    public BoolDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
        super(add, size, p, m, name, tp, timeMan);
    }

    public BoolDatum(int add, Memory m, String name, BTTimeManager timeMan) {
        this(add,null, m, name, timeMan);
    }
    
    protected BoolDatum(BoolDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new BoolDatum(this);
    }

    public void putValue(long v ) {
        int a = address ;
        mem.putByte( a, v==0 ? (byte)0 : (byte)1 );
    }
    
    public String getValueString () { 
        if (getValue() == 0) return "false" ;
        else return "true" ;
    }

    // Must override output to get nice printing
    public void output (VMState vms) { 
        Console console = vms.getConsole ();
        if (getValue () == 0) { 
            console.putchar ('0');
        } else {
            console.putchar ('1');
        }
    }
    
    public boolean input( VMState vms ) {
        boolean success = DatumUtilities.inputInt( vms, this, 1, true ) ;
        if( !success ) return false ;
        if( getValue() > 1 ) { vms.getConsole().setFailBit() ; }
        return true ;
    }
    

}
