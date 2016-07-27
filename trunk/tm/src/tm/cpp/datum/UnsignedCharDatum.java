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
import tm.cpp.ast.TyUnsignedChar;
import tm.interfaces.Datum;
import tm.virtualMachine.Console;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;


/*=========================================================================
Class: UnsignedCharDatum

Overview:
This class represents an unsigned character data item stored in memory.
===========================================================================
*/ 
 

public class UnsignedCharDatum extends AbstractIntDatum {

	private static final TyUnsignedChar tp = TyUnsignedChar.get() ;

    public static final int size = 1 ;

	public UnsignedCharDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, size, p, m, name, tp, timeMan);
	}

	public UnsignedCharDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add,null, m, name, timeMan);
	}

	public String getValueString(){
		return "'" + DatumUtilities.asciiToString( (byte) getValue() ) + "'" ;
	}

	// Must override output to get nice printing
	public void output( VMState vms ) {
		byte val = (byte) getValue() ;
		Console console = vms.getConsole() ;
		console.putchar( (char) val ) ; }
    
    public boolean input( VMState vms ) {
        Console console = vms.getConsole() ;
        boolean success = DatumUtilities.skipWhiteSpace( console ) ;
        if( ! success ) {
            // Had to ask for more input.
            return false ; }
        else {
            char inputChar = console.peekChar(0) ;
            if( inputChar != '\0' ) {
                console.consumeChars(1) ;
                putValue( (long) inputChar ) ; }
            return true ; } }
    
    public long getValue() { return getUnsignedValue() ; }

}

