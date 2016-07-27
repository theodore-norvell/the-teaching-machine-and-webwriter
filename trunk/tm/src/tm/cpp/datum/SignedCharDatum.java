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
import tm.cpp.ast.TySignedChar;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;


/*=========================================================================
Class: SignedCharDatum

Overview:
This class represents a character (char) data item stored in memory.
===========================================================================
*/ 
 

public class SignedCharDatum extends CharDatum {

	private static final TySignedChar tp = TySignedChar.get() ;

    public static final int size = CharDatum.size ;

	public SignedCharDatum(int add, Datum p, Memory m, String name, BTTimeManager timeMan) {
		super(add, p, m, name, tp, timeMan);
	}

	public SignedCharDatum(int add, Memory m, String name, BTTimeManager timeMan) {
		this(add,null, m, name, timeMan);
	}

}